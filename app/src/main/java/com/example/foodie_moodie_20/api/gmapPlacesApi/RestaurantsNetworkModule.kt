package com.example.foodie_moodie_20.api.gmapPlacesApi

import com.example.foodie_moodie_20.utils.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RestaurantsNetworkModule {

    val okHttpBuilder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)

    var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URLMaps)
        .addConverterFactory(GsonConverterFactory.create())

    val Restaurantsapi = retrofitBuilder
        .client(okHttpBuilder.build())
        .build()
        .create(RestaurantsApi::class.java)
}