package com.example.foodiemoodie.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_moodie_20.R

import com.example.foodie_moodie_20.databinding.RecipesRowLayoutBinding
import com.example.foodie_moodie_20.uiScreens.fragments.RecipiesFragmentDirections


import com.example.foodiepoodie.dataModels.Result



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
      RecipesRowLayoutBinding.bind(holder.itemView).apply{

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
          root.setOnClickListener { view->

              val action = RecipiesFragmentDirections.actionRecipiesFragmentToDetailsActivity(singleRecipe)
              view.findNavController().navigate(action)
          }


      }
    }


}



fun ImageView.loadImage(uri: String?, circleCrop: Boolean = false) {
    if (circleCrop) {
        Glide.with(this).load(uri).circleCrop().into(this)
    } else {
        Glide.with(this).load(uri).into(this)
    }
}