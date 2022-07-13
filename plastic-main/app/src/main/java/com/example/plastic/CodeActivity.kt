package com.example.plastic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plastic.databinding.ActivityCodeBinding


class CodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("CODE_INDEX", 0)
        if(id==0){
            val res = resources
            val name = ClassNames.getImage(1)
            val resID: Int = res.getIdentifier(name, "drawable", packageName)
            binding.CodeImage.setImageResource(resID)
            binding.tvCodeName.text = "01-PET"
            binding.tvCodeInfo.text = "Полиэтилентерефталат: бутылки, в которых продают воду, газировку, молоко, масло"
            return
        }
        val res = resources
        val name = ClassNames.getImage(id)
        val resID: Int = res.getIdentifier(name, "drawable", packageName)
        binding.CodeImage.setImageResource(resID)
        binding.tvCodeName.text = ClassNames.getClass(id)
        binding.tvCodeInfo.text = ClassNames.getInfo(id)
    }


}