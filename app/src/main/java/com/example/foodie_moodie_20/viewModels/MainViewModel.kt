package com.example.foodie_moodie_20.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import androidx.lifecycle.*
import com.example.foodie_moodie_20.roomDatabase.RecipesDatabase
import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao

import com.example.foodie_moodie_20.roomDatabase.entities.FavouritesEntity
import com.example.foodie_moodie_20.roomDatabase.entities.RecipesEntity
import com.example.foodie_moodie_20.api.api.FoodRecipesNetworkResult
import com.example.foodie_moodie_20.repository.Repository
import com.example.foodiepoodie.dataModels.FoodRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: Repository,
    application: Application): AndroidViewModel(application) {

    val db by lazy {
        RecipesDatabase.provideDatabase(getApplication())
    }

    var recipesdao :RecipesDao=db.recipesDao()
   // private val repository = Repository(recipesdao)


    /*ROOM - OFFLINE CACHING*/

    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    val readFavouriteRecipes: LiveData<List<FavouritesEntity>> = repository.local.readFavouriteRecipes().asLiveData()


   suspend fun insertRecipes(recipeEntity: RecipesEntity) = viewModelScope.launch(IO) {
        repository.local.insertRecipes(recipeEntity)
    }

    fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity) = viewModelScope.launch(IO) {
        repository.local.insertFavouriteRecipes(favouritesEntity)
    }

    fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity) = viewModelScope.launch(IO) {
        repository.local.deleteFavouriteRecipe(favouritesEntity)
   }

    fun deleteAllFavouriteRecipes() = viewModelScope.launch(IO) {
        repository.local.deleteAllFavouriteRecipes()
    }

    /*RETROFIT*/

    var recipesResponse:MutableLiveData<FoodRecipesNetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<FoodRecipesNetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries:Map<String,String>)=viewModelScope.launch{
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

   suspend private fun getRecipesSafeCall(queries: Map<String, String> ) {

       if(hasInternetConnection(getApplication() )){
          try {
              val response= repository.recipeRemote.getRecipes(queries)

              recipesResponse.postValue(handleFoodRecipesResponse(response))

              viewModelScope.launch(Main) {
                  val foodRecipe = recipesResponse.value?.data
                  if (foodRecipe != null) {
                      offlineCacheRecipes(foodRecipe)
                  }
              }

          } catch (e:Exception){
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
                val response = repository.recipeRemote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                searchedRecipesResponse.value = FoodRecipesNetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = FoodRecipesNetworkResult.Error("No Internet Connection.")
        }
    }

    suspend private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)

        viewModelScope.launch(IO) {
            insertRecipes(recipesEntity)
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