package com.example.sistemasolar_planetas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: ESqliteHelperSistemaSolar
    private lateinit var adapter: SistemaSolarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón para ir a la actividad de Planetas
        val botonPlaneta = findViewById<Button>(R.id.btnPlaneta)
        botonPlaneta.setOnClickListener {
            irActividad(MainPlaneta::class.java)
        }

        // Botón para ir a la actividad de Sistemas Solares
        val botonSistemaSolar = findViewById<Button>(R.id.btnSistemaSolar)
        botonSistemaSolar.setOnClickListener {
            irActividad(MainSistemaSolar::class.java)
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewSistemas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        dbHelper = ESqliteHelperSistemaSolar(this)
        val listaSistemas = dbHelper.listarSistemasSolares()

        // Configurar adaptador con el clickListener
        adapter = SistemaSolarAdapter(listaSistemas) { sistema, view ->
            mostrarMenu(sistema, view)
        }
        recyclerView.adapter = adapter
    }

    // Método para abrir actividades
    private fun irActividad(clase: Class<*>) {
        startActivity(Intent(this, clase))
    }

    // Método para mostrar el menú emergente al hacer clic en un elemento
    private fun mostrarMenu(sistemaSolar: SistemaSolar, view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_sistema_solar, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.verMapa -> abrirMapa(sistemaSolar.latitud, sistemaSolar.longitud)
            }
            true
        }
        popupMenu.show()
    }

    // Método para abrir Google Maps con la ubicación del sistema solar
    private fun abrirMapa(latitud: Double, longitud: Double) {
        val intent = Intent(this, VerMapa::class.java).apply {
            putExtra("latitud", latitud)
            putExtra("longitud", longitud)
        }
        startActivity(intent)
    }
}