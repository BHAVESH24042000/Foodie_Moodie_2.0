package com.example.foodie_moodie_20.utils

import com.example.foodie_moodie_20.database.LocalDataSource
import com.example.foodie_moodie_20.database.dao.RecipesDao
import com.example.foodiemoodie.API.RemoteDataSource

class Repository (var recipesDao: RecipesDao){

    val remote=RemoteDataSource()
    val local=LocalDataSource(recipesDao)

}