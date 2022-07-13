package com.example.plastic

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plastic.databinding.ActivityFavBinding
import com.example.plastic.databinding.ActivityPhotoBinding


class Fav : AppCompatActivity() {
    private lateinit var adapter: FavListAdapter
    lateinit var dbhelper: DBHelper
    private lateinit var binding: ActivityFavBinding
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbhelper = DBHelper(this)
        val database = dbhelper.writableDatabase
        adapter = FavListAdapter(mutableListOf("PET-1, PE-LD-4"))
        binding.rvFav.adapter = adapter
        binding.rvFav.layoutManager = LinearLayoutManager(this)

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {

            }
            startActivity(intent)
        }
    }
}