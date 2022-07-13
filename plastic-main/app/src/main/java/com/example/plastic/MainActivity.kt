package com.example.plastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ServerConn.connect()
    }
    fun openPhoto(view: View) {

        val intent = Intent(this, Photo::class.java).apply {

        }
        startActivity(intent)
    }
    fun openFav(view: View) {

        val intent = Intent(this, Fav::class.java).apply {

        }
        startActivity(intent)
    }

    fun openMap(view: View) {

        val intent = Intent(this, CodeActivity::class.java).apply {

        }
        startActivity(intent)
    }
}