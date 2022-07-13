package com.example.plastic
import retrofit2.Call;
import retrofit2.http.*


interface DBService {
    @POST("./getPlaces")
    @Headers("Content-Type: application/json; charset=utf8")
    fun getPlaces(@Body place: Place): Call<PlaceList>

    @POST("/getPlacesByCode")
    @Headers("Content-Type: application/json; charset=utf8")
    fun getPlacesByCode(@Body code: Code): Call<PlaceList>

    @POST("/getCodes")
    @Headers("Content-Type: application/json; charset=utf8")
    fun getCodes(@Body code: Code): Call<CodeList>

    @POST("/getPlacesByCodelist")
    @Headers("Content-Type: application/json; charset=utf8")
    fun getPlacesByCodelist(@Body codeList: CodeList): Call<PlaceList>

    @POST("/getPlacesByPlaceList")
    @Headers("Content-Type: application/json; charset=utf8")
    fun getPlacesByPlacelist(@Body placeList: PlaceListid): Call<PlaceList>
}