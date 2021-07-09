package com.example.foodie_moodie_20.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie_moodie_20.utils.Constants.Companion.RECIPES_TABLE
import com.example.foodiepoodie.dataModels.FoodRecipe

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}