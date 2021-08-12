package com.example.foodie_moodie_20.roomDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodie_moodie_20.roomDatabase.dao.RecipesDao
import com.example.foodie_moodie_20.roomDatabase.entities.FavouritesEntity
import com.example.foodie_moodie_20.roomDatabase.entities.RecipesEntity
import com.example.foodie_moodie_20.utils.Constants


@Database(
    entities = [RecipesEntity::class, FavouritesEntity::class], //FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

   companion object DatabaseModule
    {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RecipesDatabase? = null

        fun provideDatabase(context: Context):RecipesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    RecipesDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        fun provideDao(database: RecipesDatabase) = database.recipesDao()
    }

}