package com.example.mvvmkoin.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.mvvmkoin.databinding.ActivityWebViewBinding
import com.example.mvvmkoin.ui.moviedetails.DisplayMovieActivity.Companion.KEY_MOVIE
import com.example.mvvmkoin.ui.test.TestActivity


class WebViewActivity : AppCompatActivity() {

    private var binding: ActivityWebViewBinding? = null



    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        getUrl()

        object : CountDownTimer(5000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.d("WebActivity", "onTick: ${millisUntilFinished/1000}")
            }

            override fun onFinish() {
                navigateToPreviousActivity()
                //onBackPressed()
                finish()
            }
        }.start()

    }

    private fun getUrl() {
        val keyUrl = intent.getStringExtra(KEY_MOVIE)
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(keyUrl))
    }

    private fun navigateToPreviousActivity() {
        val intent = Intent(this, TestActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        Log.d("DisplayMovie", "onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("DisplayMovie", "onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DisplayMovie", "onResume")
    }
}


//    @SuppressLint("SetJavaScriptEnabled")
//    private fun getMovieData() {
//
//        val result = intent?.getStringExtra(KEY_MOVIE) ?: ""
//        Log.d("movieImage", "getMovieData: "+result)
//
//        val webView = binding?.uiWebView
//        webView?.loadUrl(result)
//        Log.d("result", "getMovieData: $result")
//        webView?.settings?.javaScriptEnabled = true
//        webView?.settings?.domStorageEnabled = true
//        webView?.webViewClient = WebViewClient()
//
//    }
//always pass a string inside a webView -> intent -> don't pass as bundle