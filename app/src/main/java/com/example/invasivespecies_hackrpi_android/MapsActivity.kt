package com.example.invasivespecies_hackrpi_android

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.example.invasivespecies_hackrpi_android.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CircleOptions
import com.google.firebase.firestore.FirebaseFirestore

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("hackrpi-csv-1")

        collection.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    val x = document.getString("x")
                    val y = document.getString("y")
                    var muthuAssCoordinates = LatLng(0.0, 0.0)
                    if(x != null && y != null) {
                        muthuAssCoordinates = LatLng(x.toDouble(), y.toDouble())
                    }
                    val circleOptions = CircleOptions()
                        .center(muthuAssCoordinates)
                        .radius(500000.0)
                        .strokeColor(Color.RED) // Set the stroke (border) color
                        .fillColor(Color.rgb(0, 0, 255)) // Set the fill (inside) color

                    val circle = googleMap.addCircle(circleOptions)
                }
            }
        }
    }
}