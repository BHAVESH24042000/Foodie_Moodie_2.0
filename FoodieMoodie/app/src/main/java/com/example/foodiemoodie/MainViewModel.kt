package com.example.foodiemoodie

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodiemoodie.api.NetworkResult
import com.example.foodiepoodie.dataModels.FoodRecipe
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repository=Repository()
    var recipesResponse:MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries:Map<String,String>)=viewModelScope.launch{
        getRecipesSafeCall(queries)
    }

   suspend private fun getRecipesSafeCall(queries: Map<String, String> ) {

       if(hasInternetConnection(getApplication() )){
          try {
              val response= repository.remote.getRecipes(queries)
              recipesResponse.value=handleFoodRecipesResponse(response)

          } catch (e:Exception){
           recipesResponse.value=NetworkResult.Error("Recipes not found ")
          }
       }else{
             recipesResponse.value=NetworkResult.Error("No Internet Connection")
       }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Calls Limit")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

     fun hasInternetConnection(context:Context): Boolean {

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