package com.example.foodie_moodie_20.roomDatabase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie_moodie_20.utils.Constants.Companion.FAVORITE_RECIPES_TABLE
import com.example.foodiepoodie.dataModels.Result


@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)