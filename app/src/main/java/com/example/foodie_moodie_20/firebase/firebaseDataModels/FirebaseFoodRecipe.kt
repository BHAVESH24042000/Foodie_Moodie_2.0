package com.example.foodie_moodie_20.firebase.firebaseDataModels


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class FirebaseFoodRecipe(

    val number: Int?= null,
    val results: List<FirebaseResult?>?= null,
    val totalResults: Int?= null,
    val userID : String = " ",
){

}