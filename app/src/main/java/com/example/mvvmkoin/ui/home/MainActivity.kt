package com.example.mvvmkoin.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import com.example.mvvmkoin.R
import com.example.mvvmkoin.databinding.ActivityMainBinding
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.ui.favourites.FavouritesActivity
import com.example.mvvmkoin.ui.moviedetails.DisplayMovieActivity
import com.example.mvvmkoin.adapter.MovieAdapter
import com.example.mvvmkoin.viewmodel.MovieViewModel
import com.example.mvvmkoin.viewmodel.StringType
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val movieViewModel: MovieViewModel by viewModel()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter (
            movieList = arrayListOf(),
            onItemClick = this::navigateToDisplayMovieActivity,
            onFavouritesClicked = movieViewModel::onFavouritesClicked
        )
    }
    private val resultContracts by lazy {
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::handleActivityResult
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        resultContracts
        setUpUi()
        setUpListeners()
        setUpObserver()
    }

    private fun setUpUi() {
        binding?.uiRvMovies?.apply {
            adapter = movieAdapter
        }
    }

    private fun setUpListeners() {
        binding?.uiEtSearchBar?.doOnTextChanged { text, _, _, _ ->
            movieViewModel.filterMovieList(text.toString())
        }
        binding?.uiSwipeRefresh?.setOnRefreshListener {
            binding?.uiSwipeRefresh?.isRefreshing = false
            movieViewModel.fetchMovies()
        }
    }

    private fun setMovieListToUi(movies : List<Result>) {
        movieAdapter.onUserListChanged(movies)
    }

    private fun setUpObserver() {
        movieViewModel.message.observe(this){ message ->
            when (message) {
                is StringType.StringResource -> {
                    Toast.makeText(this,getString(message.id),Toast.LENGTH_SHORT).show()
                }
                is StringType.StringRaw -> {
                    Toast.makeText(this,message.message,Toast.LENGTH_SHORT).show()
                }
            }
        }
        movieViewModel.success.observe(this) { movies ->
            binding?.uiProgressBar?.visibility = View.GONE
            movies?.let { movie -> setMovieListToUi(movie) }
        }
        movieViewModel.message.observe(this) {
            binding?.uiProgressBar?.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.uiFavourites) {
            navigateToFavouritesActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToFavouritesActivity() {
        resultContracts.launch(Intent(this, FavouritesActivity::class.java))
    }



    private fun navigateToDisplayMovieActivity(result: Result) {
        val bundle = Bundle()
        bundle.putSerializable(KEY_RESULT,result)
        val intent = Intent(this, DisplayMovieActivity::class.java)
        intent.putExtra(KEY_BUNDLE_RESULT,bundle)
        startActivity(intent)

    }

    private fun handleActivityResult(activityResult : ActivityResult) {
        if(activityResult.resultCode == RESULT_OK ) {
            movieViewModel.fetchMovies()
        }
    }

    companion object{
        const val KEY_RESULT = "key.result"
        const val KEY_BUNDLE_RESULT = "key.bundle.result"
    }
}
