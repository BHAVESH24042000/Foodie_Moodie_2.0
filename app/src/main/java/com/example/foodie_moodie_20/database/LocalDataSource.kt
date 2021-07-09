package com.example.foodie_moodie_20.database

import com.example.foodie_moodie_20.database.dao.RecipesDao

import com.example.foodie_moodie_20.database.entities.FavouritesEntity
import com.example.foodie_moodie_20.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource( var recipesDao: RecipesDao)
{
    //private val database:RecipesDatabase = DatabaseModule.provideDatabase( this)

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