package com.example.plastic
import com.google.gson.annotations.SerializedName;

data class Place (
    @SerializedName("place_id")
    var place_id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("longitude")
    var longitude: Double,

    @SerializedName("latitude")
    var latitude: Double,

    @SerializedName("timatable")
    var time: String
)

data class PlaceList(var items: List<Place>)
data class PlaceListid(var items: List<Int>)

data class Code(
    @SerializedName("code_id")
    var code_id: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("info")
    var info: String

)
data class CodeList(var items: List<Code>)