package com.example.mynewsapp.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class ArticleFragment : Fragment (R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    val args:ArticleFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article
        val webView: WebView = view.findViewById(R.id.webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }


        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Optionally show a loading indicator
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Optionally hide the loading indicator
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                // Handle the error
                Toast.makeText(context, "Error: $description", Toast.LENGTH_SHORT).show()
            }
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url?.toString().orEmpty())
                return true
            }

        }
        webView.loadUrl(article.url)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

         fab.setOnClickListener{
            viewModel.saveArticle(article)
             Snackbar.make(view,"Article has been saved",Snackbar.LENGTH_SHORT).show()
         }

    }

}