package com.mebelshop.mebelshop_mobile

data class Product(
    val name: String,
    val price: Int,
    val oldPrice : Int?,
    val shop: Shop,
    val categoryProduct: List<CategoryProduct>,
    val images: List<String>,
    val description: String,
    val attributes: Map<String, String>,
    val model: String
){
    init {
        shop.setProduct(this)
        for (i in categoryProduct){
            i.setProduct(this)
        }
    }
}
