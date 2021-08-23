package com.example.foodie_moodie_20.firebase.ViewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodie_moodie_20.DataStoreRepository
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesNetworkResult
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesRemoteDataSource
import com.example.foodie_moodie_20.utils.Constants
import com.example.foodiepoodie.dataModels.FoodRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class FirebaseRecipesViewModel(application: Application): AndroidViewModel(application) {

    var recipesResponse: MutableLiveData<FoodRecipesNetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<FoodRecipesNetworkResult<FoodRecipe>> = MutableLiveData()
    private var mealType = Constants.DEFAULT_MEAL_TYPE
    private var dietType = Constants.DEFAULT_DIET_TYPE

    val dataStoreRepository= DataStoreRepository(application)
    val readMealAndDietType= dataStoreRepository.readMealAndDietType

    fun getRecipes(queries:Map<String,String>)=viewModelScope.launch{
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    suspend private fun getRecipesSafeCall(queries: Map<String, String> ) {

        if(hasInternetConnection(getApplication() )){
            try {
                val response= FoodRecipesRemoteDataSource().getRecipes(queries)

                recipesResponse.postValue(handleFoodRecipesResponse(response))


            } catch (e: Exception){
                recipesResponse.value= FoodRecipesNetworkResult.Error("Recipes not found ")
            }
        }else{
            recipesResponse.value= FoodRecipesNetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = FoodRecipesNetworkResult.Loading()
        if (hasInternetConnection(getApplication())) {
            try {
                val response = FoodRecipesRemoteDataSource().searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipesResponse.value = FoodRecipesNetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = FoodRecipesNetworkResult.Error("No Internet Connection.")
        }
    }


    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): FoodRecipesNetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return FoodRecipesNetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return FoodRecipesNetworkResult.Error("API Calls Limit")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return FoodRecipesNetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return FoodRecipesNetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return FoodRecipesNetworkResult.Error(response.message())
            }
        }
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



    fun saveMealAndDietType(mealType:String, mealTypeId:Int, dietType:String, dietTypeId:Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(
                mealType, mealTypeId, dietType, dietTypeId
            )
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch{
            readMealAndDietType.collect { value->
                mealType=value.selectedMealType
                dietType=value.selectedDietType

            }
        }

        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_TYPE] = mealType
        queries[Constants.QUERY_DIET] = dietType
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_SEARCH] = searchQuery
        queries[Constants.QUERY_NUMBER] = Constants.DEFAULT_RECIPES_NUMBER
        queries[Constants.QUERY_API_KEY] = Constants.API_KEY
        queries[Constants.QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[Constants.QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

}