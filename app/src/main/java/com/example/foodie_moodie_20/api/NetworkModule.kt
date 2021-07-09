package com.example.foodiemoodie.API

import com.example.foodie_moodie_20.utils.Constants.Companion.BASE_URL

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




object NetworkModule {


    val okHttpBuilder = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(2, TimeUnit.SECONDS)

    val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    val api = retrofitBuilder
            .client(okHttpBuilder.build())
            .build()
            .create(FoodRecipesApi::class.java)
}