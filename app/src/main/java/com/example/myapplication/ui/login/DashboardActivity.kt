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
import com.bumptech.glide.Glide
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.TmdbResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private val userRepo = UserRepoImpl()
    private lateinit var popularAdapter: ProductAdapter
    private lateinit var trendingAdapter: ProductAdapter
    
    // Replace with your real TMDB API Key
    private val TMDB_API_KEY = "c8ac72ad3535de0d68ee95a6bb28a544"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "" // Clear default title to show NETFLIX logo-style text

        val ivFeatured = findViewById<android.widget.ImageView>(R.id.ivFeatured)
        Glide.with(this)
            .load("https://images.tmdb.org/t/p/w1280/56v2KjBlU4p9rTzuZ113vNA2SZb.jpg") // Wednesday Backdrop
            .centerCrop()
            .into(ivFeatured)

        setupRecyclerViews()
        
        fetchTrendingMovies()
        fetchPopularMovies()

        findViewById<Button>(R.id.btnPlay).setOnClickListener {
            startActivity(Intent(this, com.example.myapplication.bottom_menu.VideoPlayerActivity::class.java))
        }

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

    private fun fetchTrendingMovies() {
        RetrofitClient.instance.getTrending(TMDB_API_KEY).enqueue(object : Callback<TmdbResponse> {
            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results?.map {
                        ProductModel(
                            id = it.id.toString(),
                            name = it.title ?: it.name ?: "Unknown",
                            description = it.overview,
                            category = "Trending",
                            imageUrl = "https://image.tmdb.org/t/p/w500${it.posterPath}"
                        )
                    } ?: emptyList()
                    trendingAdapter.updateData(movies)
                }
            }
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Failed to load trending", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchPopularMovies() {
        RetrofitClient.instance.getPopularMovies(TMDB_API_KEY).enqueue(object : Callback<TmdbResponse> {
            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                if (response.isSuccessful) {
                    val movies = response.body()?.results?.map {
                        ProductModel(
                            id = it.id.toString(),
                            name = it.title ?: it.name ?: "Unknown",
                            description = it.overview,
                            category = "Popular",
                            imageUrl = "https://image.tmdb.org/t/p/w500${it.posterPath}"
                        )
                    } ?: emptyList()
                    popularAdapter.updateData(movies)
                }
            }
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Failed to load popular", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
