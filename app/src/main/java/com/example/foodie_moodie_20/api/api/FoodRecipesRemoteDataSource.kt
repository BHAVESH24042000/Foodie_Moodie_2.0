package com.example.foodie_moodie_20.api.api

import com.example.foodiepoodie.dataModels.FoodRecipe
import retrofit2.Response
import javax.inject.Inject


class FoodRecipesRemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

   // private val foodRecipesApi: FoodRecipesApi = FoodRecipesNetworkModule.api

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

}