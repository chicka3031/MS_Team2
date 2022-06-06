package com.example.ms_team2

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ms_team2.databinding.MainrowBinding


class ScheAdapter(val items:ArrayList<ScheData>) : RecyclerView.Adapter<ScheAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: ScheData)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: MainrowBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }
           // Log.d("urlimg",items[adapterPosition].hometeamimg)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MainrowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply{
            DateView.text = items[position].match_date+" "+items[position].match_time
            ScheView.text = items[position].hometeam + " VS " + items[position].awayteam

            Glide.with(root).load(items[position].hometeamimg)
                .override(150, 150).into(hometeamImg)
            Glide.with(root).load(items[position].awayteamimg)
                .override(150, 150).into(awayteamImg)

            //Log.d("glide", items[position].hometeamimg)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}