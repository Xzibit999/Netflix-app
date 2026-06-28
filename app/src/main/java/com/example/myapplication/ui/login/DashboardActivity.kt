package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Toast
import com.example.myapplication.model.ProductModel
import com.example.myapplication.repository.ProductRepoImpl
import com.example.myapplication.R

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ProductAdapter

import com.example.myapplication.repository.UserRepoImpl

class DashboardActivity : AppCompatActivity() {

    private val repo = ProductRepoImpl()
    private val userRepo = UserRepoImpl()
    private lateinit var popularAdapter: ProductAdapter
    private lateinit var trendingAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Car Showcase"

        setupRecyclerViews()
        loadProducts()

        val signOutBtn = findViewById<Button>(R.id.btnSignOut)
        signOutBtn?.setOnClickListener {
            userRepo.logout()
            Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerViews() {
        val rvPopular = findViewById<RecyclerView>(R.id.rvPopular)
        val rvTrending = findViewById<RecyclerView>(R.id.rvTrending)

        popularAdapter = ProductAdapter(emptyList())
        trendingAdapter = ProductAdapter(emptyList())

        rvPopular.adapter = popularAdapter
        rvTrending.adapter = trendingAdapter
    }

    private fun loadProducts() {
        repo.getAllProducts { products, message ->
            if (products != null) {
                if (products.isEmpty()) {
                    seedProducts()
                } else {
                    // Split products into categories if you want, or just show them
                    popularAdapter.updateData(products.take(5))
                    trendingAdapter.updateData(products.takeLast(5))
                }
            } else {
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun seedProducts() {
        val repo = ProductRepoImpl()
        val products = listOf(
            ProductModel(name = "Tesla Model S", description = "Electric Luxury Sedan", price = 89990.0, category = "Electric"),
            ProductModel(name = "Ford Mustang", description = "Classic American Muscle Car", price = 55000.0, category = "Muscle"),
            ProductModel(name = "BMW M4", description = "High-Performance Coupe", price = 78000.0, category = "Performance"),
            ProductModel(name = "Audi R8", description = "Mid-Engine Sports Car", price = 158000.0, category = "Exotic"),
            ProductModel(name = "Porsche 911", description = "Iconic Rear-Engine Sports Car", price = 114000.0, category = "Sports"),
            ProductModel(name = "Toyota Supra", description = "Legendary Japanese Sports Car", price = 52000.0, category = "Sports"),
            ProductModel(name = "Honda Civic Type R", description = "Ultimate Hot Hatch", price = 44000.0, category = "Hatchback"),
            ProductModel(name = "Mercedes-Benz G-Wagon", description = "Luxury Off-Road SUV", price = 139000.0, category = "SUV"),
            ProductModel(name = "Lamborghini Huracan", description = "V10 Supercar Performance", price = 210000.0, category = "Supercar"),
            ProductModel(name = "Ferrari F8", description = "Italian Exotic Masterpiece", price = 280000.0, category = "Supercar")
        )

        products.forEach { product ->
            repo.addProduct(product) { success, _ ->
                if (success) {
                    // Car added successfully
                }
            }
        }
        Toast.makeText(this, "Seeding 10 cars to Firebase...", Toast.LENGTH_SHORT).show()
    }
}
