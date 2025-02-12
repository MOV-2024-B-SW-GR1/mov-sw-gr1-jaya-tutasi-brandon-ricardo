package com.example.psicofrmacos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BuscarMedPorClave : AppCompatActivity() {

    private lateinit var autoCompleteBuscarMedPorClave: AutoCompleteTextView
    private lateinit var listViewResultados: ListView
    private lateinit var dbHelper: DBHelper
    private lateinit var medicamentos: List<Medicamento>  // Guardamos la lista completa de medicamentos


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscar_med_por_clave)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Inicialización
        autoCompleteBuscarMedPorClave = findViewById(R.id.autoCompleteBuscarMedPorClave)
        listViewResultados = findViewById(R.id.listViewResultados)
        dbHelper = DBHelper(this)

        // Configurar el AutoCompleteTextView
        autoCompleteBuscarMedPorClave.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    // Realiza la consulta y actualiza la lista con los resultados
                    val resultados = buscarMedicamentoPorPalabraClave(query)
                    val adapter = ArrayAdapter(this@BuscarMedPorClave, android.R.layout.simple_list_item_1, resultados)
                    listViewResultados.adapter = adapter
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
        // Manejar clic en los elementos de la lista
        listViewResultados.setOnItemClickListener { parent, view, position, id ->
            // Obtener el medicamento seleccionado
            val selectedMedicamento = medicamentos[position]

            // Crear el Intent para ir a la actividad de detalles
            val intent = Intent(this, DetallesMedicamento::class.java).apply {
                putExtra("id_medicamento", selectedMedicamento.idMedicamento)
                putExtra("nombre_comercial", selectedMedicamento.nombreComercial)
                putExtra("nombre_generico", selectedMedicamento.nombreGenerico)
                putExtra("descripcion", selectedMedicamento.descripcion)
                putExtra("contraindicaciones", selectedMedicamento.contraindicaciones)
                putExtra("dosis_recomendada", selectedMedicamento.dosisRecomendada)
                putExtra("advertencia", selectedMedicamento.advertencia)
            }

            // Iniciar la actividad de detalles
            startActivity(intent)
        }
    }
    // Método para realizar la búsqueda por palabra clave
    private fun buscarMedicamentoPorPalabraClave(query: String): List<String> {
        val medicamentosEncontrados = dbHelper.obtenerMedicamentosPorPalabraClave(query)
        // Guardar la lista completa de medicamentos para acceder a ella después
        medicamentos = medicamentosEncontrados

        // Convertimos la lista de medicamentos en una lista de nombres comerciales
        return medicamentosEncontrados.map { it.nombreComercial }
    }
}