package com.example.foodie_moodie_20.api.spoonacularFood

import com.example.foodiepoodie.dataModels.FoodRecipe
import retrofit2.Response


class FoodRecipesRemoteDataSource {

    private val foodRecipesApi: FoodRecipesApi = FoodRecipesNetworkModule.api

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

}