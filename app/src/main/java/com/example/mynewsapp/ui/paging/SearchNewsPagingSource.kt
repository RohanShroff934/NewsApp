package com.example.mynewsapp.ui.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mynewsapp.ui.api.NewsApi
import com.example.mynewsapp.ui.models.Article
import com.example.mynewsapp.ui.models.NewsResponse
import com.example.mynewsapp.ui.util.Constants.Companion.API_KEY
import com.example.mynewsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import java.io.IOException


class SearchNewsPagingSource(
    private val newsApi: NewsApi,
    private val query: String
) : PagingSource<Int, Article>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1 // Start with page 1 if no key is provided
        return try {
            val response = newsApi.searchForNews(query, page, API_KEY)
            if (response.isSuccessful) {
                val emptyMutableList: MutableList<Article> = mutableListOf()
                val newsResponse = response.body() ?: NewsResponse(emptyMutableList, "", 0)
                val articles = newsResponse.articles.distinctBy { it.title }

                // Calculate the next page
                val nextPage = if (newsResponse.totalResults <= page * QUERY_PAGE_SIZE) {
                    null // No more pages
                } else {
                    page + 1
                }

                LoadResult.Page(
                    data = articles,
                    prevKey = null,
                    nextKey = nextPage
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
        // Return the page key for the nearest page to the anchor position
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
