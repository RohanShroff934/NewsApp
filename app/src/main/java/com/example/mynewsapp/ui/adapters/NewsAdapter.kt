import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsapp.R
import com.example.mynewsapp.databinding.ItemArticlePreviewBinding
import com.example.mynewsapp.ui.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, clickListener: ((Article) -> Unit)?) {
            with(binding) {
                Log.d("ArticleData", "Image URL: ${article.urlToImage}")

                Glide.with(ivArticleImage.context)
                    .load(article.urlToImage)
                    .placeholder(R.drawable.placeholder)  // Add a placeholder image
                    .error(R.drawable.error_image)
                    .into(ivArticleImage)
                Log.d("ArticleDescription", "Description: ${article.description}")
                //Log.d("ArticleTitle", "Description: ${article.title}")


                tvSource.text = article.source.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                //tvDescription.text = "Hello"
                val publishedAt = article.publishedAt
                tvPublishedAt.text = formatPublishedAt(publishedAt)


                root.setOnClickListener {
                    clickListener?.invoke(article)
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
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
