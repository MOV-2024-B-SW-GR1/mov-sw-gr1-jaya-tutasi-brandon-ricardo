package com.example.psicofrmacos

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesMedicamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_medicamento)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtén los datos pasados desde la actividad anterior
        val idMedicamento = intent.getLongExtra("id_medicamento", -1)
        val nombreComercial = intent.getStringExtra("nombre_comercial")
        val nombreGenerico = intent.getStringExtra("nombre_generico")
        val descripcion = intent.getStringExtra("descripcion")
        val contraindicaciones = intent.getStringExtra("contraindicaciones")
        val dosisRecomendada = intent.getStringExtra("dosis_recomendada")
        val advertencia = intent.getStringExtra("advertencia")

        // Mostrar los datos en los TextViews correspondientes
        findViewById<TextView>(R.id.tvNombreComercial).text = nombreComercial
        findViewById<TextView>(R.id.tvNombreGenerico).text = nombreGenerico
        findViewById<TextView>(R.id.tvDescripcion).text = descripcion
        findViewById<TextView>(R.id.tvContraindicaciones).text = contraindicaciones
        findViewById<TextView>(R.id.tvDosisRecomendada).text = dosisRecomendada
        findViewById<TextView>(R.id.tvAdvertencia).text = advertencia
    }
}