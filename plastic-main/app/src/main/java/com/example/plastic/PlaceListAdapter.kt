package com.example.plastic

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.plastic.databinding.ItemPlaceListBinding

class PlaceListAdapter (var places: PlaceList) : RecyclerView.Adapter<PlaceListAdapter.PlacesViewHolder>() {
    inner class PlacesViewHolder(val binding: ItemPlaceListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {


            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceListBinding.inflate(layoutInflater, parent, false)
        return PlacesViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return places.items.size
    }
    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.binding.apply {
            tvNamePlace.text = places.items[position].name
            tvTime.text = places.items[position].time
        }
    }
}