package com.example.plastic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plastic.databinding.ItemFavListBinding
import com.example.plastic.databinding.ItemPlaceListBinding

class FavListAdapter (var codes: MutableList<String>) : RecyclerView.Adapter<FavListAdapter.FavViewHolder>() {
    inner class FavViewHolder(val binding: ItemFavListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFavListBinding.inflate(layoutInflater, parent, false)
        return FavViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return codes.size
    }
    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.binding.apply {
            tvFav.text = codes[position]
        }
    }
}