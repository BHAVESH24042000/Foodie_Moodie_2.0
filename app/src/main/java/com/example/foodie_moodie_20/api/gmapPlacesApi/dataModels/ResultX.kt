package com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels


import com.google.gson.annotations.SerializedName

data class ResultX(
    @SerializedName("business_status")
    val businessStatus: String,

    @SerializedName("name")
    val name: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHours,
    @SerializedName("permanently_closed")
    val permanentlyClosed: Boolean,
    @SerializedName("photos")
    val photos: List<Photo?>?,

    @SerializedName("price_level")
    val priceLevel: Int,
    @SerializedName("rating")
    val rating: Double,

    @SerializedName("types")
    val types: List<String>,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("vicinity")
    val vicinity: String
)