package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.repository.ProductRepo
import com.example.myapplication.repository.UserRepo

class ViewModelFactory(private val repo: Any) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repo as UserRepo) as T
        } else if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repo as ProductRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
