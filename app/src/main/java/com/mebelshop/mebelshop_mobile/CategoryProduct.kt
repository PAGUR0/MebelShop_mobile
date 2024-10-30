package com.mebelshop.mebelshop_mobile

data class CategoryProduct(
    val name: String,
    val image: String,

){
    var products: MutableList<Product> = mutableListOf()

    fun setProduct(product: Product){
        products.add(product)
    }
}