package com.example.foodie_moodie_20.firebase.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.databinding.GlobalRvItemOneBinding
import com.example.foodie_moodie_20.firebase.FirebaseMemoryDetailsActivity
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Memory
import com.example.foodie_moodie_20.utils.Constants
import com.example.foodie_moodie_20.utils.GlideLoader
import kotlinx.android.synthetic.main.global_rv_item_one.view.*


class GlobalMemoryAdapter(private val context : Context, private var list : ArrayList<Memory>) :
                                                        RecyclerView.Adapter<GlobalMemoryAdapter.MyViewHolder>(){

    class MyViewHolder ( val binding : GlobalRvItemOneBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(GlobalRvItemOneBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]



        GlideLoader(context).loadPicture(model.imageURL, holder.binding.ivCircleImage)
        holder.binding.tvTitle.text = model.title
        holder.binding.tvCityName.text ="${model.cityName[0].toString().toUpperCase()}${model.cityName.substring(1)}"
        holder.binding.tvDescription.text = model.description
        holder.itemView.tvName.text="${model.date}, ${model.userName}"

        holder.itemView.setOnClickListener{
            val intent= Intent(context, FirebaseMemoryDetailsActivity::class.java)
            intent.putExtra(Constants.MEMORY_DETAILS,model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}