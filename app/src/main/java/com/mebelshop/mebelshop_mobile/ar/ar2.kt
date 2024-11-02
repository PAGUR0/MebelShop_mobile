package com.mebelshop.mebelshop_mobile.ar

import Arrow_left
import Arrow_right
import CrosshairSvgrepoCom
import Duplicate
import Plane_off
import Plane_on
import SearchIcon
import Trashcan
import android.R.attr.height
import android.R.attr.width
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.PixelCopy
import android.view.Surface
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.google.android.filament.View
import com.google.android.filament.gltfio.FilamentAsset
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.SharedCamera
import com.google.ar.core.TrackingFailureReason
import com.mebelshop.mebelshop_mobile.DataMobile
import com.mebelshop.mebelshop_mobile.Product
import com.mebelshop.mebelshop_mobile.R
import com.mebelshop.mebelshop_mobile.getImageFromAssets
import com.mebelshop.mebelshop_mobile.ui.theme.AppTypography
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.ARSession
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.ar.rememberARCameraStream
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.OutputStream


//private const val kModelFile1 = "models/example_model_1.glb"
//private const val kModelFile2 = "models/example_model_2.glb"
//private const val kModelFile3 = "models/example_model_3.glb"
//private const val kModelFile4 = "models/example_model_4.glb"
//private const val kModelFile5 = "models/example_model_5.glb"
private const val kModelFile6 = "models/office_chair_1.glb"
private const val kModelFile7 = "models/school_chair_1.glb"
private const val kModelFile8 = "models/table_1.glb"


private const val kMaxModelInstances = 10


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AR2Preview(){
    AR2(kModelFile6, onBack = {})
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AR2(selectedPathToModel: String? = null, onBack: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    val (screenWidth, screenHeight) = getScreenSize(context)

    val centerX = screenWidth / 2f
    val centerY = screenHeight / 2f

    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    BackHandler {
        onBack()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val materialLoader = rememberMaterialLoader(engine)
        val cameraNode = rememberARCameraNode(engine)
        val childNodes = rememberNodes()
        val view = rememberView(engine)
        val renderer = rememberRenderer(engine)
        val scene = rememberScene(engine)
        val collisionSystem = rememberCollisionSystem(view)

        var planeRenderer by remember { mutableStateOf(true) }

        val cameraStream = rememberARCameraStream(materialLoader)

        val modelInstances = remember { mutableListOf<ModelInstance>() }
        var trackingFailureReason by remember {
            mutableStateOf<TrackingFailureReason?>(null)
        }
        var frame by remember { mutableStateOf<Frame?>(null) }

        val bottomSelectionSheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        var showCrosshair by remember { mutableStateOf(false) }

        val catalogItems = remember {
            DataMobile().listProduct!!
        }

        var selectedModel by remember { mutableStateOf<String?>(null) }


        if (selectedPathToModel != null) {
            selectedModel = selectedPathToModel
            showCrosshair = true
        }

        var rotationAngle by remember { mutableStateOf(0f) }
        var initialRotation by remember { mutableStateOf(dev.romainguy.kotlin.math.Quaternion()) }
        var selectedNode by remember { mutableStateOf<ModelNode?>(null) }

        var longPress by remember { mutableStateOf(false) }
        var showMenuModel by remember { mutableStateOf(false) }
        var modelPosition by remember { mutableStateOf(Float3(0f, 0f, 0f)) }
        var y_val by remember { mutableStateOf(0f) }

        var duplicateNode by remember { mutableStateOf(false) }

        var sharedSession: Session? = null
        var sharedCamera: SharedCamera? = null

        val captureHelper = CaptureHelper()
        var arSceneView: ARSceneView? = null

        Scaffold { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
            ) {
                var initialRotation by remember { mutableStateOf(Float3(0f, 0f, 0f)) }
                var initialPosition by remember { mutableStateOf(Float3(0f, 0f, 0f)) }

                ARScene(
                    modifier = Modifier
                        .fillMaxSize(),
                    childNodes = childNodes,
                    engine = engine,
                    view = view,
                    renderer = renderer,
                    scene = scene,
                    modelLoader = modelLoader,
                    collisionSystem = collisionSystem,
                    sessionFeatures = setOf(
                        Session.Feature.SHARED_CAMERA
                    ),
                    sessionConfiguration = { session, config ->
                        config.depthMode =
                            when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                                true -> Config.DepthMode.AUTOMATIC
                                else -> Config.DepthMode.DISABLED
                            }
                        config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
                        config.lightEstimationMode =
                            Config.LightEstimationMode.ENVIRONMENTAL_HDR
                    },
                    cameraNode = cameraNode,
                    planeRenderer = planeRenderer,
                    cameraStream = cameraStream,
                    onSessionCreated = {

                    },
                    onTrackingFailureChanged = {
                        trackingFailureReason = it
                    },
                    onSessionUpdated = { session, updatedFrame ->
                        frame = updatedFrame
                    },
                    onGestureListener = rememberOnGestureListener(
                        onSingleTapConfirmed = { _, node ->
                            if (node == null) {
                                if (showMenuModel) {
                                    showMenuModel = false
                                }

                                selectedModel?.let { modelPath ->
                                    if (modelInstances.isNotEmpty()) {
                                        modelInstances.clear()
                                    }

                                    val hitResults = frame?.hitTest(centerX, centerY)
                                    hitResults?.firstOrNull {
                                        it.isValid(
                                            depthPoint = false,
                                            point = false
                                        )
                                    }?.createAnchorOrNull()
                                        ?.let { anchor ->
                                            childNodes += createAnchorNode(
                                                engine = engine,
                                                modelLoader = modelLoader,
                                                materialLoader = materialLoader,
                                                modelInstances = modelInstances,
                                                anchor = anchor,
                                                modelPath = modelPath
                                            )
                                        }
                                }

                                if (duplicateNode) {
                                    if (modelInstances.isNotEmpty()) {
                                        modelInstances.clear()
                                    }

                                    val hitResults = frame?.hitTest(centerX, centerY)
                                    hitResults?.firstOrNull {
                                        it.isValid(
                                            depthPoint = false,
                                            point = false
                                        )
                                    }?.createAnchorOrNull()
                                        ?.let { anchor ->
                                            childNodes += createAnchorNode(
                                                engine = engine,
                                                modelLoader = modelLoader,
                                                materialLoader = materialLoader,
                                                modelInstances = modelInstances,
                                                anchor = anchor,
                                                model = selectedNode!!.model
                                            )
                                        }
                                    duplicateNode = false
                                }

                                selectedModel = null
                                showCrosshair = false
                            }

                            if (node != null) {
                                node?.let {
                                    selectedNode = node as? ModelNode
                                    showMenuModel = true
                                }
                            }
                        },
                        onLongPress = { _, node ->
                            if (node is ModelNode) {
                                node.isPositionEditable = false
                            }
                            Log.d("=POSITION=", "${node!!.worldQuaternion.xyz}")
                        }
                    )
                ) {
                    arSceneView = this
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .wrapContentSize(),
                    onClick = {
                        showBottomSheet = true
                        scope.launch { bottomSelectionSheetState.show() }
                    },
                    colors = ButtonDefaults.buttonColors(
                        colorResource(R.color.green),
                        colorResource(R.color.white)
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = CircleShape
                ) {
                    Icon(
                        SearchIcon,
                        contentDescription = "Поиск",
                        tint = Color.White
                    )
                }

                if (showCrosshair) {
//                    Box(
//                        modifier = Modifier
//                            .size(24.dp)
//                            .background(Color.Red, shape = CircleShape)
//                            .align(Alignment.Center)
//                    )

                    Icon(
                        painter = painterResource(R.drawable.crosshair),
                        contentDescription = "crosshair",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                )
                {
                    Button(
                        modifier = Modifier
                            .size(50.dp) ,
                        onClick = {
                            planeRenderer = !planeRenderer
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.green),
                            containerColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            if (planeRenderer) Plane_off else Plane_on,
                            contentDescription = "plane render ON or OFF"
                        )
                    }

                    Button(
                        modifier = Modifier
                            .size(50.dp) ,
                        onClick = {
                            childNodes.clear()
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = colorResource(R.color.green),
                            containerColor = Color.White
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            Trashcan,
                            contentDescription = "Удаление всех элементов",
                            tint = colorResource(R.color.green)
                        )
                    }
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    onClick = {
                        planeRenderer = !planeRenderer

                        captureHelper.takePhoto(arSceneView!!, context)

                        planeRenderer = !planeRenderer
                    },
                    shape = RoundedCornerShape(percent = 40)
                ) {
                    Text(text = "Фото")
                }

                if (showMenuModel) {
                    selectedNode!!.isRotationEditable = true
                    Column (
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(60.dp, 10.dp)
                                .background(
                                    color = colorResource(R.color.green),
                                    shape = RoundedCornerShape(16)
                                )
                                .wrapContentHeight()
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    IconButton(
                                        modifier = Modifier
                                            .size(100.dp),
                                        onClick = {
                                            selectedNode?.let {
                                                val currentQuaternion = it.quaternion
                                                val rotationQuaternion =
                                                    dev.romainguy.kotlin.math.Quaternion
                                                        .fromAxisAngle(
                                                            Float3(0f, 1f, 0f), -90f
                                                        )
                                                it.quaternion = currentQuaternion * rotationQuaternion
                                            }
                                        },
                                    ) {
                                        Icon(
                                            Arrow_left,
                                            modifier = Modifier.size(37.dp),
                                            contentDescription = "turn left model",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(
                                        modifier = Modifier
                                            .size(100.dp),
                                        onClick = {
                                            selectedNode?.let {
                                                val currentQuaternion = it.quaternion
                                                val rotationQuaternion =
                                                    dev.romainguy.kotlin.math.Quaternion
                                                        .fromAxisAngle(
                                                            Float3(0f, 1f, 0f), 90f
                                                        )
                                                it.quaternion = currentQuaternion * rotationQuaternion
                                            }
                                        },
                                    ) {
                                        Icon(
                                            Arrow_right,
                                            modifier = Modifier.size(37.dp),
                                            contentDescription = "turn right model",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(
                                        modifier = Modifier
                                            .size(100.dp),
                                        onClick = {
                                            duplicateNode = true
                                            showCrosshair = true
                                        },
                                    ) {
                                        Icon(
                                            Duplicate,
                                            modifier = Modifier.size(37.dp),
                                            contentDescription = "turn right model",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(
                                        modifier = Modifier
                                            .size(100.dp),
                                        onClick = {
                                            selectedNode?.destroy()
                                            showMenuModel = false
                                        },
                                    ) {
                                        Icon(
                                            Trashcan,
                                            modifier = Modifier.size(37.dp),
                                            contentDescription = "delete model",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                        Slider(
                            modifier = Modifier
                                .padding(vertical = 92.dp),
                            value = y_val,
                            valueRange = -180f..180f,
                            steps = 360,
                            onValueChange = {value ->
                                y_val = value
                                selectedNode?.let { node ->
                                    val rotationQuaternion =
                                        dev.romainguy.kotlin.math.Quaternion
                                            .fromAxisAngle(
                                                Float3(0f, 1f, 0f), y_val
                                            )
                                    node.quaternion = rotationQuaternion
                                }
                            },
                            thumb = {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            colorResource(R.color.green),
                                            shape = CircleShape
                                        )
                                )
                            },
                            colors = SliderDefaults.colors(thumbColor = colorResource(R.color.green))
                        )
                    }
                } else {
                    selectedNode?.isRotationEditable = false
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                        sheetState = bottomSelectionSheetState
                    ) {
                        CatalogScreen(catalogItems!!) { modelPath ->
                            selectedModel = modelPath
                            showCrosshair = true
                        }
                    }
                }
            }
        }
    }
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

fun getScreenSize(context: Context): Pair<Int, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels to displayMetrics.heightPixels
}

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

@Composable
fun CatalogItemCard(item: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.5.dp)
            .clickable(onClick = onClick)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            val bitmapState = getImageFromAssets(item.images[0])
            if (null != bitmapState) {
                val bitmap = bitmapState.asImageBitmap()
                Image(
                    bitmap,
                    contentDescription = "example photo",
                    modifier = Modifier.size(40.dp, 40.dp)
                )
            }
            Text(item.name)
            Icon(Icons.Filled.FavoriteBorder, null)
        }
    }

}

@Preview
@Composable
fun CatalogItemCardPreview(){
    CatalogItemCard(DataMobile().listProduct!![0], onClick = {})
}

@Composable
fun CatalogScreen(items: List<Product>, onItemSelected: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items) { item ->
            CatalogItemCard(item = item) {
                onItemSelected(item.model)
            }
        }
    }
}

class CaptureHelper {
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
            if (copyResult === PixelCopy.SUCCESS) {
                saveImageBitmapToGallery(context, bitmap)
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))

    }
}