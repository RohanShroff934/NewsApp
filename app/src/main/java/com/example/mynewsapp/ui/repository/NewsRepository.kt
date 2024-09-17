package com.example.mynewsapp.ui.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mynewsapp.ui.api.NewsApi
import com.example.mynewsapp.ui.db.ArticleDatabase
import com.example.mynewsapp.ui.models.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    val api: NewsApi,
    private val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { db.getArticleDao().getAllArticles() }
        ).flow
    }

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article.id.toString())
}
