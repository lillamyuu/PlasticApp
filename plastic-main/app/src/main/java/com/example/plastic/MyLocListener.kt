package com.example.plastic

import android.location.Location
import android.location.LocationListener

class MyLocListener : LocationListener {
    //исправления для работы
    private lateinit var locListenerInterface: locListenerInterface

    override fun onLocationChanged(location: Location) {
        locListenerInterface.onLocationChanged(location)
    }

    //сеттер прописывал вручную
    public fun setLocListenerInterface(locListenerInterface: locListenerInterface) : Unit{
        this.locListenerInterface = locListenerInterface
    }
}