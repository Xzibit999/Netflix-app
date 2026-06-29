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
                description = "Mystery, Sci-Fi",
                price = 0.0,
                category = "Trending",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BMDZkYmVhNjMtNWU4MC00MDQxLWE3MjYtZGJlZDE0NzMxYTQyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg"
            ),
            ProductModel(
                name = "The Witcher",
                description = "Action, Adventure",
                price = 0.0,
                category = "Trending",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BOGE4MmVjMDgtMzIzYy00NjEwLWJlODMtMDI1MGY2ZDkwMzE2XkEyXkFqcGdeQXVyMzY0MTE3NzU@._V1_.jpg"
            ),
            ProductModel(
                name = "Wednesday",
                description = "Comedy, Crime",
                price = 0.0,
                category = "Trending",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BM2MyNwYyM2UtNjUyZi00NmY0LWEwZDYtOWVmDFjODcwYTRiXkEyXkFqcGdeQXVyMTMzNzIyMDc0._V1_.jpg"
            ),
            ProductModel(
                name = "Money Heist",
                description = "Action, Crime",
                price = 0.0,
                category = "Popular",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BODI0ZTljYTMtODQ1NC00NmI0LTk1YWUtN2FlNDM1MDExMDUzXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"
            ),
            ProductModel(
                name = "Dark",
                description = "Crime, Drama",
                price = 0.0,
                category = "Popular",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BOTk2NzUyOTctZDdlMS00MDJlLTgzNTEtNzQzYjFhNjA0YjBjXkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg"
            ),
            ProductModel(
                name = "The Crown",
                description = "Biography, Drama",
                price = 0.0,
                category = "Popular",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BZmY0MzAyNTgtMzQ3My00ZGVmLTg3NGEtZTE4OWE2ZTgzZDY4XkEyXkFqcGdeQXVyODUxOTU0OTg@._V1_.jpg"
            ),
            ProductModel(
                name = "Squid Game",
                description = "Action, Thriller",
                price = 0.0,
                category = "Trending",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BYWE3MDVkN2EtNjQ5MS00ZDQ4LTliNzYtM2I2OGE1OTYzYzEzXkEyXkFqcGdeQXVyNjU1OTg4OTM@._V1_.jpg"
            ),
            ProductModel(
                name = "Peaky Blinders",
                description = "Crime, Drama",
                price = 0.0,
                category = "Popular",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BZjYzZDgzMmYtZGIxNS00ZDNmLWJkZWUtOTQyMWYwNWRhNTc0XkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg"
            ),
            ProductModel(
                name = "Breaking Bad",
                description = "Crime, Drama",
                price = 0.0,
                category = "Classic",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BMjhiMzgxZTctNDc1Ni00OTIxLTlhMTUtMTMyN2UxNmJjNmVkXkEyXkFqcGdeQXVyMzQ2MDI5NjU@._V1_.jpg"
            ),
            ProductModel(
                name = "Interstellar",
                description = "Sci-Fi",
                price = 0.0,
                category = "Movies",
                imageUrl = "https://m.media-amazon.com/images/M/MV5BZjdkOTU3MDktN2IxOS00OGEyLWFmMjktY2FiMmZkNWIyODZiXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"
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
