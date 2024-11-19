package com.mebelshop.mebelshop_mobile.ar

import Arrow_left
import Arrow_right
import SearchIcon
import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.ar.core.TrackingFailureReason
import com.mebelshop.mebelshop_mobile.ARConfig
import com.mebelshop.mebelshop_mobile.R
import com.mebelshop.mebelshop_mobile.model.Product
import com.mebelshop.mebelshop_mobile.model.SelectedModels
import com.mebelshop.mebelshop_mobile.ui.theme.AppTheme
import com.mebelshop.mebelshop_mobile.viewmodel.ARViewModel
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.ar.rememberARCameraStream
import io.github.sceneview.model.ModelInstance
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
import kotlinx.coroutines.launch

val arConfig = ARConfig()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AR(selectedPathToModel: String? = null, onBack: () -> Unit, navController: NavController) {
    val viewModel = ARViewModel(LocalContext.current)

    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraNode = rememberARCameraNode(engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine)
    val renderer = rememberRenderer(engine)
    val scene = rememberScene(engine)
    val ARView = remember{
        mutableStateOf<ARSceneView?>(null)
    }
    val collisionSystem = rememberCollisionSystem(view)
    val cameraStream = rememberARCameraStream(materialLoader)
    val modelInstances = remember { mutableListOf<ModelInstance>() }
    var frame by remember { mutableStateOf<Frame?>(null) }
    var selectedNode by remember { mutableStateOf<ModelNode?>(null) }
    var planeRenderer by remember { mutableStateOf(true) }

    val bottomSelectionSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var trackingFailureReason by remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }

    var selectedModel by remember { mutableStateOf<String?>(null) }

    if (selectedPathToModel != null) {
        selectedModel = selectedPathToModel
        arConfig.isModelSelected = true
        arConfig.isShowCrosshair = true
    }

    var y_val by remember { mutableStateOf(0f) }

    BackHandler {
        navController.navigate("main_screen")
    }

    AppTheme {
        Scaffold { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
            ) {
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
                    onTrackingFailureChanged = {
                        trackingFailureReason = it
                    },
                    onSessionUpdated = { _, updatedFrame ->
                        frame = updatedFrame
                    },
                    onGestureListener = rememberOnGestureListener(
                        onSingleTapConfirmed = { _, node ->
                            if (arConfig.isModelSelected || arConfig.isModelDuplicate) {
                                viewModel.onSingleTapConfirmed(
                                    childNodes = childNodes,
                                    engine = engine,
                                    modelLoader = modelLoader,
                                    materialLoader = materialLoader,
                                    modelInstances = modelInstances,
                                    frame = frame!!,
                                    selectedModel = selectedModel,
                                    selectedNode = selectedNode
                                )
                                arConfig.isModelDuplicate = false
                                arConfig.isModelSelected = false
                                selectedModel = null
                            }
                            selectedNode = viewModel.nodeToModelNode(node)
                            if (selectedNode != null) {
                                arConfig.isShowMenuModel = false
                            }
                        }
                    )
                ) {
                    ARView.value = this
                    viewModel.setSurfaceView(ARView.value!!)
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .wrapContentSize(),
                    onClick = {
                        arConfig.isShowBottomSheet = true
                        scope.launch {
                            bottomSelectionSheetState.show()
                        }
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
                if (arConfig.isShowCrosshair) {
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
                            if (planeRenderer) painterResource(R.drawable.plane_off)
                            else painterResource(R.drawable.plane_on),
                            modifier = Modifier.size(30.dp),
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
                            painterResource(R.drawable.trashcan),
                            contentDescription = "Удаление всех элементов",
                            tint = colorResource(R.color.green)
                        )
                    }

                    if (arConfig.isShowMenuModel) {
                        selectedNode!!.isRotationEditable = true
                        Column (
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp, 5.dp)
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
                                                .size(120.dp),
                                            onClick = {
                                                arConfig.isModelDuplicate = true
                                                arConfig.isShowCrosshair = true
                                            },
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.duplicate),
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
                                                arConfig.isShowMenuModel = false
                                            },
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.trashcan),
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
                                    .padding(vertical = 85.dp),
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
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomStart),
                    onClick = {
                        planeRenderer = false

                        viewModel.makePhoto(ARView.value!!)

                        planeRenderer = true
                    },
                    shape = RoundedCornerShape(percent = 40)
                ) {
                    Text(text = "Фото")
                }
            }
            if (bottomSelectionSheetState.isVisible) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch {
                            bottomSelectionSheetState.hide()
                        }
                    },
                    sheetState = bottomSelectionSheetState
                ) {
                    CatalogScreen(SelectedModels.getModels()) { modelPath ->
                        selectedModel = modelPath
                        arConfig.isShowCrosshair = true
                    }
                }
            }
        }
    }
}
@Composable
fun CatalogItemCard(item: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.name)
        }
    }
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