package com.rmd.business.newsapp.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rmd.business.newsapp.R
import com.rmd.business.newsapp.databinding.RowNewsBinding
import com.rmd.business.newsapp.domain.model.Article

class ArticleRecycleAdapter : RecyclerView.Adapter<ArticleRecycleAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val binding = RowNewsBinding.bind(holder.itemView)
        val article = differ.currentList[position]

        holder.itemView.apply {
            binding.articleTitle.text = article.title
            binding.articleSource.text = article.source.name
            binding.articleDateTime.text = article.publishedAt
            binding.articleDescription.text = article.description
            Glide.with(this).load(article.urlToImage).into(binding.articleImage)
        }

        holder.itemView.setOnClickListener {
            /*NavHostFragment.findNavController()
                .navigate()*/
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url ==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

}