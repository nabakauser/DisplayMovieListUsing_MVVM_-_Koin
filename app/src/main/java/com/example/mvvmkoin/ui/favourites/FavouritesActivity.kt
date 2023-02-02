package com.example.mvvmkoin.ui.favourites

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmkoin.adapter.MovieAdapter
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.databinding.ActivityFavouritesBinding
import com.example.mvvmkoin.viewmodel.MovieViewModel
import com.example.mvvmkoin.viewmodel.StringType
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesActivity : AppCompatActivity() {

    private var binding: ActivityFavouritesBinding? = null
    private val movieViewModel: MovieViewModel by viewModel()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(
            movieList = arrayListOf(),
            onItemClick = {},
            onFavouritesClicked = {
                onFavouritesRemove(it)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpUi()
        getMovieList()
        setUpObserver()
    }

    private fun setUpUi() {
        binding?.uiRvFavourites?.apply {
            adapter = movieAdapter
        }
    }

    private fun setMovieListToUi(movies : List<Result>) {
        movieAdapter.onUserListChanged(movies)
    }

    private fun setUpObserver() {
        movieViewModel.message.observe(this) { message ->
            when (message) {
                is StringType.StringResource -> {
                    Toast.makeText(this, getString(message.id), Toast.LENGTH_SHORT).show()
                }
                is StringType.StringRaw -> {
                    Toast.makeText(this, message.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getMovieList() {
        setMovieListToUi(movieViewModel.getMovieList())
    }

    private fun onFavouritesRemove(movie: Result) {
        movieViewModel.onFavouritesRemove(movie)
        getMovieList()
    }

    override fun onBackPressed() {
        if(movieViewModel.flag == true) setResult(RESULT_OK)
        else setResult(RESULT_CANCELED)
        super.onBackPressed()
    }
}