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
import io.github.sceneview.ar.ARSceneView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream

class ARModel(){
    fun saveImageBitmapToGallery(
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


    fun takePhoto(
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
            if (copyResult == PixelCopy.SUCCESS) {
                saveImageBitmapToGallery(context, bitmap)
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))

    }

}