package com.mebelshop.mebelshop_mobile.model

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import android.view.PixelCopy
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.filament.Engine
import com.google.android.filament.gltfio.FilamentAsset
import com.google.ar.core.Anchor
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream

private const val kMaxModelInstances = 10

class ARModel {
    private var _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> get() = _listProduct


    fun refreshListProduct() {
        _listProduct.postValue(DataMobile().listProduct)
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        anchor: Anchor,
        modelPath: String
    ): AnchorNode {
        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
        val modelNode = ModelNode(
            modelInstance = modelInstances.apply {
                if (isEmpty()) {
                    this += modelLoader.createInstancedModel(modelPath, kMaxModelInstances)
                }
            }.removeLast()
        ).apply {
            isEditable = true
            isScaleEditable = false
            isPositionEditable = false
            isRotationEditable = false
        }
        val boundingBoxNode = CubeNode(
            engine,
            size = modelNode.extents,
            center = modelNode.center,
            materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
        ).apply {
            isVisible = false
        }
        modelNode.addChildNode(boundingBoxNode)
        anchorNode.addChildNode(modelNode)

        listOf(modelNode, anchorNode).forEach {
            it.onEditingChanged = { editingTransforms ->
                boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
            }
        }
        return anchorNode
    }

    fun createAnchorNode(
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        anchor: Anchor,
        model: FilamentAsset
    ): AnchorNode {
        val anchorNode = AnchorNode(engine = engine, anchor = anchor)
        val modelNode = ModelNode(
            modelInstance = modelInstances.apply {
                if (isEmpty()) {
                    this += modelLoader.createInstance(model) as ModelInstance
                }
            }.removeLast()
        ).apply {
            isEditable = true
            isScaleEditable = false
            isPositionEditable = false
            isRotationEditable = false
        }
        val boundingBoxNode = CubeNode(
            engine,
            size = modelNode.extents,
            center = modelNode.center,
            materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
        ).apply {
            isVisible = false
        }
        modelNode.addChildNode(boundingBoxNode)
        anchorNode.addChildNode(modelNode)

        listOf(modelNode, anchorNode).forEach {
            it.onEditingChanged = { editingTransforms ->
                boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
            }
        }
        return anchorNode
    }

    private fun saveImageBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String = "Screenshot_${System.currentTimeMillis()}"
    ) {
        val contentResolver = context.contentResolver
        val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
        }

        try {
            val uri = contentResolver.insert(imageUri, contentValues)
            uri?.let {
                val outputStream: OutputStream? = contentResolver.openOutputStream(it)
                outputStream?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.flush()
                    Toast.makeText(context, "Сохранено в галерею", Toast.LENGTH_SHORT).show()
                }
            } ?: throw Exception("Не удалось создать URI для изображения")
        } catch (e: Exception) {
            Toast.makeText(context, "Ошибка при сохранении: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    fun makeBitmapFromScreenshot(
        arSceneView: ARSceneView,
        context: Context
    ) {
        val bitmap = Bitmap.createBitmap(
            arSceneView.width, arSceneView.height,
            Bitmap.Config.ARGB_8888
        )

        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()

        CoroutineScope(Dispatchers.IO).launch {

        }
        PixelCopy.request(arSceneView, bitmap, { copyResult ->
            if (copyResult === PixelCopy.SUCCESS) {
                saveImageBitmapToGallery(context, bitmap)
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))

    }
}
