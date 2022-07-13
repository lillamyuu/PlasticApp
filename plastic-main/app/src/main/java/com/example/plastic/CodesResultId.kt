package com.example.plastic

import android.util.Log

object CodesResultId {
    private val codes = mutableSetOf<Int>()
    fun addCode(code: Int){
        codes.add(code)
    }
    fun addCodes(code: MutableList<Int>){

        codes.addAll(code)
    }
    fun getInstance():MutableList<Int>{
        return codes.toMutableList()
    }
}

object CodeResultCode{
    private var codes = mutableSetOf<Code>()
    fun addCode(code: Code){
        Log.d("mlog", "YE")
        if (code.code_id !in CodesResultId.getInstance()) {
            codes.add(code)
            CodesResultId.addCode(code.code_id)
        }
    }
    fun getInstance():MutableList<Code>{
        return codes.toMutableList()
    }
    fun decode(){
        codes.forEach{ it.name = String(it.name.toByteArray(), Charsets.UTF_8) }
    }

}

object PlaceResultId{
    private var places = mutableSetOf(1,2,3,4,5,6,7,8)

    fun addPlaces(place: MutableList<Int>?){
        if (!place.isNullOrEmpty()){
            places = places.intersect(place) as MutableSet<Int>
        }
    }
    fun getInstance():MutableList<Int>{
        return places.toMutableList()
    }
}

object PlaceResultPlace{
    lateinit var places: PlaceList
    fun createList(placelist: PlaceList){
        places = PlaceList(placelist.items)
    }
    fun getInstance(): PlaceList{
        return places
    }
}