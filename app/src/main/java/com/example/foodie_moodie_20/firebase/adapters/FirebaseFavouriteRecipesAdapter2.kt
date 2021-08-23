package com.example.foodie_moodie_20.firebase.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foodie_moodie_20.firebase.FirebaseRecipesFavouriteFragment
import com.example.foodie_moodie_20.firebase.firebaseDataModels.FirebaseResult
import com.example.foodiemoodie.adapters.loadImage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.personal_rv_item.view.*

class FirebaseFavouriteRecipesAdapter2 (private val context : Context, private var list : ArrayList<FirebaseResult>) :
    RecyclerView.Adapter<FirebaseFavouriteRecipesAdapter2 .MyViewHolder>(){

    class MyViewHolder ( val binding : FavoriteRecipesRowLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FavoriteRecipesRowLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

   // @SuppressLint("SetTextI18n")
   lateinit var singleRecipe : FirebaseResult
    lateinit var userANDrecipe : String
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //val model = list[position]
        FavoriteRecipesRowLayoutBinding.bind(holder.itemView).apply {

            singleRecipe = list[position]

            favoriteTitleTextView.text = singleRecipe.title
            favoriteDescriptionTextView.text = singleRecipe.summary
            favoriteHeartTextView.text = singleRecipe.aggregateLikes.toString()
            favoriteClockTextView.text = singleRecipe.readyInMinutes.toString()
            favoriteRecipeImageView.loadImage(singleRecipe.image)

            if (singleRecipe.vegan == true) {
                favoriteLeafTextView.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
                favoriteLeafImageView.setColorFilter(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.green
                    )
                )
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    private val mFireStore = FirebaseFirestore.getInstance()

    fun notifyDeleteItem(fragment: FirebaseRecipesFavouriteFragment, position: Int) {
        //fragment.showProgressDialog()
        userANDrecipe = list[position].userANDrecipe!!
        var delID :String? =null

        mFireStore.collection("savedRecipe")
            .whereEqualTo("userANDrecipe", userANDrecipe).get().addOnCompleteListener {  snapshot->

                Log.i("deleteid", "delreipe is : $snapshot")

                for (d in snapshot.result) {
                    Log.i(
                        "deleteid",
                        "Doc ID is : ${d.toObject(FirebaseResult::class.java).documentId}"
                    )
                    //println(d.toObject(FirebaseResult::class.java).documentId)
                    delID = d.toObject(FirebaseResult::class.java).documentId
                    Log.i("deleteid", "DelID1 is : ${delID}")
                }

                Log.i("deleteid", "DelID2 is : ${delID}")

                if(delID!=null) {

                    mFireStore.collection("savedRecipe").document("${delID}").delete().addOnCompleteListener {
                        Log.i("deleteid", "Document Deleted of Delete ID is : ${delID}")
                        Toast.makeText(fragment.requireActivity(), "Recipe Deleted", Toast.LENGTH_SHORT).show()
                        notifyItemRemoved(position)
                         fragment.deleteSuccessful()

                    }.addOnFailureListener{
                        Toast.makeText(fragment.requireActivity(), "Error in Recipe Deletion", Toast.LENGTH_SHORT).show()
                        Log.i("deleteid", "Document Deletion Error of Delete ID is : ${delID}")
                    }
                }
            }
        //fragment.hideProgressBar()

    }


}