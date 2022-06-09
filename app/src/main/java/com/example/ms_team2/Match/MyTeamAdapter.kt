package com.example.ms_team2.Match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ms_team2.Team.MyTeamData
import com.example.ms_team2.databinding.MyteamRowBinding

class MyTeamAdapter(val items:ArrayList<MyTeamData>) : RecyclerView.Adapter<MyTeamAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: MyTeamData)
    }
    var itemClickListener: OnItemClickListener?=null

    inner class ViewHolder(val binding: MyteamRowBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTeamAdapter.ViewHolder {
        val view = MyteamRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyTeamAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            LeagueName.text = items[position].League
            TeamName.text = items[position].teamname

            Glide.with(root).load(items[position].teamimg)
                .override(150, 150).into(TeamImg)

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}