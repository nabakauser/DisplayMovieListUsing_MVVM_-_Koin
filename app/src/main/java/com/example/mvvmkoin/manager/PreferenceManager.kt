package com.example.mvvmkoin.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.mvvmkoin.data.model.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceManager(context: Context) {

    companion object {
        const val MOVIE_RESULTS = "key.movie.results"
    }

    private val mySharedPreferences: SharedPreferences =
        context.getSharedPreferences(MOVIE_RESULTS, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = mySharedPreferences.edit()

    fun getMovieList() : MutableList<Result> {
        var savedList:List<Result> = arrayListOf()
        val movie = mySharedPreferences.getString(MOVIE_RESULTS, null)
        if(movie?.isNotEmpty()==true) {
            val type = object: TypeToken<ArrayList<Result>>(){}.type
            savedList = Gson().fromJson<ArrayList<Result>>(movie,type)
        }
        return savedList.toMutableList()
    }

    fun saveMovieInfo(movieData: Result) {
        val getMovieList = getMovieList()
        val movieModel = getMovieList.find { it.id == movieData.id }
        if(movieModel == null) {
            movieData.let { getMovieList.add(it) }
            val movie = Gson().toJson(getMovieList)
            editor?.putString(MOVIE_RESULTS, movie)?. apply()
        }
    }

    fun removeMovieFromList(movieData: Result) {
        val getMovieList = getMovieList()
        val movieModel = getMovieList.find { it.id == movieData.id }
        if(movieModel != null ) {
            movieData.let { getMovieList.remove(movieModel) }
            val movie = Gson().toJson(getMovieList)
            editor?.putString(MOVIE_RESULTS, movie)?.apply()
        }
    }
}