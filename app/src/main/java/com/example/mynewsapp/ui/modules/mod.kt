package com.example.mynewsapp.ui

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.mynewsapp.ui.db.ArticleDatabase
import com.example.mynewsapp.ui.db.ArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideArticleDatabase(context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            "article_db.db"
        ).build()
    }

    @Provides
    fun provideArticleDao(database: ArticleDatabase): ArticleDao {
        return database.getArticleDao()
    }
}
