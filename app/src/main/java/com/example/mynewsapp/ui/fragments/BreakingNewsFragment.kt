package com.example.mynewsapp.ui.fragments

import NewsAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.ui.NewsViewModel
import com.example.mynewsapp.ui.util.Resource

class BreakingNewsFragment : Fragment (R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter
    val TAG = "breakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.breakingNews.observe(viewLifecycleOwner,Observer{response ->
            when(response){
                 is Resource.Success -> {
                    response.data?.let{newsResponse->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    response.message?.let{ message->
                        Log.e(TAG,"an error occurred: $message")
                    }
                }
                is Resource.Loading ->{

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getBreakingNews("us")
    }


    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        val rvBreakingNews = requireView().findViewById<RecyclerView>(R.id.rvBreakingNews)
        rvBreakingNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}