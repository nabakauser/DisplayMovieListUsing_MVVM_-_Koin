package com.example.mvvmkoin.repository

import com.example.mvvmkoin.warehouse.Constants.API_KEY
import com.example.mvvmkoin.manager.PreferenceManager
import com.example.mvvmkoin.service.ApiService
import com.example.mvvmkoin.data.mapper.MoviesMapper
import com.example.mvvmkoin.data.model.Movies
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.utils.Resource

class MainRepository (
    private val preferenceManager: PreferenceManager,
    private val apiService: ApiService
    ) {

    fun getMovie(): MutableList<Result> {
        return preferenceManager.getMovieList()
    }

    fun saveMovie(movie: Result) {
        preferenceManager.saveMovieInfo(movie)
    }

    fun removeMovie(movie: Result) {
        preferenceManager.removeMovieFromList(movie)
    }

    suspend fun getAllMovies() : Resource<Movies> {
        return MoviesMapper.map(apiService.getPopularMovies(API_KEY))
    }

    //fun getName(): LiveData<String> = preferenceManager {}
}

//      Because the app will be able to get data from the network as well as keep an offline
// cache of previously downloaded results, you'll need a way for your app to organize
// these multiple sources of data. You'll do this by implementing a repository class,
// which will serve as a single source of truth for the app's data, and abstract
// the source of the data (network, cache, etc.) out of the view model.


//      Inside the below repository class, we need to pass the retrofit service instance
// to perform the network call. The repository class will only interact with the network source,
// the response of the network call will be handled later in ViewModel.