package com.example.plastic

import android.location.Location

interface locListenerInterface {
    fun onLocationChanged(loc: Location) : Unit
}