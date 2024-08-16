package com.example.mynewsapp.ui.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id :Int? = 0,
    val author: String? = "hello",
    val content: String? = "hello",
    val description: String? = "hi",
    val publishedAt: String? = "hi",
    val source: Source,
    val title: String? = "hi",
    val url: String = "hi",
    val urlToImage: String? = "hi"
): Serializable{
    override fun hashCode(): Int {
        return (title ?: "").hashCode() // Use empty string if title is null
    }
}