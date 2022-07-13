package com.example.plastic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.plastic.databinding.ActivityListBinding
import kotlin.collections.Map

class ViewPlaceList : AppCompatActivity() {
    lateinit var binding: ActivityListBinding
    lateinit var adapter: PlaceListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PlaceListAdapter(PlaceResultPlace.getInstance())
        binding.rvPlaces.adapter = adapter
        binding.rvPlaces.layoutManager = LinearLayoutManager(this)
        binding.btnToMap.setOnClickListener{
            val intent = Intent(this, Map::class.java)
            this.startActivity(intent)
        }
    }
}
