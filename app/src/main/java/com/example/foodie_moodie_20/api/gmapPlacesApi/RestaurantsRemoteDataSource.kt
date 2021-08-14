package com.example.foodie_moodie_20.api.gmapPlacesApi

import android.media.Image
import com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels.ResultRestrau

import retrofit2.Response

class RestaurantsRemoteDataSource {

    private val restaurantsApi: RestaurantsApi = RestaurantsNetworkModule.Restaurantsapi

    suspend fun getRestrauByCurrentLocation(location :String?, radius:Int?, type:String?, key:String? ): Response<ResultRestrau?> {
        return  restaurantsApi.getRestrauByCurrentLocation(location, radius, type, key)
    }

    suspend fun getRestrauBySearch(query :String? , key: String?): Response<ResultRestrau?> {
        return restaurantsApi.getRestrauBySearch(query, key)
    }

    suspend fun getphotoRestrau(width :Int?,reference:String?, key:String?): Image? {
        return restaurantsApi.getphotoRestrau(width, reference, key)
    }


}