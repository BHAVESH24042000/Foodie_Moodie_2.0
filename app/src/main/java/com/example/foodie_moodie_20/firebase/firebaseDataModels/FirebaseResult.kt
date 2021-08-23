package com.example.foodie_moodie_20.firebase.firebaseDataModels


data class FirebaseResult(
    val extendedIngredients: MutableList<FirebaseExtendedIngredient>? = null,
    val id: Int? = null,
    val image: String? = null,
    val readyInMinutes: Int? = null,
    val summary: String? = null,
    val vegan: Boolean? = null,
    val aggregateLikes: Int? = null,
    val title: String? = null,
    val userID: String = " ",
    val userANDrecipe:String? = null,
    val sourceUrl: String? = " ",
    val documentId: String = " ",


    ){

}