package com.example.foodiemoodie.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.roomDatabase.entities.FavouritesEntity
import com.example.foodie_moodie_20.databinding.FavoriteRecipesRowLayoutBinding


class FavoriteRecipesAdapter : ListAdapter<FavouritesEntity, FavoriteRecipesAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<FavouritesEntity>() {
            override fun areItemsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FavouritesEntity, newItem: FavouritesEntity): Boolean {
                return oldItem.toString() == newItem.toString()
            }
        }
)
{

    lateinit var singleRecipe : FavouritesEntity
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                parent.context.getSystemService(LayoutInflater::class.java).inflate(
                        R.layout.favorite_recipes_row_layout,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
      FavoriteRecipesRowLayoutBinding.bind(holder.itemView).apply{

          singleRecipe = getItem(position)

          favoriteTitleTextView.text=singleRecipe.result.title
          favoriteDescriptionTextView.text=singleRecipe.result.summary
          favoriteHeartTextView.text=singleRecipe.result.aggregateLikes.toString()
          favoriteClockTextView.text=singleRecipe.result.readyInMinutes.toString()
          favoriteRecipeImageView.loadImage(singleRecipe.result.image)

          if(singleRecipe.result.vegan){
              favoriteLeafTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
              favoriteLeafImageView.setColorFilter(ContextCompat.getColor(holder.itemView.context, R.color.green))
          }

         /* root.setOnClickListener { view->

              val action =FavouriteRecipiesFragmentDirections.actionFavouriteRecipiesFragmentToDetailsActivity(singleRecipe.result)
              view.findNavController().navigate(action)
          }*/


      }
    }


}



