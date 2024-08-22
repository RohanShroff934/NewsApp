package com.example.mynewsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynewsapp.ui.repository.NewsRepository

class NewsViewModelProviderFactory(
    val newsRepository: Application, val app: NewsRepository
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository,app) as T
    }
}