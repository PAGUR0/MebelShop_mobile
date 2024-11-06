package com.mebelshop.mebelshop_mobile.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mebelshop.mebelshop_mobile.model.ARModel
import io.github.sceneview.ar.ARSceneView

class ARViewModel(val model: ARModel) :ViewModel() {

    fun takePhoto(
        arSceneView: ARSceneView,
        context: Context
    ){
        model.takePhoto(arSceneView, context)
    }
}