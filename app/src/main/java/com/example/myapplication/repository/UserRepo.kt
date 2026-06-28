package com.example.myapplication.repository

import com.example.myapplication.model.UserModel
import com.google.firebase.auth.FirebaseUser

interface UserRepo {
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit)
    fun register(userModel: UserModel, password: String, callback: (Boolean, String?) -> Unit)
    fun forgetPassword(email: String, callback: (Boolean, String?) -> Unit)
    fun editProfile(userModel: UserModel, callback: (Boolean, String?) -> Unit)
    fun updateUser(id: String, data: Map<String, Any?>, callback: (Boolean, String?) -> Unit)
    fun deleteUser(id: String, callback: (Boolean, String?) -> Unit)
    fun getUserById(id: String, callback: (UserModel?, String?) -> Unit)
    fun getAllUsers(callback: (List<UserModel>?, String?) -> Unit)
    fun logout()
    fun getCurrentUser(): FirebaseUser?
}
