package com.example.plastic

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Map : AppCompatActivity(), locListenerInterface {
    lateinit var mapv: MapView
    lateinit var dbhelper: DBHelper
    //location
    private lateinit var myLocListener: MyLocListener
    private lateinit var locationManager: LocationManager
    private lateinit var myLocation: Location
    private var myLatitude: Double = 4242424242.0
    private var myLongitude: Double = 4242424242.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        MapKitFactory.setApiKey("b5f26dea-b684-4bb6-974a-8bca769e3bf8")
        MapKitFactory.initialize(this)


        // getting access to the database with the ability to write




        setContentView(R.layout.activity_map)
        mapv = findViewById(R.id.mapview)
//        val view = View(requireContext()).apply {
//            background = requireContext().getDrawable(R.drawable.ic_place_24px)
//        }
        //mapv.map.mapObjects.addPlacemark(Point(64.55256209, 40.55594677))

        init()

//        database.getdata(places_ids, codes)
//        Log.d("mLog", places_ids.toString())
//        database.getdata(places, places_ids)
        val dbService = ServerConn.getInstance()

        val createCall = dbService.getPlacesByPlacelist(PlaceListid(PlaceResultId.getInstance()))
        createCall.enqueue(object: Callback<PlaceList> {
            override fun onFailure(call: Call<PlaceList>, t: Throwable) {
                Log.d("mlog", t.toString())
            }

            override fun onResponse(call: Call<PlaceList>, resp: Response<PlaceList>) {
                Log.d("mlog", resp.body().toString())
                resp.body()?.let { it -> PlaceResultPlace.createList(it) }
                for (p in PlaceResultPlace.getInstance().items){
                    Log.d("mLog", p.latitude.toString()+" "+p.longitude.toString())
                    mapv.map.mapObjects.addPlacemark(Point(p.longitude, p.latitude))
                }


            }
        })
        mapv.getMap().move(
            CameraPosition(Point(64.55256209, 40.55594677), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.0F),
            null
        )
    }
    fun addToFav(view: View){
        dbhelper = DBHelper(this)
        val database = dbhelper.writableDatabase

        database.setresult()

        val intent = Intent(this, Fav::class.java).apply {

        }
        startActivity(intent)
    }
    fun openList(view: View){
        val intent = Intent(this, ViewPlaceList::class.java).apply {

        }
        startActivity(intent)
    }
    private fun init() : Unit {
        //forLat = findViewById(R.id.forLat)
        //forLog = findViewById(R.id.forLog)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        myLocListener = MyLocListener()
        myLocListener.setLocListenerInterface(this)
    }
    @Override
    override fun onStop() {
        super.onStop();
        mapv.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    override fun onStart() {
        super.onStart();
        mapv.onStart();
        MapKitFactory.getInstance().onStart();
    }

    override fun onLocationChanged(loc: Location) {
        myLatitude = loc.latitude
        myLongitude = loc.longitude
        mapv.getMap().move(
            CameraPosition(Point(myLatitude, myLongitude), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.0F),
            null
        )
        //forLat.setText("lat =  $myLatitude")
        //forLog.setText("log = $myLongitude")

    }
}