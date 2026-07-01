package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.ProductModel
import com.example.myapplication.network.TmdbMovie
import com.example.myapplication.repository.MovieRepository

class MovieViewModel : ViewModel() {
    private val repository = MovieRepository()

    private val _trendingMovies = MutableLiveData<List<ProductModel>>()
    val trendingMovies: LiveData<List<ProductModel>> = _trendingMovies

    private val _popularMovies = MutableLiveData<List<ProductModel>>()
    val popularMovies: LiveData<List<ProductModel>> = _popularMovies

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _featuredMovie = MutableLiveData<ProductModel>()
    val featuredMovie: LiveData<ProductModel> = _featuredMovie

    private val _featuredVideoKey = MutableLiveData<String?>()
    val featuredVideoKey: LiveData<String?> = _featuredVideoKey

    fun fetchAllMovies(apiKey: String) {
        _isLoading.value = true
        
        repository.getTrendingMovies(apiKey) { response ->
            response?.results?.let { tmdbMovies ->
                _trendingMovies.value = tmdbMovies.map { it.toProductModel("Trending") }
                if (_featuredMovie.value == null) {
                    val firstMovie = tmdbMovies.firstOrNull()
                    val featured = firstMovie?.toProductModel("Featured")
                    featured?.let { 
                        _featuredMovie.value = it 
                        fetchVideoForKey(firstMovie.id, apiKey)
                    }
                }
            }
            checkLoadingFinished()
        }

        repository.getPopularMovies(apiKey) { response ->
            response?.results?.let { tmdbMovies ->
                _popularMovies.value = tmdbMovies.map { it.toProductModel("Popular") }
            }
            checkLoadingFinished()
        }
    }

    private fun fetchVideoForKey(movieId: Int, apiKey: String) {
        repository.getMovieVideo(movieId, apiKey) { key ->
            _featuredVideoKey.value = key
        }
    }

    private fun checkLoadingFinished() {
        if (_trendingMovies.value != null && _popularMovies.value != null) {
            _isLoading.value = false
        }
    }

    private fun TmdbMovie.toProductModel(cat: String): ProductModel {
        return ProductModel(
            id = this.id.toString(),
            name = this.title ?: this.name ?: "Unknown",
            description = this.overview,
            price = this.voteAverage,
            imageUrl = "https://image.tmdb.org/t/p/w500${this.posterPath}",
            category = cat,
            backdropUrl = "https://image.tmdb.org/t/p/original${this.backdropPath}"
        )
    }
}
