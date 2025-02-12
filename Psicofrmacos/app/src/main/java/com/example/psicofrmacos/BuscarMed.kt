package com.example.psicofrmacos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BuscarMed : AppCompatActivity() {

    private lateinit var autoCompleteBuscarMed: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_med)  // Aquí debe estar el layout de la actividad
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Iniciamos el AutoCompleteTextView
        autoCompleteBuscarMed = findViewById(R.id.autoCompleteBuscarMed)

        // Obtener la lista de medicamentos de la base de datos
        val medicamentosList = obtenerMedicamentos()  // Aquí obtienes los datos desde la base de datos

        // Configurar el adaptador para el AutoCompleteTextView
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, medicamentosList)
        autoCompleteBuscarMed.setAdapter(adapter)

        // Configurar un TextWatcher para actualizar las sugerencias al escribir
        autoCompleteBuscarMed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    // Realiza la consulta y actualiza las sugerencias
                    val resultados = buscarMedicamento(query)
                    adapter.clear()
                    adapter.addAll(resultados)
                    adapter.notifyDataSetChanged()
                } else {
                    // Si el campo está vacío, muestra todas las sugerencias
                    adapter.clear()
                    adapter.addAll(medicamentosList)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Configurar un listener para manejar la selección de un medicamento
        autoCompleteBuscarMed.setOnItemClickListener { parent, view, position, id ->
            val selectedMedicamento = parent.getItemAtPosition(position) as String
            // Busca el medicamento completo en la base de datos por nombre comercial
            val medicamento = obtenerMedicamentoPorNombre(selectedMedicamento)

            // Crear un Intent para iniciar la actividad de detalles y pasar los datos
            val intent = Intent(this, DetallesMedicamento::class.java)
            intent.putExtra("id_medicamento", medicamento.idMedicamento)
            intent.putExtra("nombre_comercial", medicamento.nombreComercial)
            intent.putExtra("nombre_generico", medicamento.nombreGenerico)
            intent.putExtra("descripcion", medicamento.descripcion)
            intent.putExtra("contraindicaciones", medicamento.contraindicaciones)
            intent.putExtra("dosis_recomendada", medicamento.dosisRecomendada)
            intent.putExtra("advertencia", medicamento.advertencia)
            startActivity(intent)
        }
    }

    // Método que obtiene la lista de medicamentos de la base de datos
    private fun obtenerMedicamentos(): List<String> {
        val dbHelper = DBHelper(this)
        val medicamentos = dbHelper.obtenerMedicamentos()  // Este método devuelve una lista de objetos Medicamento

        // Convertimos la lista de medicamentos en una lista de nombres comerciales
        return medicamentos.map { it.nombreComercial }
    }

    // Método que realiza la consulta SQL para obtener medicamentos que coincidan con el texto
    private fun buscarMedicamento(query: String): List<String> {
        val dbHelper = DBHelper(this)
        val medicamentos = dbHelper.obtenerMedicamentosPorPalabraClave(query)

        // Convertimos la lista de medicamentos en una lista de nombres comerciales
        return medicamentos.map { it.nombreComercial }
    }

    // Método para obtener el medicamento completo por su nombre comercial
    private fun obtenerMedicamentoPorNombre(nombreComercial: String): Medicamento {
        val dbHelper = DBHelper(this)
        return dbHelper.obtenerMedicamentoPorNombre(nombreComercial)
    }
}