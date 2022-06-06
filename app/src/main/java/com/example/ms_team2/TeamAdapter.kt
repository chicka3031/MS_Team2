package com.example.ms_team2

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ms_team2.databinding.RowBinding

class TeamAdapter(val items:ArrayList<TeamData>) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnItemClick(data: TeamData)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        init{
            binding.root.setOnClickListener{
                itemClickListener?.OnItemClick(items[adapterPosition])
                Log.d("click", "adapter")
                items[adapterPosition].isSelected != items[adapterPosition].isSelected
                if(items[adapterPosition].isSelected) {
                    binding.wishView.setImageResource(R.drawable.ic_baseline_star_24)
                }else{
                    binding.wishView.setImageResource(R.drawable.ic_baseline_star_border_24)
                }
            //binding.teamImg.setImageDrawable()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply{
            teamView.text = items[position].position.toString() + ".   " + items[position].teamname
//            val requestBuilder: RequestBuilder<PictureDrawable> = GlideToVectorYou
//                .init()
//                .with(this)
//                .requestBuilder
//
//            // Customize request like you were using Glide normally
//            requestBuilder
//                .load((Uri.parse(IMAGE_URL)))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageview)

      //      GlideToVectorYou.justLoadImage(this, Uri.parse(items[position].teamimg), teamImg)

            Glide.with(root).load(items[position].teamimg)
                .override(150, 150).into(teamImg)

            Log.d("bindview", items[position].teamname)
            if(items[position].isSelected){
                wishView.setImageResource(R.drawable.ic_baseline_star_24)
            }else{
                wishView.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}