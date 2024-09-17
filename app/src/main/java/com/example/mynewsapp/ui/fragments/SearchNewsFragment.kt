package com.example.mynewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.ui.NewsAdapter
import com.example.mynewsapp.ui.NewsViewModel
import com.example.mynewsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mynewsapp.ui.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mynewsapp.ui.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val newsAdapter: NewsAdapter by lazy { NewsAdapter() }
    private val viewModel: NewsViewModel by activityViewModels()

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        // Set up item click listener for the adapter
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment, bundle)
        }

        // Set up search EditText with a text change listener
        val etSearch = requireView().findViewById<EditText>(R.id.etSearch)
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString()) // Trigger search with updated query
                    }
                }
            }
        }

        // Observe the search news flow and submit paging data to adapter
        observeSearchNews()
    }

    private fun setupRecyclerView() {
        val rvSearchNews = requireView().findViewById<RecyclerView>(R.id.rvSearchNews)
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeSearchNews() {
        // Collect the PagingData flow from ViewModel
        lifecycleScope.launch {
            viewModel.searchNewsFlow.collectLatest { pagingData ->
                // Submit the PagingData to the adapter
                newsAdapter.submitData(pagingData)
            }
        }
    }
}
