package com.example.invasivespecies_hackrpi_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.invasivespecies_hackrpi_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemReselectedListener {

        }
    }
}