package com.example.ms_team2

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ms_team2.databinding.ActicleRowBinding


class ArticleAdapter(val items:ArrayList<ArticleData>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: ArticleData)
    }
    var itemClickListener: OnItemClickListener?=null

    inner class ViewHolder(val binding: ActicleRowBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ActicleRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply{
            Title.text = items[position].title
            Log.d("adaptertest",items[position].title)
           // articleTitle.text = items[position].title
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}