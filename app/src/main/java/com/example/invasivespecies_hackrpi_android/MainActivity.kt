package com.example.invasivespecies_hackrpi_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.invasivespecies_hackrpi_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(MapFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map_button -> {
                    loadFragment(MapFragment())
                    true
                }

                R.id.camera_button -> {
                    loadFragment(CameraFragment())
                    true
                }

                else -> {
                    true
                }
            }
        }
    }
    private fun loadFragment(frag: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, frag)
        transaction.commit()
    }
}