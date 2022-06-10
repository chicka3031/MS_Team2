package com.example.ms_team2.Main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ms_team2.R
import kotlinx.android.synthetic.main.article_item.view.*

class ArticleAdapter (var items : ArrayList<articleItem>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.article_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount() = items.count()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item : articleItem) {
            itemView.articleTitle.text = item.title
            itemView.articleExplain.text = item.explain
            itemView.articleTime.text = item.time

            Glide.with(itemView).load(item.poster).into(itemView.articleImage)
        }
    }
}