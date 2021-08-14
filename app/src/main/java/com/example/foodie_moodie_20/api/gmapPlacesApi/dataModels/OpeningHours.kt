package com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels


import com.google.gson.annotations.SerializedName

data class OpeningHours(
    @SerializedName("open_now")
    val openNow: Boolean
)