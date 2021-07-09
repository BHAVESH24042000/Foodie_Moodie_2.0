package com.example.foodiemoodie.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodiemoodie.R
import com.example.foodiemoodie.databinding.RecipesRowLayoutBinding
import com.example.foodiepoodie.dataModels.Result

/*
class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MyViewHolder>() {

    private var recipe= emptyList<Result>()
    class MyViewHolder(private val binding:RecipesRowLayoutBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {


        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}*/


class RecipesAdapter : ListAdapter<Result, RecipesAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
)
{

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                parent.context.getSystemService(LayoutInflater::class.java).inflate(
                        R.layout.recipes_row_layout,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        RecipesRowLayoutBinding.bind(holder.itemView).apply {
            val singleRecipe = getItem(position)

            titleTextView.text=singleRecipe.title
            descriptionTextView.text=singleRecipe.summary
            heartTextView.text=singleRecipe.aggregateLikes.toString()
            clockTextView.text=singleRecipe.readyInMinutes.toString()
            recipeImageView.loadImage(singleRecipe.image)

            if(singleRecipe.vegan){
            leafTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                leafImageView.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
            }
            root.setOnClickListener {  }

        }

    }


}



fun ImageView.loadImage(uri: String, circleCrop: Boolean = false) {
    if (circleCrop) {
        Glide.with(this).load(uri).circleCrop().into(this)
    } else {
        Glide.with(this).load(uri).into(this)
    }
}