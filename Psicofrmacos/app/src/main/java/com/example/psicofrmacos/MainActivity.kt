package com.example.psicofrmacos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para ir a la actividad
        val botonBuscarMed = findViewById<Button>(R.id.btnBuscarMed)
        botonBuscarMed.setOnClickListener {
            irActividad(BuscarMed::class.java)
        }

        val botonBuscarMedPorClave = findViewById<Button>(R.id.btnBuscarMedPorClave)
        botonBuscarMedPorClave.setOnClickListener {
            irActividad(BuscarMedPorClave::class.java)
        }

        val botonFiltrarMed = findViewById<Button>(R.id.btnFiltrarMed)
        botonFiltrarMed.setOnClickListener {
            irActividad(FiltrarMed::class.java)
        }

        val botonHistorial = findViewById<Button>(R.id.btnHistorial)
        botonHistorial.setOnClickListener {
            irActividad(Historial::class.java)
        }

        val botonFavoritos = findViewById<Button>(R.id.btnFavoritos)
        botonFavoritos.setOnClickListener {
            irActividad(Favoritos::class.java)
        }

    }
    // Método para abrir actividades
    private fun irActividad(clase: Class<*>) {
        startActivity(Intent(this, clase))
    }
}