package com.example.foodie_moodie_20.firebase.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.databinding.PersonalRvItemBinding
import com.example.foodie_moodie_20.firebase.FirebaseAddEditMemory
import com.example.foodie_moodie_20.firebase.FirebaseMemoryDetailsActivity
import com.example.foodie_moodie_20.firebase.FirebasePersonalFragment
import com.example.foodie_moodie_20.firebase.firebaseDataModels.Memory
import com.example.foodie_moodie_20.utils.Constants
import com.example.foodie_moodie_20.utils.GlideLoader
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.personal_rv_item.view.*

class PersonalMemoryAdapter(private val context : Context, private var list : ArrayList<Memory>) :
                                                                        RecyclerView.Adapter<PersonalMemoryAdapter.MyViewHolder>(){

    class MyViewHolder ( val binding : PersonalRvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(PersonalRvItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]


        GlideLoader(context).loadPicture(model.imageURL, holder.binding.ivCircleImage)
        holder.binding.tvTitle.text = model.title
        holder.binding.tvCityName.text =
            "${model.cityName[0].toString().toUpperCase()}${model.cityName.substring(1)}"
        holder.binding.tvDescription.text = model.description
        //holder.binding.tvRating.text = model.rating.toString()
        holder.itemView.tvDate.text = model.date

        holder.itemView.setOnClickListener{
            val intent=Intent(context, FirebaseMemoryDetailsActivity::class.java)
            intent.putExtra(Constants.MEMORY_DETAILS,model)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    private val mFireStore = FirebaseFirestore.getInstance()
    fun notifyDeleteItem(fragment: FirebasePersonalFragment, position: Int) {
        fragment.showProgressBar()
       // FireStoreClass().deleteMemory(fragment,list[position].memoryID)
        mFireStore.collection(Constants.MEMORIES)
            .document(list[position].memoryID)
            .delete()
            .addOnSuccessListener {
                Log.e("Deleted","DONE")
                fragment.deleteSuccessful()
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
                Log.e(fragment.javaClass.simpleName, "Error while deleting the memory.")
            }

        notifyItemRemoved(position)

    }

    fun notifyEditItem(fragment: FirebasePersonalFragment, position: Int){
        val intent = Intent(context, FirebaseAddEditMemory::class.java)
        intent.putExtra(Constants.PASS_MEMORY,list[position])
        fragment.startActivity(intent)
        notifyItemChanged(position)
    }
}