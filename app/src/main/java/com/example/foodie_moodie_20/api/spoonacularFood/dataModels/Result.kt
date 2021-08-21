package com.example.foodiepoodie.dataModels


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Result(

    @SerializedName("extendedIngredients")
    val extendedIngredients: @RawValue List<ExtendedIngredient>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("vegetarian")
    val vegetarian: Boolean?,
    @SerializedName("cheap")
    val cheap: Boolean?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?,
    @SerializedName("glutenFree")
    val glutenFree: Boolean?,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?,


   /* @SerializedName("dairyFree")
    val dairyFree: Boolean?,
    @SerializedName("diets")
    val diets: List<String>?,
    @SerializedName("dishTypes")
    val dishTypes: List<String>?,

    @SerializedName("gaps")
    val gaps: String?,
    @SerializedName("glutenFree")
    val glutenFree: Boolean?,
    @SerializedName("healthScore")
    val healthScore: Double?,

    @SerializedName("imageType")
    val imageType: String?,
    @SerializedName("license")
    val license: String?,

    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?,
    @SerializedName("missedIngredientCount")
    val missedIngredientCount: Int?,


    @SerializedName("preparationMinutes")
    val preparationMinutes: Int?,
    @SerializedName("pricePerServing")
    val pricePerServing: Double?,

    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceName")
    val sourceName: String?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("spoonacularScore")
    val spoonacularScore: Double?,
    @SerializedName("spoonacularSourceUrl")
    val spoonacularSourceUrl: String?,

    @SerializedName("sustainable")
    val sustainable: Boolean?,

    @SerializedName("unusedIngredients")
    val unusedIngredients: @RawValue List<Any>?,
    @SerializedName("usedIngredientCount")
    val usedIngredientCount: Int?,
    @SerializedName("usedIngredients")
    val usedIngredients: @RawValue List<Any>?,

    @SerializedName("vegetarian")
    val vegetarian: Boolean?,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?,
    @SerializedName("veryPopular")
    val veryPopular: Boolean?,
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?*/
):Parcelable