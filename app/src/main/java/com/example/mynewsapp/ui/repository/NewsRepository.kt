package com.example.mynewsapp.ui.repository

import com.example.mynewsapp.ui.api.RetrofitInstance
import com.example.mynewsapp.ui.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode:String,pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
}