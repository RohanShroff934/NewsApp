package com.example.mynewsapp.ui.fragments

import NewsAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsapp.R
import com.example.mynewsapp.ui.MainActivity
import com.example.mynewsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SavedNewsFragment : Fragment (R.layout.fragment_saved_news) {

    lateinit var newsAdapter: NewsAdapter
   // @get:Inject
    val viewModel:NewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply{
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Article Deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            val rvSavedNews = requireView().findViewById<RecyclerView>(R.id.rvSavedNews)
            attachToRecyclerView(rvSavedNews)
        }

        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            newsAdapter.differ.submitList(articles)
        })

    }
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        val rvSavedNews = requireView().findViewById<RecyclerView>(R.id.rvSavedNews)
        rvSavedNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}