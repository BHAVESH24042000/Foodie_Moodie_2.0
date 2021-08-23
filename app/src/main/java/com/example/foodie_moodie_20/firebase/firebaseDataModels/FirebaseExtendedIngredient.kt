package com.example.foodie_moodie_20.firebase.firebaseDataModels


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class FirebaseExtendedIngredient(

    var amount: Double? = null,
    var consistency: String? = null,
    var id: Int?= null,
    var image: String?= null,
    var name: String?= null,
    var original: String?= null,
    var unit: String?= null

){

}