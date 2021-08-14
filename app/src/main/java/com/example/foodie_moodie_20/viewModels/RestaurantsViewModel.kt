package com.example.foodie_moodie_20.viewModels

import android.app.Application
import android.content.Context
import android.media.Image
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels.ResultRestrau
import com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels.ResultX
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesNetworkResult
import com.example.foodie_moodie_20.roomDatabase.RecipesDatabase
import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao
import com.example.foodie_moodie_20.utils.Constants.Companion.API_KEYmaps
import com.example.foodie_moodie_20.utils.Repository
import com.example.foodiemoodie.adapters.loadImage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class RestaurantsViewModel(application: Application): AndroidViewModel(application) {

    val db by lazy{
        RecipesDatabase.provideDatabase(getApplication())
    }

    var recipesdao : RecipesDao =db.recipesDao()
    private val repository = Repository(recipesdao)

    var restaurantResponseCurrentLocation: MutableLiveData<List<ResultX?>?> = MutableLiveData()
    var restaurantResponseSearched: MutableLiveData<List<ResultX?>?> = MutableLiveData()
    var locationString : MutableLiveData<String?> = MutableLiveData()

    fun getRestrauByCurrentLocation(location :String?,
                                    radius:Int?,
                                    type:String?, key:String? )=viewModelScope.launch{

        if (hasInternetConnection(getApplication())) {
            try {
                restaurantResponseCurrentLocation.postValue(repository.restauranRemote.getRestrauByCurrentLocation(location, radius, type, key).body()?.results)
            } catch (e: Exception) {
               Toast.makeText(getApplication(), "Restaurants Not Found", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_LONG).show()
        }

    }

    fun getRestrauBySearch(query :String?, key:String?) = viewModelScope.launch {

        if (hasInternetConnection(getApplication())) {
            try {
                restaurantResponseSearched.value = repository.restauranRemote.getRestrauBySearch(query,key).body()?.results
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Restaurants Not Found", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_LONG).show()
        }
    }

    fun getphotoRestrau(width :Int?,reference:String?, Imageview : ImageView) = viewModelScope.launch {
        Picasso.get()
            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=$width&photoreference=$reference&key=$API_KEYmaps")

            .into(Imageview)

     // Imageview( repository.restauranRemote.getphotoRestrau(width, reference, API_KEYmaps)

    }



    fun hasInternetConnection(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

}