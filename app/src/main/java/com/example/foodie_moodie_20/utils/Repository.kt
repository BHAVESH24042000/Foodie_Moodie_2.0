package com.example.foodie_moodie_20.utils

import com.example.foodie_moodie_20.api.gmapPlacesApi.RestaurantsRemoteDataSource
import com.example.foodie_moodie_20.roomDatabase.LocalDataSource
import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao
import com.example.foodie_moodie_20.api.spoonacularFood.FoodRecipesRemoteDataSource

class Repository (var recipesDao: RecipesDao){

    val recipeRemote= FoodRecipesRemoteDataSource()
    val restauranRemote = RestaurantsRemoteDataSource()
    val local=LocalDataSource(recipesDao)

}