package com.example.myapplication.repository

import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.TmdbResponse
import com.example.myapplication.network.TmdbVideoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository {
    private val apiService = RetrofitClient.instance

    fun getTrendingMovies(apiKey: String, onResult: (TmdbResponse?) -> Unit) {
        apiService.getTrending(apiKey).enqueue(object : Callback<TmdbResponse> {
            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                onResult(response.body())
            }
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getPopularMovies(apiKey: String, onResult: (TmdbResponse?) -> Unit) {
        apiService.getPopularMovies(apiKey).enqueue(object : Callback<TmdbResponse> {
            override fun onResponse(call: Call<TmdbResponse>, response: Response<TmdbResponse>) {
                onResult(response.body())
            }
            override fun onFailure(call: Call<TmdbResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getMovieVideo(movieId: Int, apiKey: String, onResult: (String?) -> Unit) {
        apiService.getMovieVideos(movieId, apiKey).enqueue(object : Callback<TmdbVideoResponse> {
            override fun onResponse(call: Call<TmdbVideoResponse>, response: Response<TmdbVideoResponse>) {
                val video = response.body()?.results?.find { it.site == "YouTube" && it.type == "Trailer" }
                onResult(video?.key)
            }
            override fun onFailure(call: Call<TmdbVideoResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}
