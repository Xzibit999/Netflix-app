package com.example.myapplication.ui.login

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.ProductModel

class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val movie = intent.getParcelableExtra<ProductModel>("movie")

        if (movie != null) {
            findViewById<TextView>(R.id.tvMovieTitle).text = movie.name
            findViewById<TextView>(R.id.tvMovieOverview).text = movie.description
            
            val ivBackdrop = findViewById<ImageView>(R.id.ivMovieBackdrop)
            Glide.with(this)
                .load(movie.imageUrl)
                .into(ivBackdrop)
        }

        findViewById<Button>(R.id.btnPlayMovie).setOnClickListener {
            // Logic to play movie (e.g. open VideoPlayerActivity)
        }
    }
}
