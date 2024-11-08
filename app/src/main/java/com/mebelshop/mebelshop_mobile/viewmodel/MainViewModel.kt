package com.mebelshop.mebelshop_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.mebelshop.mebelshop_mobile.CategoryProduct
import com.mebelshop.mebelshop_mobile.DataMobile
import com.mebelshop.mebelshop_mobile.Product
import com.mebelshop.mebelshop_mobile.model.MainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class MainViewModel(private val model: MainModel) : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>>(emptyList())

    private val _filteredProducts = MutableStateFlow<List<Product>>(emptyList())
    val filteredProducts: StateFlow<List<Product>> = _filteredProducts

    init {
        getProducts()
    }

    fun getProducts() {
        _productList.value = DataMobile().listProduct ?: emptyList()
        _filteredProducts.value = _productList.value
    }

    fun filterProducts(categoryProduct: CategoryProduct) {
        _filteredProducts.value = _productList.value.filter { product ->
            if (categoryProduct == CategoryProduct("Все категории", image = "image/all.png")) {
                true
            } else {
                categoryProduct in product.categoryProduct
            }
        }
    }
    fun filterProductsSearch(name: String){
        _filteredProducts.value = _productList.value.filter { product ->
            if (name == "") {
                true
            } else {
                name == product.name
            }
        }
    }
}



