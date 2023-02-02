package com.example.mvvmkoin.di

import com.example.mvvmkoin.manager.PreferenceManager
import com.example.mvvmkoin.module.RestHelper
import com.example.mvvmkoin.repository.MainRepository
import com.example.mvvmkoin.utils.NetworkHelper
import com.example.mvvmkoin.viewmodel.MovieViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object Test {
    fun modules() = commonModule + repositoryModule + viewModelModule
}

val repositoryModule  = module {
    single { MainRepository(get(),get()) }
    //single { MainRepository(get(),get()) } -> for multiple repositories
}

val viewModelModule = module {
    viewModel { MovieViewModel(get(),get()) }
}

val commonModule = module {
    single {
        NetworkHelper(androidContext())
    }
    single { RestHelper.client }
    single { PreferenceManager(androidContext()) }
}