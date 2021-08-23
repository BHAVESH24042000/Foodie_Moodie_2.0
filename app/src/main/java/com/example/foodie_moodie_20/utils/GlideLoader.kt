package com.example.foodie_moodie_20.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class GlideLoader(val context: Context){
    fun loadPicture(image:Any,imageView:ImageView){
        Glide
            .with(context)
            .load(image)
            .centerCrop()
            .into(imageView)
    }
}