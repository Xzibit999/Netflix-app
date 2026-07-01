package com.example.myapplication.repository

import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.TmdbResponse
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
}
