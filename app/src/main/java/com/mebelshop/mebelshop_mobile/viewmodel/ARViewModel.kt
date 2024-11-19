package com.mebelshop.mebelshop_mobile.viewmodel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.filament.Engine
import com.google.ar.core.Frame
import com.mebelshop.mebelshop_mobile.MainActivity
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

    val centerX: Float
    val centerY: Float

    init {
        val (x, y) = getScreenCenter(context)
        centerX = x
        centerY = y
    }

    private lateinit var view: ARSceneView
    private var context = context

    fun setSurfaceView(view: ARSceneView) {
        this.view = view
    }

    fun makePhoto(view: ARSceneView) {
        arModel.makeBitmapFromScreenshot(view, context)
    }

    fun onSingleTapConfirmed(
        childNodes: SnapshotStateList<Node>,
        engine: Engine,
        modelLoader: ModelLoader,
        materialLoader: MaterialLoader,
        modelInstances: MutableList<ModelInstance>,
        frame: Frame,
        selectedModel: String?,
        selectedNode: ModelNode?
    ) {
        Log.d("=SELECTED_MODEL=", "selected_model: $selectedModel")
        if (arConfig.isModelDuplicate) {
            handleDuplicationNode(childNodes, engine, modelLoader, materialLoader, modelInstances,
                frame, selectedNode)
        } else if (arConfig.isModelSelected) {
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
        Log.d("=SELECTED_MODEL=", "selected_model: $selectedModel")
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