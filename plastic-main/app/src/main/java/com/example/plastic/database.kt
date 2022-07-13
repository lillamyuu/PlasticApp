package com.example.plastic

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class DBHelper(val context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists " + TA)
        onCreate(db)
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
        "create table if not exists " + TA + " (" + TA_ID
                + " integer primary key, " + TA_NAME + " text not null, "
                + TA_LAT + " real not null, " + TA_LONG + " real not null, " + TA_TIME + " text); "
    )
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS place_complect " +
                    "(place_id INTEGER, complect_id INTEGER, " +
                    "PRIMARY KEY(place_id, complect_id), " +
                    "FOREIGN KEY (place_id) REFERENCES place(place_id));"
        )

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS complect_code " +
                    "(complect_id INTEGER, code_id INTEGER, " +
                    "PRIMARY KEY(complect_id, code_id));"
        )
}

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "plasticDB"
        const val TA = "place"
        const val TA_ID = "place_id"
        const val TA_NAME = "name"
        const val TA_LAT = "latitude"
        const val TA_LONG = "longitude"
        const val TA_TIME = "timetable"

        const val TPC = "place_complect"
        const val TPC_CID = "complect_id"
        const val TPC_PID = "place_id"

        const val TCC = "complect_code"
        const val TCC_CID = "complect_id"
        const val TCC_ID = "code_id"



        const val ASSETS_PATH = "databases"

    }
}
fun SQLiteDatabase.getdata(): MutableList<Place>{
    val places = mutableListOf<Place>()
    val cursor: Cursor = this.query(DBHelper.TA, null, null, null, null, null, null)
    if (cursor.moveToFirst()){
        val idIndex: Int = cursor.getColumnIndex(DBHelper.TA_ID)
        val nameIndex: Int = cursor.getColumnIndex(DBHelper.TA_NAME)
        val latIndex: Int = cursor.getColumnIndex(DBHelper.TA_LAT)
        val longIndex: Int = cursor.getColumnIndex(DBHelper.TA_LONG)
        val timeIndex: Int = cursor.getColumnIndex(DBHelper.TA_TIME)
        do {
            if (cursor.getInt(idIndex) in places.map{it.place_id}) continue
            val t = Place(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getDouble(latIndex), cursor.getDouble(longIndex), cursor.getString(timeIndex))
            places.add(t)
            Log.d("mLog", t.latitude.toString()+" "+t.longitude.toString())

        } while (cursor.moveToNext())
    } else Log.d("mLog", "0 rows")
    return places

}

fun SQLiteDatabase.getdata(places: MutableList<Place>, places_ids: MutableSet<Int>){
    for (id in places_ids){
        val cursor: Cursor = this.rawQuery("select * from place where place_id = "+id.toString(), null)
        if(cursor.moveToFirst()){
            val idIndex: Int = cursor.getColumnIndex(DBHelper.TA_ID)
            val nameIndex: Int = cursor.getColumnIndex(DBHelper.TA_NAME)
            val latIndex: Int = cursor.getColumnIndex(DBHelper.TA_LAT)
            val longIndex: Int = cursor.getColumnIndex(DBHelper.TA_LONG)
            val timeIndex: Int = cursor.getColumnIndex(DBHelper.TA_TIME)
            val t = Place(cursor.getInt(idIndex), cursor.getString(nameIndex), cursor.getDouble(latIndex), cursor.getDouble(longIndex), cursor.getString(timeIndex))
            places.add(t)
        }
    }
}


//fun SQLiteDatabase.getdata(places_ids: MutableSet<Int>, id: Int){
//    val cursor: Cursor = this.rawQuery("select place_id from "+DBHelper.TPC+" where code_id = "+id.toString(), null)
//    if (cursor.moveToFirst()){
//        val placeIndex: Int = cursor.getColumnIndex(DBHelper.TPC_PLACE)
//        do {
//            val t = cursor.getInt(placeIndex)
//            places_ids.add(t)
//        } while (cursor.moveToNext())
//}}

//fun SQLiteDatabase.getdata(places_ids: MutableSet<Int>, codes_ids: MutableList<Int>){
//    var iter = mutableSetOf<Int>()
//    for (id in codes_ids){
//        var res = mutableSetOf<Int>()
//        this.getdata(res, id)
//        Log.d("mLog", (res).toString())
//
//        if (iter.size==0){
//            iter.addAll(res)
//            Log.d("mLog", iter.toString())
//        }
//        else{
//            iter = (iter intersect res) as MutableSet<Int>
//            //Log.d("mLog", iter.toString())
//        }
//        //Log.d("mLog", places_ids.toString())
//
//    }
//    places_ids.addAll(iter)
//
//}

fun SQLiteDatabase.getmax():Int {
    var res = 0

    val cursor: Cursor = this.rawQuery("SELECT MAX("+DBHelper.TCC_CID+") from "+DBHelper.TCC, null)
    if (cursor.moveToFirst()) {
        val idIndex: Int = cursor.getColumnIndex(DBHelper.TCC_CID)
        do {
            res = cursor.getInt(idIndex)
        } while (cursor.moveToNext())

    }
    return res
}
fun SQLiteDatabase.setdata(p: Place){
    val contentValues = ContentValues()
    contentValues.put(DBHelper.TA_ID, p.place_id)
    contentValues.put(DBHelper.TA_NAME, p.name)
    contentValues.put(DBHelper.TA_LAT, p.latitude)
    contentValues.put(DBHelper.TA_LONG, p.longitude)
    contentValues.put(DBHelper.TA_TIME, p.time)

    this.insert(DBHelper.TA, null, contentValues)
}
fun SQLiteDatabase.setdata(places: PlaceList){
    val db_places = this.getdata().map{it.place_id}
    for (place in places.items){
        if (place.place_id in db_places) continue
        this.setdata(place)
    }
}

fun SQLiteDatabase.setresult(){
    val places = PlaceResultPlace.getInstance()
    val codes = CodesResultId.getInstance()
    this.setdata(places)
    val contentValuesPlace = ContentValues()
    val contentValuesCode = ContentValues()
    val size = 0
    contentValuesCode.put(DBHelper.TCC_CID, size+1)
    contentValuesPlace.put(DBHelper.TPC_CID, size+1)
    for (p in places.items.map{it.place_id}){
        contentValuesPlace.put(DBHelper.TPC_PID, p)
        this.insert(DBHelper.TPC, null, contentValuesPlace)
    }
    for (c in codes){
        contentValuesCode.put(DBHelper.TCC_ID, c)
        this.insert(DBHelper.TCC, null, contentValuesCode)
    }
}
fun SQLiteDatabase.getcodesid(complect: Int):MutableList<Int>{
    val codes = mutableListOf<Int>()
    val cursor: Cursor = this.rawQuery("select code_id from"+
            DBHelper.TCC+" where complect_id = "+complect.toString(), null)
        if(cursor.moveToFirst()){
            val idIndex: Int = cursor.getColumnIndex(DBHelper.TCC_ID)
            do {
                codes.add(cursor.getInt(idIndex))
            } while (cursor.moveToNext())
        }
    return codes
}

@RequiresApi(Build.VERSION_CODES.N)
fun SQLiteDatabase.getallcodes():MutableList<String>{
    val codes = mapOf<Int, MutableList<String>>()
    val cursor: Cursor = this.rawQuery("select code_id, complect_id from "+
            DBHelper.TCC, null)
    if(cursor.moveToFirst()){
        val codeIndex: Int = cursor.getColumnIndex(DBHelper.TCC_ID)
        val complectIndex: Int = cursor.getColumnIndex(DBHelper.TCC_CID)
        do {
            val comp = cursor.getInt(complectIndex)
            codes.getOrDefault(comp, mutableListOf())
            codes[comp]!!.add(ClassNames.getClass(cursor.getInt(codeIndex)))
        } while (cursor.moveToNext())
    }
    val res = codes.map{it.value.toString()}.map{it.slice(1..it.length)} as MutableList<String>
    return res

}


fun SQLiteDatabase.getplacesid(complect: Int):MutableList<Int>{
    val places = mutableListOf<Int>()
    val cursor: Cursor = this.rawQuery("select place_id from"+
            DBHelper.TPC+" where complect_id = "+complect.toString(), null)
    if(cursor.moveToFirst()){
        val idIndex: Int = cursor.getColumnIndex(DBHelper.TPC_PID)
        do {
            places.add(cursor.getInt(idIndex))
        } while (cursor.moveToNext())
    }
    return places
}
fun SQLiteDatabase.deletedata(id: Int){
    this.delete(DBHelper.TA, "_id ="+id.toString(), null)
}

fun SQLiteDatabase.clear(){
    this.execSQL("delete from "+DBHelper.TA)
}

