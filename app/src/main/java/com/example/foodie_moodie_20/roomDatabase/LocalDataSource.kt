package com.example.foodie_moodie_20.roomDatabase

import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao

import com.example.foodie_moodie_20.roomDatabase.entities.FavouritesEntity
import com.example.foodie_moodie_20.roomDatabase.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDbDataSource  @Inject constructor( private val recipesDao: RecipesDao)
{


    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }


    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }
    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity){
        recipesDao.insertFavoriteRecipe(favouritesEntity)
    }

    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity){
        recipesDao.deleteFavoriteRecipe(favouritesEntity)
    }

    suspend fun deleteAllFavouriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
}