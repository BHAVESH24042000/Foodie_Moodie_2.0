package com.example.foodie_moodie_20.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_moodie_20.R
import com.example.foodie_moodie_20.api.gmapPlacesApi.RestaurantsRemoteDataSource
import com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels.ResultX
import com.example.foodie_moodie_20.databinding.RestaurantsRowLayoutBinding
import com.example.foodie_moodie_20.viewModels.RestaurantsViewModel
import kotlinx.coroutines.*


val restrauRemoteDataSource = RestaurantsRemoteDataSource()
private lateinit var viewModel: RestaurantsViewModel


class RestaurantsAdapter  : ListAdapter<ResultX, RestaurantsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ResultX>() {
        override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX): Boolean {
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
                R.layout.restaurants_row_layout,
                parent,
                false
            )
        )
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        viewModel =
            ViewModelProvider((recyclerView.context as ViewModelStoreOwner)!!)[RestaurantsViewModel::class.java]
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        //viewModel= ViewModelProvider(this).get(RestaurantsViewModel::class.java)
        RestaurantsRowLayoutBinding.bind(holder.itemView).apply{

            val singleRestaurant = getItem(position)

          if( singleRestaurant.photos?.get(0)?.width != null && singleRestaurant.photos.get(0)?.photoReference != null) {

            viewModel.getphotoRestrau(
                singleRestaurant.photos?.get(0)?.width ,
                singleRestaurant.photos.get(0)?.photoReference,
                restarImageView
            )
            }

           // restarImageView.loadImage( singleRestaurant.photos?.get(0)?.photoReference)//.photoReference)
            restarName.text = singleRestaurant.name.toString()
            restarVicinity.text = singleRestaurant.vicinity
            restarRating.text = "Rating :" + singleRestaurant.rating.toString()

           /*
            root.setOnClickListener { view->

                val action = RecipiesFragmentDirections.actionRecipiesFragmentToDetailsActivity(singleRecipe)
                view.findNavController().navigate(action)
            }*/


        }
    }

}

