package com.mebelshop.mebelshop_mobile

data class Shop(
    val name_shop: String,
    val image: String,
    val description: String,
    val name_organization: String,
    val INN: Int,
){
    var products: MutableList<Product> = mutableListOf()

    fun setProduct(product: Product){
        products.add(product)
    }
}