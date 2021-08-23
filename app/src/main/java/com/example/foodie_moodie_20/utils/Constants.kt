package com.example.foodie_moodie_20.utils

class Constants {

    companion object {
        const val BASE_URLRecipie = "https://api.spoonacular.com"
        const val BASE_URLMaps = "https://maps.googleapis.com/maps/api/place/"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val API_KEY = ""
        const val API_KEYmaps = ""
        const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 3
        const val RECIPE_RESULT_KEY = "recipeBundle"
        const val IMAGE_URI: String="imageURI"
        const val IMAGE_URL: String="imageURL"
        const val PRIVACY: String="privacy"
        const val LONGITUDE: String="longitude"
        const val LATITUDE: String="latitude"
        const val RATING: String="rating"
        const val LOCATION: String="location"
        const val DATE: String="date"
        const val CITY_NAME: String="cityName"
        const val DESCRIPTION = "description"
        const val TITLE = "title"

        const val PUBLIC = "public"
        const val PRIVATE = "private"

        const val FIRST_NAME="firstName"
        const val USER_NAME="userName"

        const val USERS = "users"
        const val MEMORIES = "memories"

        const val EXTRA_USER_DETAILS = "extraUserDetails"
        const val MEMORY_DETAILS = "memory_details"

        const val CAMERA = 1
        const val GALLERY = 2


        const val IMAGE_NAME_IN_CLOUD = "memory_image"

        const val USERID = "userID"
        const val MEMORY_ID = "memoryID"

        const val PASS_MEMORY = "pass_memory"


        // API Query Keys
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"
        const val FAVORITE_RECIPES_TABLE = "favorite_recipes_table"
        const val FOOD_JOKE_TABLE = "food_joke_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_RECIPES_NUMBER = "100"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foody_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"

    }
}