package com.example.plastic

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plastic.databinding.ItemCodeListBinding

class CodeListAdapter
    (var codes: MutableList<Int>) : RecyclerView.Adapter<CodeListAdapter.CodesViewHolder>() {
    inner class CodesViewHolder(val binding: ItemCodeListBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, CodeActivity::class.java)
                intent.putExtra("CODE_INDEX", codes[adapterPosition])
                binding.root.context.startActivity(intent)

            }
        }
        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCodeListBinding.inflate(layoutInflater, parent, false)
        return CodesViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return codes.size
    }
    override fun onBindViewHolder(holder: CodesViewHolder, position: Int) {
        holder.binding.apply {
            tvNameCode.text = codes[position].toString()
        }
    }
}