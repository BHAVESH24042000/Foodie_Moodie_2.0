package com.example.foodie_moodie_20.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R

import com.example.foodie_moodie_20.databinding.IngredientsRowLayoutBinding
import com.example.foodie_moodie_20.databinding.RecipesRowLayoutBinding
import com.example.foodie_moodie_20.utils.Constants.Companion.BASE_IMAGE_URL
import com.example.foodiemoodie.adapters.RecipesAdapter
import com.example.foodiemoodie.adapters.loadImage
import com.example.foodiepoodie.dataModels.ExtendedIngredient
import com.example.foodiepoodie.dataModels.Result
import java.util.*

class IngredientsAdapter: ListAdapter<ExtendedIngredient, IngredientsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ExtendedIngredient>() {
        override fun areItemsTheSame(oldItem: ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem:ExtendedIngredient, newItem: ExtendedIngredient): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }
) {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.context.getSystemService(LayoutInflater::class.java).inflate(
                R.layout.ingredients_row_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        IngredientsRowLayoutBinding.bind(holder.itemView).apply{

           var ingredient = getItem(position)



            ingredientImageView.loadImage(BASE_IMAGE_URL + ingredient.image)
            ingredientName.text = ingredient.name?.capitalize(Locale.ROOT)
            ingredientAmount.text = ingredient.amount.toString()
            ingredientUnit.text = ingredient.unit
            ingredientConsistency.text = ingredient.consistency
            ingredientOriginal.text = ingredient.original

        }
    }


}