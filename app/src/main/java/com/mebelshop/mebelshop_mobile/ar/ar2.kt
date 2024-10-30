package com.mebelshop.mebelshop_mobile.ar

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display.Mode
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.filament.Engine
import com.google.android.filament.Texture
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import com.mebelshop.mebelshop_mobile.model.CatalogItem
import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.quaternion
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.isFocusable
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.collision.HitResult
import io.github.sceneview.collision.Quaternion
import io.github.sceneview.collision.Vector3
import io.github.sceneview.gesture.MoveGestureDetector
import io.github.sceneview.gesture.RotateGestureDetector
import io.github.sceneview.gesture.ScaleGestureDetector
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.math.Position
import io.github.sceneview.model.Model
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import io.github.sceneview.utils.worldToScreen
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.nio.ByteBuffer

private const val kModelFile1 = "models/example_model_1.glb"
private const val kModelFile2 = "models/example_model_2.glb"
private const val kModelFile3 = "models/example_model_3.glb"
private const val kModelFile4 = "models/example_model_4.glb"
private const val kModelFile5 = "models/example_model_5.glb"


private const val kMaxModelInstances = 10


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AR2() {

    val context = LocalContext.current
    val (screenWidth, screenHeight) = getScreenSize(context)

    val centerX = screenWidth / 2f
    val centerY = screenHeight / 2f

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val engine = rememberEngine()
        val modelLoader = rememberModelLoader(engine)
        val materialLoader = rememberMaterialLoader(engine)
        val cameraNode = rememberARCameraNode(engine)
        val childNodes = rememberNodes()
        val view = rememberView(engine)
        val collisionSystem = rememberCollisionSystem(view)

        var planeRenderer by remember { mutableStateOf(true) }

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
            listOf(
                CatalogItem(1, "куб", kModelFile1),
                CatalogItem(2, "тц", kModelFile2),
                CatalogItem(3, "стеллаж", kModelFile3),
                CatalogItem(4, "тумба", kModelFile4),
                CatalogItem(5, "лампа", kModelFile5),
                )
        }
        var selectedModel by remember { mutableStateOf<String?>(null) }

        var rotationAngle by remember { mutableStateOf(0f) }
        var initialRotation by remember { mutableStateOf(dev.romainguy.kotlin.math.Quaternion()) }
        var selectedNode by remember { mutableStateOf<ModelNode?>(null) }

        var longPress by remember { mutableStateOf(false) }
        var showMenuModel by remember { mutableStateOf(false) }
        var modelPosition by remember { mutableStateOf(Float3(0f, 0f, 0f)) }
        var y_val by remember { mutableStateOf(0f) }
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                        scope.launch { bottomSelectionSheetState.show() }
                    },
                    containerColor = Color(0xFF4CAF50),
                    shape = RoundedCornerShape(300.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = Color.White
                    )
                }
            }
        ) { contentPadding ->

            Box(
                modifier = Modifier
                    .padding(contentPadding)
            ) {
                var initialRotation by remember { mutableStateOf(Float3(0f, 0f, 0f)) }
                var initialPosition by remember { mutableStateOf(Float3(0f, 0f, 0f)) }

                ARScene(
                    modifier = Modifier.fillMaxSize(),
                    childNodes = childNodes,
                    engine = engine,
                    view = view,
                    modelLoader = modelLoader,
                    collisionSystem = collisionSystem,
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
                        }
                        )
                )
                if (showCrosshair) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color.Red, shape = CircleShape)
                            .align(Alignment.Center)
                    )
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    onClick = {
                        planeRenderer = !planeRenderer
                    },
                    shape = RoundedCornerShape(percent = 40)
                ) {
                    Text(text = if (planeRenderer) "Сетка выкл" else "Сетка вкл")
                }

                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    onClick = {

                    },
                    shape = RoundedCornerShape(percent = 40)
                ) {
                    Text(text = "Фото")
                }

                if (showMenuModel) {
                    selectedNode!!.isRotationEditable = true
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .background(Color.Gray.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(8.dp))
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Button(
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

                                    shape = RoundedCornerShape(percent = 18)
                                ) {
                                    Text("Поворот\nвлево", textAlign = TextAlign.Center)
                                }
                                Button(
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

                                    shape = RoundedCornerShape(percent = 18)
                                ) {
                                    Text("Поворот\nвправо", textAlign = TextAlign.Center)
                                }
                                Button(
                                    onClick = {
                                        selectedNode?.let { it.destroy() }
                                        showMenuModel = false
                                    },

                                    shape = RoundedCornerShape(percent = 18)
                                ) {
                                    Text("Удалить", textAlign = TextAlign.Center)
                                }
                            }
                            Slider(
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
                                }
                            )
                        }
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
                        CatalogScreen(catalogItems) { modelPath ->
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

fun getScreenSize(context: Context): Pair<Int, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels to displayMetrics.heightPixels
}
