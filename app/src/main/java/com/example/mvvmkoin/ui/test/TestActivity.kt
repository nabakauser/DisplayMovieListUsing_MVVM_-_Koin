package com.example.mvvmkoin.ui.test

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mvvmkoin.warehouse.Constants
import com.example.mvvmkoin.R
import com.example.mvvmkoin.databinding.ActivityTestBinding
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.ui.home.MainActivity

class TestActivity : AppCompatActivity() {
    private var binding: ActivityTestBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        //getMovieData()

    }

    @SuppressLint("SetTextI18n")
    fun getMovieData() {
        val bundle = intent.extras?.getBundle(MainActivity.KEY_BUNDLE_RESULT)
        val result = bundle?.getSerializable(MainActivity.KEY_RESULT) as Result

        binding?.uiTvId?.text = getString(R.string.movie_id) + " " + result.id
        binding?.uiIvMovieImage?.let {
            Glide.with(this).load(Constants.IMAGE_URL + result.poster_path).into(it)
        }
        binding?.uiTvTitle?.text = result.title
        binding?.uiTvReleaseDate?.text = getString(R.string.movie_releaseDate) + " " + result.release_date
        binding?.uiTvOverview?.text = result.overview
        binding?.uiTvWebLink?.text = Constants.IMAGE_URL + result.poster_path
    }
}