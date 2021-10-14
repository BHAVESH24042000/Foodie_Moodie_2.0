package com.example.foodie_moodie_20.api.api

import com.example.foodie_moodie_20.utils.Constants.Companion.BASE_URLRecipie

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




object FoodRecipesNetworkModule {


    val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(2, TimeUnit.SECONDS)

    val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URLRecipie)
            .addConverterFactory(GsonConverterFactory.create())

    val api = retrofitBuilder
            .client(okHttpBuilder.build())
            .build()
            .create(FoodRecipesApi::class.java)
}