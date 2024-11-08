package com.mebelshop.mebelshop_mobile.model


object SelectedModels {
    private val modelsList = mutableListOf<Product>()

    fun addModel(path: Product){
        modelsList.add(path)
    }

    fun dropModel(path: Product){
        modelsList.remove(path)
    }

    fun getModels(): List<Product> {
        return modelsList
    }
}