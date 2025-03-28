package com.mebelshop.mebelshop_mobile.viewmodel

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.filament.Engine
import com.google.ar.core.Frame
import com.mebelshop.mebelshop_mobile.ar.arConfig
import com.mebelshop.mebelshop_mobile.model.ARModel
import com.mebelshop.mebelshop_mobile.model.Product
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node


class ARViewModel(context: Context): ViewModel() {
    private val arModel: ARModel = ARModel()
    val listProduct: LiveData<List<Product>> get() = ARModel().listProduct

    val centerX: Float
    val centerY: Float

    init {
        val (x, y) = getScreenCenter(context)
        centerX = x
        centerY = y
    }

    private val _isShowBottomSheet = MutableLiveData<Boolean>(false)
    val isShowBottomSheet: LiveData<Boolean> get() = _isShowBottomSheet

    private lateinit var view: ARSceneView
    private var context = context

    fun setSurfaceView(view: ARSceneView) {
        this.view = view
    }

    fun loadListProduct() {
        ARModel().refreshListProduct()
    }

    fun makePhoto() {
        arModel.makeBitmapFromScreenshot(view, context)
    }

    fun onSingleTapConfirmed(
        childNodes: SnapshotStateList<Node>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        frame: Frame,
        createModelFlag: Boolean,
        selectedModel: String?,
        duplicateNode: Boolean,
        selectedNode: ModelNode?
    ) {
        if (duplicateNode) {
            handleDuplicationNode(childNodes, engine, modelLoader, materialLoader, modelInstances,
                frame, selectedNode)
        } else if (createModelFlag) {
            handleNewNode(childNodes, engine, modelLoader, materialLoader, modelInstances,
                frame, selectedModel)
        }

    }

    private fun handleNewNode(
        childNodes: SnapshotStateList<Node>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        frame: Frame?,
        selectedModel: String?
    ) {
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
                    childNodes += arModel.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstances = modelInstances,
                        anchor = anchor,
                        modelPath = modelPath
                    )

                }
        }
    }

    private fun handleDuplicationNode(
        childNodes: SnapshotStateList<Node>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        frame: Frame?,
        selectedNode: ModelNode?
    ) {
        selectedNode?.let { modelNode ->
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
                    childNodes += arModel.createAnchorNode(
                        engine = engine,
                        modelLoader = modelLoader,
                        materialLoader = materialLoader,
                        modelInstances = modelInstances,
                        anchor = anchor,
                        model = selectedNode.model
                    )
                }
        }
    }

    fun nodeToModelNode (node: Node?): ModelNode? {
        return if (node != null) {
            arConfig.isShowMenuModel = true
            node as ModelNode
        } else{
            null
        }
    }

    private fun getScreenCenter(context: Context): Pair<Float, Float> {
        val displayMetrics = context.resources.displayMetrics
        val centerX = displayMetrics.widthPixels / 2f
        val centerY = displayMetrics.heightPixels / 2f
        return Pair(centerX, centerY)
    }
}