package com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels


import com.google.gson.annotations.SerializedName

data class ResultRestrau(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("results")
    val results: List<ResultX?>?,
    @SerializedName("status")
    val status: String
)