package com.example.myapplication.repository

import com.example.myapplication.model.ProductModel

interface ProductRepo {
    fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit)
    fun updateProduct(id: String, data: Map<String, Any?>, callback: (Boolean, String?) -> Unit)
    fun deleteProduct(id: String, callback: (Boolean, String?) -> Unit)
    fun getProductById(id: String, callback: (ProductModel?, String?) -> Unit)
    fun getAllProducts(callback: (List<ProductModel>?, String?) -> Unit)
}
