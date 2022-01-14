package com.tuwaiq.newsplanet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.tuwaiq.newsplanet.R
import com.tuwaiq.newsplanet.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // this DiffUtil object will help to update the recyclerView with focusing only on the items that changed ..
    // not updating the whole recycler like the notifyDataSetChanged ..
    private val diffCallback = object : DiffUtil.ItemCallback<Article>() {

        // this function will compare a specific item if it's same or not ..
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }


        // and this function will compare the whole content of the article if its changed or not ..
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    // this AsyncListDiffer will work on the background thread ..
    val mDiffer = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = mDiffer.currentList[position]
        holder.itemView.apply {
            // trying coil ..
            ivArticleImage.load(article.urlToImage)
            //tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            //tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                // here I used let to check if onClickListener is not null I call the fun with this article ..
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        // I didn't pass a list of item to the constructor like always but I used ListDiffer so I will work with it here and it will deal will items count ..
        return mDiffer.currentList.size
    }

    // this will pass the current article to the fragment whenever we clicked on the recyclerViewItem ..
    private var onItemClickListener: ((Article) -> Unit)? = null

    // this function what I will call when I click on an item in the recycler ..
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}