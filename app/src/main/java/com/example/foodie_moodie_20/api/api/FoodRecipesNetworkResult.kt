package com.example.foodie_moodie_20.api.api

sealed class FoodRecipesNetworkResult<T>(
        val data: T? = null,
        val message: String? = null
) {

    class Success<T>(data: T?) : FoodRecipesNetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : FoodRecipesNetworkResult<T>(data, message)
    class Loading<T> : FoodRecipesNetworkResult<T>()
}