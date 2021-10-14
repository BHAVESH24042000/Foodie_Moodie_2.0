package com.example.foodie_moodie_20.repository



import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao
import com.example.foodie_moodie_20.api.api.FoodRecipesRemoteDataSource
import com.example.foodie_moodie_20.roomDatabase.RoomDbDataSource
import javax.inject.Inject

class Repository @Inject constructor(
    remoteDataSource: FoodRecipesRemoteDataSource,
    localDataSource: RoomDbDataSource
    ){

    val recipeRemote= remoteDataSource
    val local=localDataSource

}