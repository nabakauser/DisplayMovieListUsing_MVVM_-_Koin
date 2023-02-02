package com.example.mvvmkoin.data.mapper

import com.example.mvvmkoin.data.model.Movies
import com.example.mvvmkoin.utils.Resource
import retrofit2.Response

class MoviesMapper {
    companion object {
        fun map(movies : Response<Movies>) : Resource<Movies> {
            return if (movies.isSuccessful){
                 Resource.success(movies.body())
            }else Resource.error(movies.message(),null)
        }
    }
}