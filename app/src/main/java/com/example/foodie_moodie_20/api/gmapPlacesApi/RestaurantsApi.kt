package com.example.foodie_moodie_20.api.gmapPlacesApi


import android.media.Image
import com.example.foodie_moodie_20.api.gmapPlacesApi.dataModels.ResultRestrau
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RestaurantsApi {

    //    /nearbysearch/json?location=28.627562,%2077.278404&radius=1000&type=restaurant&key=
@GET( "nearbysearch/json")
suspend fun getRestrauByCurrentLocation(
        @Query("location") location:String?=null,
        @Query("radius") radius:Int?=null,
        @Query("type")  type :String ?=null,
        @Query("key") key : String ?=null

) : Response<ResultRestrau?>

//  https://maps.googleapis.com/maps/api/place/textsearch/json?query=restaurants+in+shahdara&key=AIzaSyDJWdTrVeRwjK4lsL5lW3yhl2Qg-xh9GtU

    @GET( "textsearch/json")
    suspend fun getRestrauBySearch(
        @Query("query") location:String?=null,
        @Query("key") key : String ?=null

    ) : Response<ResultRestrau?>

    //   https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9ULlBU&key=YOUR_API_KEY
    @GET("photo")
    suspend fun getphotoRestrau(
       @Query("maxwidth") width :Int? =null,
       @Query("photoreference") reference :String? = null,
       @Query("key") key : String ?=null
    ): Image?

}