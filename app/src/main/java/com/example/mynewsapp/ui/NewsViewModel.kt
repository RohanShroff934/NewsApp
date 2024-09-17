package com.example.mynewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.mynewsapp.ui.api.NewsApi
import com.example.mynewsapp.ui.models.Article
import com.example.mynewsapp.ui.paging.NewsPagingSource
import com.example.mynewsapp.ui.paging.SearchNewsPagingSource
import com.example.mynewsapp.ui.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    // Breaking news flow setup
    val breakingNewsFlow = Pager(PagingConfig(pageSize = 20)) {
        NewsPagingSource(newsRepository.api, "us") // Adjust parameters as needed
    }.flow
        .cachedIn(viewModelScope)

    // Search news flow setup
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    // Update the Pager to listen to searchQuery
    val searchNewsFlow = searchQuery.flatMapLatest { query ->
        Pager(PagingConfig(pageSize = 20)) {
            SearchNewsPagingSource(newsRepository.api, query)
        }.flow.cachedIn(viewModelScope)
    }

    fun searchNews(query: String) {
        _searchQuery.value = query
    }

    // Save and delete article functions
    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    // Check internet connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasTransport(TRANSPORT_WIFI) || capabilities.hasTransport(TRANSPORT_CELLULAR) || capabilities.hasTransport(TRANSPORT_ETHERNET)
    }
}
