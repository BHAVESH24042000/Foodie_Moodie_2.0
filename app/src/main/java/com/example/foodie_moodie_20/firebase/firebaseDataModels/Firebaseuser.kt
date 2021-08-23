package com.example.foodie_moodie_20.firebase.firebaseDataModels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class Firebaseuser(
    val userID : String = " ",
    val firstName : String = " ",
    val lastName : String = " ",
    val email : String =" ",
    val password: String=" ",

){

}