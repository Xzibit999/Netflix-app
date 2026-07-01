package com.example.myapplication.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.repository.UserRepoImpl
import com.example.myapplication.viewmodel.MovieViewModel
import com.facebook.shimmer.ShimmerFrameLayout

class DashboardActivity : AppCompatActivity() {

    private val userRepo = UserRepoImpl()
    private val movieViewModel: MovieViewModel by viewModels()
    
    private lateinit var popularAdapter: ProductAdapter
    private lateinit var trendingAdapter: ProductAdapter
    
    private lateinit var shimmerView: ShimmerFrameLayout
    private lateinit var mainScrollView: View

    private val TMDB_API_KEY = "c8ac72ad3535de0d68ee95a6bb28a544"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        initViews()
        setupRecyclerViews()
        observeViewModel()

        movieViewModel.fetchAllMovies(TMDB_API_KEY)
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        shimmerView = findViewById(R.id.shimmerView)
        mainScrollView = findViewById(R.id.mainScrollView)

        findViewById<Button>(R.id.btnPlay).setOnClickListener {
            startActivity(Intent(this, com.example.myapplication.bottom_menu.VideoPlayerActivity::class.java))
        }

        findViewById<Button>(R.id.btnSignOut).setOnClickListener {
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

    private fun observeViewModel() {
        movieViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                shimmerView.startShimmer()
                shimmerView.visibility = View.VISIBLE
                mainScrollView.visibility = View.GONE
            } else {
                shimmerView.stopShimmer()
                shimmerView.visibility = View.GONE
                mainScrollView.visibility = View.VISIBLE
            }
        }

        movieViewModel.trendingMovies.observe(this) { movies ->
            trendingAdapter.updateData(movies)
        }

        movieViewModel.popularMovies.observe(this) { movies ->
            popularAdapter.updateData(movies)
        }

        movieViewModel.featuredMovie.observe(this) { movie ->
            val ivFeatured = findViewById<ImageView>(R.id.ivFeatured)
            val tvFeaturedTitle = findViewById<TextView>(R.id.tvFeaturedTitle)
            
            tvFeaturedTitle.text = movie.name
            Glide.with(this)
                .load(movie.backdropUrl)
                .centerCrop()
                .into(ivFeatured)
        }

        movieViewModel.featuredVideoKey.observe(this) { videoKey ->
            if (videoKey != null) {
                findViewById<Button>(R.id.btnPlay).setOnClickListener {
                    val intent = Intent(this, com.example.myapplication.bottom_menu.VideoPlayerActivity::class.java)
                    intent.putExtra("VIDEO_URL", "https://www.youtube.com/watch?v=$videoKey")
                    startActivity(intent)
                }
            }
        }
    }
}
