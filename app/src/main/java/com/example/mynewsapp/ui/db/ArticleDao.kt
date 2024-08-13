package com.example.mynewsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.example.mynewsapp.ui.models.Article

@Dao
interface ArticleDao {
    @Insert
    suspend fun insert(article: Article):Long

    @Query("Select * from articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article): Int
}