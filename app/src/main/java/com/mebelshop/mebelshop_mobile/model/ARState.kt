package com.mebelshop.mebelshop_mobile.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.View
import com.google.ar.core.Session
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.camera.ARCameraStream
import io.github.sceneview.ar.rememberARCameraStream
import io.github.sceneview.collision.CollisionSystem
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import java.util.Objects

class ARState {
//    private lateinit var childNodes: SnapshotStateList<Node>
//    private lateinit var renderer: Renderer
//    private lateinit var scene: Scene
//    private lateinit var collisionSystem: CollisionSystem
//    private lateinit var cameraStream: ARCameraStream
//    private lateinit var modelInstances: ModelInstance
//
//    fun initChildNodes(childNodes: SnapshotStateList<Node>) {
//        this.childNodes = childNodes
//    }
//    fun initRenderer(renderer: Renderer){
//        this.renderer = renderer
//    }
//    fun initScene(scene: Scene){
//        this.scene = scene
//    }
//    fun initCollisionSystem(collisionSystem: CollisionSystem){
//        this.collisionSystem = collisionSystem
//    }
//    fun initCameraStream(cameraStream: ARCameraStream) {
//        this.cameraStream = cameraStream
//    }
//    fun initModelInstances(modelInstances: ModelInstance){
//        this.modelInstances = modelInstances
//    }

    private lateinit var session: Session

    fun initSession(session: Session) {
        this.session = session
    }

    fun getSession(): Session {
        return session
    }
}