package com.example.myapplication.repository

import com.example.myapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepoImpl : UserRepo {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Login Successful")
                } else {
                    callback(false, task.exception?.message ?: "Login Failed")
                }
            }
    }

    override fun register(userModel: UserModel, password: String, callback: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(userModel.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val updatedUser = userModel.copy(id = userId)
                    usersRef.child(userId).setValue(updatedUser)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                callback(true, "Registration Successful")
                            } else {
                                callback(false, dbTask.exception?.message ?: "Database Error")
                            }
                        }
                } else {
                    callback(false, task.exception?.message ?: "Registration Failed")
                }
            }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Reset link sent to your email")
                } else {
                    callback(false, task.exception?.message ?: "Failed to send reset link")
                }
            }
    }

    override fun editProfile(userModel: UserModel, callback: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            usersRef.child(userId).setValue(userModel)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true, "Profile Updated")
                    } else {
                        callback(false, task.exception?.message ?: "Update Failed")
                    }
                }
        } else {
            callback(false, "User not logged in")
        }
    }

    override fun updateUser(id: String, data: Map<String, Any?>, callback: (Boolean, String?) -> Unit) {
        usersRef.child(id).updateChildren(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "User Updated")
                } else {
                    callback(false, task.exception?.message ?: "Update Failed")
                }
            }
    }

    override fun deleteUser(id: String, callback: (Boolean, String?) -> Unit) {
        usersRef.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "User Deleted")
                } else {
                    callback(false, task.exception?.message ?: "Deletion Failed")
                }
            }
    }

    override fun getUserById(id: String, callback: (UserModel?, String?) -> Unit) {
        usersRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                callback(user, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }

    override fun getAllUsers(callback: (List<UserModel>?, String?) -> Unit) {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    if (user != null) users.add(user)
                }
                callback(users, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
