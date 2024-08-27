package com.example.mynewsapp.ui.fragments

import NewsAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.ui.NewsViewModel
import com.example.mynewsapp.ui.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mynewsapp.ui.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class BreakingNewsFragment : Fragment (R.layout.fragment_breaking_news) {
    lateinit var newsAdapter: NewsAdapter
    val TAG = "breakingNewsFragment"
    val viewModel:NewsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("article",it)
            }

            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner,Observer{response ->
            when(response){
                 is Resource.Success -> {
                     hideProgressBar()
                    response.data?.let{newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    response.message?.let{ message->
                        hideProgressBar()
                        Toast.makeText(activity,"An Error occurred: $message",Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        val paginationProgressBar = requireView().findViewById<ProgressBar>(R.id.paginationProgressBar)
        paginationProgressBar.visibility= View.INVISIBLE
        isLoading = false
    }
    private fun showProgressBar(){
        val paginationProgressBar = requireView().findViewById<ProgressBar>(R.id.paginationProgressBar)
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBreakingNews("us")
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleLayoutPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotAtLastPage = !isLoading and !isLastPage
            val isAtLastItem = firstVisibleLayoutPosition + visibleItemCount >= totalItemCount
            val isNotAtbegining = firstVisibleLayoutPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotAtLastPage and isAtLastItem and isNotAtbegining and isTotalMoreThanVisible and isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        val rvBreakingNews = requireView().findViewById<RecyclerView>(R.id.rvBreakingNews)
        rvBreakingNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}