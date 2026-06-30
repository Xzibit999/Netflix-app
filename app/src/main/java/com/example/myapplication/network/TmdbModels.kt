package com.example.myapplication.network

import com.google.gson.annotations.SerializedName

data class TmdbResponse(
    val results: List<TmdbMovie>
)

data class TmdbMovie(
    val id: Int,
    val title: String?,
    val name: String?, // For TV shows
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    val overview: String,
    @SerializedName("vote_average") val voteAverage: Double
)
