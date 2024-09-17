// AppModule.kt
package com.example.mynewsapp.ui.singletons

import android.content.Context
import androidx.room.Room
import com.example.mynewsapp.ui.api.NewsApi
import com.example.mynewsapp.ui.db.ArticleDatabase
import com.example.mynewsapp.ui.repository.NewsRepository
import com.example.mynewsapp.ui.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        api: NewsApi,
        db: ArticleDatabase
    ): NewsRepository {
        return NewsRepository(api, db)
    }
}
