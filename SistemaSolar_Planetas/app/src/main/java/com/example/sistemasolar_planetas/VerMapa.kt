package com.example.sistemasolar_planetas

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class VerMapa : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_mapa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener valores de latitud y longitud
        latitud = intent.getDoubleExtra("latitud", 0.0)
        longitud = intent.getDoubleExtra("longitud", 0.0)

        // Configurar el mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Habilitar controles de zoom
        mMap.uiSettings.isZoomControlsEnabled = true  // 🔹 Muestra los botones de zoom
        mMap.uiSettings.isZoomGesturesEnabled = true  // 🔹 Permite hacer zoom con los dedos
        mMap.uiSettings.isMapToolbarEnabled = true    // 🔹 Agrega accesos directos de navegación
        mMap.uiSettings.isMyLocationButtonEnabled = true  // 🔹 Activa el botón de ubicación (requiere permisos)

        // Configurar la ubicación
        val ubicacion = LatLng(latitud, longitud)

        // Agregar marcador en la ubicación del sistema solar
        mMap.addMarker(MarkerOptions().position(ubicacion).title("Ubicación del Sistema Solar"))

        // Mover la cámara con zoom más cercano
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f)) // 🔹 Ajusta el zoom a un valor más cercano
    }
}