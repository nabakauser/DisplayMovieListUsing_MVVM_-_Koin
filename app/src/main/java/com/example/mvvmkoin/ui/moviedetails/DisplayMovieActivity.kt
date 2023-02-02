package com.example.mvvmkoin.ui.moviedetails

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.example.mvvmkoin.warehouse.Constants.IMAGE_URL
import com.example.mvvmkoin.R
import com.example.mvvmkoin.databinding.ActivityDisplayMovieDetailsBinding
import com.example.mvvmkoin.data.model.Result
import com.example.mvvmkoin.ui.home.MainActivity
import com.example.mvvmkoin.ui.home.MainActivity.Companion.KEY_RESULT
import com.example.mvvmkoin.ui.webview.WebViewActivity
import com.example.mvvmkoin.viewmodel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayMovieActivity : AppCompatActivity() {
    private var binding: ActivityDisplayMovieDetailsBinding? = null
    private val movieViewModel: MovieViewModel by viewModel()
/*    private val activityResultLauncher by lazy{
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::handleActivityResult
        )
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpObserver()
    }

    private fun setUpObserver() {
        movieViewModel.success.observe(this, {
            binding?.uiProgressBar?.visibility = View.GONE
            getMovieData()
        })
    }

    @SuppressLint("SetTextI18n", "CheckResult", "ResourceAsColor")
    fun getMovieData() {
        val bundle = intent.extras?.getBundle(MainActivity.KEY_BUNDLE_RESULT)
        val result = bundle?.getSerializable(KEY_RESULT) as Result



        binding?.uiTvId?.text = getString(R.string.movie_id) + " " + result.id
        binding?.uiIvMovieImage?.let { Glide.with(this).load(IMAGE_URL + result.poster_path).into(it) }
        binding?.uiTvTitle?.text = result.title
        binding?.uiTvReleaseDate?.text = getString(R.string.movie_releaseDate) + " " + result.release_date
        binding?.uiTvOverview?.text = result.overview
        binding?.uiTvWebLink?.text = IMAGE_URL + result.poster_path

        binding?.uiTvWebLink?.setOnClickListener {
           // getUrl(IMAGE_URL + result.poster_path)
            val intent  = Intent(this, WebViewActivity::class.java)
            intent.putExtra(KEY_MOVIE,IMAGE_URL + result.poster_path)
            startActivity(intent)
            //counter()
//            val builder = CustomTabsIntent.Builder()
////            * @param enterResId -> "enter" animation for the browser.
////            * @param exitResId -> "exit" animation for the application.
//            builder.setStartAnimations(this,android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//            // builder.setExitAnimations(this,android.R.anim.slide_in_left, android.R.anim.slide_out_right)
//            val customTabsIntent = builder.build()
//            customTabsIntent.launchUrl(this, Uri.parse(IMAGE_URL + result.poster_path))

                //navigateToWebView(result)
                //customTabBuilder()

            }
        }

    private fun getUrl(url : String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    private fun navigateToWebView(result: Result) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(KEY_MOVIE,IMAGE_URL + result.poster_path)
        startActivity(intent)
    }

/*    private fun handleActivityResult(activityResult: ActivityResult) {
        Log.d("DisplayMovie", "handleActivityResult: ${activityResult.resultCode}")
        if (activityResult.resultCode == RESULT_OK) {
            if(activityResult.data?.getStringExtra("NAME") != null) {
                Log.d("DisplayMovie", "handleActivityResult: ${activityResult.data}")
            }
            //ur logics
        }
    }*/



    companion object{
        const val KEY_MOVIE = "key.movie"
    }


}