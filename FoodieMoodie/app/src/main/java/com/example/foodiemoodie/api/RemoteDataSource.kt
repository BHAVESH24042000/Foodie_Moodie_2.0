package com.example.foodiemoodie.api

import com.example.foodiepoodie.dataModels.FoodRecipe
import retrofit2.Response

class RemoteDataSource {

    private val foodRecipesApi: FoodRecipesApi =NetworkModule.api

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

  /*  suspend fun getFoodJoke(apikey: String): Response<FoodJoke> {
        return foodRecipesApi.getFoodJoke(apikey)
    }*/
}