package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.ProductModel
import com.example.myapplication.repository.ProductRepo

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    private val _allProducts = MutableLiveData<List<ProductModel>?>()
    val allProducts: LiveData<List<ProductModel>?> get() = _allProducts

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {
        _loading.value = true
        repo.addProduct(productModel) { success, message ->
            _loading.value = false
            callback(success, message)
        }
    }

    fun fetchAllProducts() {
        _loading.value = true
        repo.getAllProducts { products, message ->
            _loading.value = false
            if (products != null) {
                _allProducts.value = products
            } else {
                _error.value = message
            }
        }
    }

    fun updateProduct(id: String, data: Map<String, Any?>, callback: (Boolean, String?) -> Unit) {
        repo.updateProduct(id, data, callback)
    }

    fun deleteProduct(id: String, callback: (Boolean, String?) -> Unit) {
        repo.deleteProduct(id, callback)
    }
}
