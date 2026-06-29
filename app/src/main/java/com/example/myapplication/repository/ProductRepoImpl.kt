package com.example.myapplication.repository

import com.example.myapplication.model.ProductModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductRepoImpl : ProductRepo {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val productsRef = database.getReference("products")

    override fun addProduct(productModel: ProductModel, callback: (Boolean, String?) -> Unit) {
        val id = productsRef.push().key ?: ""
        val newProduct = productModel.copy(id = id)
        productsRef.child(id).setValue(newProduct)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Product added successfully")
                } else {
                    callback(false, task.exception?.message ?: "Failed to add product")
                }
            }
    }

    override fun updateProduct(id: String, data: Map<String, Any?>, callback: (Boolean, String?) -> Unit) {
        productsRef.child(id).updateChildren(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Product updated successfully")
                } else {
                    callback(false, task.exception?.message ?: "Update failed")
                }
            }
    }

    override fun deleteProduct(id: String, callback: (Boolean, String?) -> Unit) {
        productsRef.child(id).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, "Product deleted successfully")
                } else {
                    callback(false, task.exception?.message ?: "Deletion failed")
                }
            }
    }

    override fun getProductById(id: String, callback: (ProductModel?, String?) -> Unit) {
        productsRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = snapshot.getValue(ProductModel::class.java)
                callback(product, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }

    override fun getAllProducts(callback: (List<ProductModel>?, String?) -> Unit) {
        productsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<ProductModel>()
                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(ProductModel::class.java)
                    if (product != null) products.add(product)
                }
                callback(products, null)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, error.message)
            }
        })
    }

    fun clearAllProducts(callback: (Boolean) -> Unit) {
        productsRef.removeValue().addOnCompleteListener { callback(it.isSuccessful) }
    }
}
