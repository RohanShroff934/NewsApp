package com.example.mynewsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.ItemArticlePreviewBinding
import com.example.mynewsapp.ui.models.Article

class NewsAdapter : PagingDataAdapter<Article, NewsAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article?, clickListener: ((Article) -> Unit)?) {
            with(binding) {
                // Load image with Glide
                Glide.with(ivArticleImage.context)
                    .load(article?.urlToImage)
                    .placeholder(R.drawable.placeholder)  // Add a placeholder image
                    .error(R.drawable.err)
                    .into(ivArticleImage)

                // Set other article details
                tvSource.text = article?.source?.name
                tvTitle.text = article?.title
                tvDescription.text = article?.description
                tvPublishedAt.text = formatPublishedAt(article?.publishedAt)

                // Set click listener
                root.setOnClickListener {
                    article?.let { clickListener?.invoke(it) }
                }
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article, onItemClickListener)
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    fun formatPublishedAt(publishedAt: String?): String {
        val date = publishedAt?.take(10) ?: " "
        val time = publishedAt?.drop(11)?.take(5) ?: ""
        return "$date  $time"
    }
}
