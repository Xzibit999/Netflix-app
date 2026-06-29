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
import com.squareup.picasso.Picasso

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
        supportActionBar?.title = "" // Clear default title to show NETFLIX logo-style text

        val ivFeatured = findViewById<android.widget.ImageView>(R.id.ivFeatured)
        Picasso.get()
            .load("https://m.media-amazon.com/images/M/MV5BMDZkYmVhNjMtNWU4MC00MDQxLWE3MjYtZGJlZDE0NzMxYTQyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg")
            .into(ivFeatured)

        setupRecyclerViews()
        
        // FORCE RESET: Clear database and re-seed with movies once
        repo.clearAllProducts { 
            seedProducts()
        }

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
                val hasOldData = products.any { 
                    val name = it.name.lowercase()
                    name.contains("car") || name.contains("bmw") || name.contains("tesla") || name.contains("audi")
                }
                
                if (products.isEmpty() || hasOldData) {
                    repo.clearAllProducts { success ->
                        if (success) seedProducts()
                    }
                } else {
                    popularAdapter.updateData(products.filter { it.category == "Popular" })
                    trendingAdapter.updateData(products.filter { it.category == "Trending" })
                }
            } else {
                Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun seedProducts() {
        val repo = ProductRepoImpl()
        
        val products = listOf(
            ProductModel(
                name = "Stranger Things",
                description = "When a young boy vanishes, a small town uncovers a mystery involving secret experiments.",
                price = 9.5,
                category = "Trending",
                imageUrl = "https://images.tmdb.org/t/p/w500/49WfT1UuUpWShwinB296CztpZgB.jpg"
            ),
            ProductModel(
                name = "Squid Game",
                description = "Hundreds of cash-strapped players accept a strange invitation to compete in children's games.",
                price = 8.1,
                category = "Trending",
                imageUrl = "https://images.tmdb.org/t/p/w500/d5NXSklZfsDfUFEyc6u46u6u97v.jpg"
            ),
            ProductModel(
                name = "Wednesday",
                description = "Smart, sarcastic and a little dead inside, Wednesday Addams investigates a murder spree.",
                price = 8.2,
                category = "Trending",
                imageUrl = "https://images.tmdb.org/t/p/w500/9PFonB9tqdt6oqdKFpPSAnfkvqh.jpg"
            ),
            ProductModel(
                name = "The Witcher",
                description = "Geralt of Rivia, a mutated monster-hunter for hire, journeys toward his destiny.",
                price = 8.1,
                category = "Trending",
                imageUrl = "https://images.tmdb.org/t/p/w500/u7S7H68Q9YpYv077O0v2mYp9Swa.jpg"
            ),
            ProductModel(
                name = "Money Heist",
                description = "Eight thieves take hostages and lock themselves in the Royal Mint of Spain.",
                price = 8.2,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/reEMJA1uzpG3SZ0KGv7Gz9XpTid.jpg"
            ),
            ProductModel(
                name = "Dark",
                description = "A family saga with a supernatural twist, set in a German town.",
                price = 8.8,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/ap8Y9S7znS7R7m77o9679vC3m9Y.jpg"
            ),
            ProductModel(
                name = "The Crown",
                description = "Follows the political rivalries and romance of Queen Elizabeth II's reign.",
                price = 8.6,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/70A96B99YpYv077O0v2mYp9Swa.jpg"
            ),
            ProductModel(
                name = "Peaky Blinders",
                description = "A gangster family epic set in 1900s England.",
                price = 8.8,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/v9qxr9O0S0R5y7W9fV2Y8Y0Y8Y0Y.jpg"
            ),
            ProductModel(
                name = "Breaking Bad",
                description = "A high school chemistry teacher turned meth kingpin.",
                price = 9.5,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/ztkUQfjwhC19DbpgghqbDnQh9Ep.jpg"
            ),
            ProductModel(
                name = "Inception",
                description = "A thief who steals corporate secrets through the use of dream-sharing technology.",
                price = 8.8,
                category = "Popular",
                imageUrl = "https://images.tmdb.org/t/p/w500/edv5CZvnc0a9YFYqRzdILOGFiqa.jpg"
            )
        )

        products.forEach { product ->
            repo.addProduct(product) { success, _ ->
                if (success) {
                    // Movie added successfully
                }
            }
        }
        Toast.makeText(this, "Seeding Netflix content to Firebase...", Toast.LENGTH_SHORT).show()
    }
}
