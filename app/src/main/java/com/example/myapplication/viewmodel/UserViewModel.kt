package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.UserModel
import com.example.myapplication.repository.UserRepo
import com.google.firebase.auth.FirebaseUser

class UserViewModel(private val repo: UserRepo) : ViewModel() {

    private val _userData = MutableLiveData<UserModel?>()
    val userData: LiveData<UserModel?> get() = _userData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun login(email: String, pass: String, callback: (Boolean, String?) -> Unit) {
        _loading.value = true
        repo.login(email, pass) { success, message ->
            _loading.value = false
            if (success) {
                _error.value = null
            } else {
                _error.value = message
            }
            callback(success, message)
        }
    }

    fun register(userModel: UserModel, pass: String, callback: (Boolean, String?) -> Unit) {
        _loading.value = true
        repo.register(userModel, pass) { success, message ->
            _loading.value = false
            if (!success) {
                _error.value = message
            }
            callback(success, message)
        }
    }

    fun forgetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        repo.forgetPassword(email, callback)
    }

    fun getUserById(id: String) {
        _loading.value = true
        repo.getUserById(id) { user, message ->
            _loading.value = false
            if (user != null) {
                _userData.value = user
            } else {
                _error.value = message
            }
        }
    }

    fun logout() {
        repo.logout()
        _userData.value = null
    }

    fun getCurrentUser(): FirebaseUser? {
        return repo.getCurrentUser()
    }
}
