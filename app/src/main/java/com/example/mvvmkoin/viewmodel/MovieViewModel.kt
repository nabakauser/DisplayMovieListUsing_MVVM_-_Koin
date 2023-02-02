package com.example.mvvmkoin.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.mvvmkoin.R
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.repository.MainRepository
import com.example.mvvmkoin.utils.NetworkHelper
import com.example.mvvmkoin.utils.Status
import kotlinx.coroutines.launch

class MovieViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val movies: ArrayList<Result> = arrayListOf()
    var flag = false
        private set
    private val successLD = MutableLiveData<List<Result>>()
    val success : LiveData<List<Result>> = successLD.map { it }
    // -> difference btw map &switch map -> switch map: must return only a live data


    private val messageLD = MutableLiveData<StringType>()
    val message : LiveData<StringType> = messageLD


    init {
        fetchMovies()
    }

    fun fetchMovies() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getAllMovies().let {
                    if (it.status == Status.SUCCESS) {
                        it.data?.results?.let { it1 -> movies.addAll(it1) }
                        checkDataSavedInDb(movies)
                        successLD.value = it.data?.results

                    } else if (it.status == Status.ERROR){
                        messageLD.value = StringType.StringRaw(it.msg.toString())
                    }
                }
            } else messageLD.value = StringType.StringResource(R.string.msg_no_internet)
        }
    }

    fun filterMovieList(searchText: String) {
        if (searchText.isEmpty()) {
            successLD.value = movies
        }else {
            val filteredList = movies.filter {
                it.title.contains(searchText,true)
            }
            successLD.value = filteredList
        }
    }

    fun onFavouritesClicked(movie: Result) {
        if(!movie.isSaved) {
            movie.isSaved = true
            mainRepository.saveMovie(movie)
            messageLD.value = StringType.StringResource(R.string.msg_saved)
        } else {
            movie.isSaved = false
            mainRepository.removeMovie(movie)
            messageLD.value = StringType.StringResource(R.string.msg_removed)
        }
    }

    fun getMovieList(): MutableList<Result> {
        return mainRepository.getMovie()
    }

    fun onFavouritesRemove(movie: Result) {
        if(movie.isSaved) {
            movie.isSaved = false
            flag = true
            mainRepository.removeMovie(movie)
            messageLD.value = StringType.StringResource(R.string.msg_removed)

        }
    }

    private fun checkDataSavedInDb(movieList : List<Result>) {
        movieList.forEach { movie ->
            val savedList = mainRepository.getMovie()
            savedList.forEach {
                if(it.id == movie.id) {
                    movie.isSaved = true
                }
            }
        }
    }
}

sealed class StringType{
    class StringResource(@StringRes val  id : Int) : StringType()
    class StringRaw(val message : String) : StringType()
}