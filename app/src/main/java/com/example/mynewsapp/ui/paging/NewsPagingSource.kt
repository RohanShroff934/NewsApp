package com.example.mynewsapp.ui.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.room.Query
import com.example.mynewsapp.ui.NewsAdapter
import com.example.mynewsapp.ui.api.NewsApi
import com.example.mynewsapp.ui.models.Article
import com.example.mynewsapp.ui.models.NewsResponse
import com.example.mynewsapp.ui.util.Constants.Companion.API_KEY
import java.io.IOException


class NewsPagingSource(
    private val newsApi: NewsApi,
    private val query: String?
):PagingSource<Int, Article>() {

    private var totalNewsCount = 0
    private val newsAdapter: NewsAdapter by lazy { NewsAdapter() }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key?: 1
        return try {
            var response = newsApi.getBreakingNews("us", page, API_KEY)

            if(query != null) {
                response = newsApi.searchForNews(query, page, API_KEY)
            }

            if (response.isSuccessful) {
                val emptyMutableList: MutableList<Article> = mutableListOf()
                val newsResponse = response.body() ?: NewsResponse(emptyMutableList, "", 0)
                totalNewsCount += newsResponse.articles.size
                val articles = newsResponse.articles.distinctBy { it.title } // Remove duplicates

                LoadResult.Page(
                    data = articles,
                    nextKey = if (newsResponse.totalResults <= totalNewsCount) null else page + 1,
                    prevKey = null
                )
            } else {
                LoadResult.Error(retrofit2.HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}