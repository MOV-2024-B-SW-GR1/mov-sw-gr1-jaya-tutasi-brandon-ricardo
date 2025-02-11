package com.example.sistemasolar_planetas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class MainPlaneta : AppCompatActivity() {
    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_planeta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Buscar planeta por ID
        val botonBuscarBDD = findViewById<Button>(R.id.btnBuscarP)
        botonBuscarBDD.setOnClickListener {
            val idText = findViewById<EditText>(R.id.inputID).text.toString()
            val id = idText.toIntOrNull()

            if (id == null) {
                mostrarSnackbar("Ingrese un ID válido.")
                return@setOnClickListener
            }

            val planeta = ESqliteHelperPlaneta(this).consultarPlanetaPorId(id)

            if (planeta == null) {
                mostrarSnackbar("Planeta no encontrado")
            } else {
                findViewById<EditText>(R.id.inputNombreP).setText(planeta.nombre)
                findViewById<EditText>(R.id.inputDiametroP).setText(planeta.diametro.toString())
                findViewById<EditText>(R.id.inputPeriodoOrbitalP).setText(planeta.periodoOrbital.toString())
                findViewById<Switch>(R.id.inputHabitadoP).isChecked = planeta.esHabitable
                findViewById<EditText>(R.id.inputTemperaturaMediaP).setText(planeta.temperaturaMedia.toString())
                findViewById<EditText>(R.id.inputIDSistemaSolarP).setText(planeta.sistemaSolarId.toString())
                mostrarSnackbar("Planeta ${planeta.nombre} encontrado")
            }
        }

        // 🔹 Eliminar planeta
        val botonEliminarBDD = findViewById<Button>(R.id.btnEliminarP)
        botonEliminarBDD.setOnClickListener {
            val idText = findViewById<EditText>(R.id.inputID).text.toString()
            val id = idText.toIntOrNull()

            if (id == null) {
                mostrarSnackbar("Ingrese un ID válido para eliminar.")
                return@setOnClickListener
            }

            val respuesta = ESqliteHelperPlaneta(this).eliminarPlaneta(id)
            mostrarSnackbar(if (respuesta) "Planeta eliminado" else "No encontrado")
        }

        // 🔹 Crear un nuevo planeta
        val botonCrearBDD = findViewById<Button>(R.id.btnCrearP)
        botonCrearBDD.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.inputNombreP).text.toString()
            val diametroText = findViewById<EditText>(R.id.inputDiametroP).text.toString()
            val periodoOrbitalText = findViewById<EditText>(R.id.inputPeriodoOrbitalP).text.toString()
            val esHabitable = findViewById<Switch>(R.id.inputHabitadoP).isChecked
            val temperaturaMediaText = findViewById<EditText>(R.id.inputTemperaturaMediaP).text.toString()
            val sistemaSolarIdText = findViewById<EditText>(R.id.inputIDSistemaSolarP).text.toString()

            if (nombre.isEmpty() || diametroText.isEmpty() || periodoOrbitalText.isEmpty() || temperaturaMediaText.isEmpty() || sistemaSolarIdText.isEmpty()) {
                mostrarSnackbar("Todos los campos deben estar llenos.")
                return@setOnClickListener
            }

            val respuesta = ESqliteHelperPlaneta(this).crearPlaneta(
                nombre,
                diametroText.toDouble(),
                periodoOrbitalText.toInt(),
                esHabitable,
                temperaturaMediaText.toFloat(),
                sistemaSolarIdText.toInt()
            )

            if (respuesta) {
                mostrarSnackbar("Planeta creado")
                findViewById<EditText>(R.id.inputNombreP).setText("")
                findViewById<EditText>(R.id.inputDiametroP).setText("")
                findViewById<EditText>(R.id.inputPeriodoOrbitalP).setText("")
                findViewById<Switch>(R.id.inputHabitadoP).isChecked = false
                findViewById<EditText>(R.id.inputTemperaturaMediaP).setText("")
                findViewById<EditText>(R.id.inputIDSistemaSolarP).setText("")
            } else {
                mostrarSnackbar("Fallo al crear planeta")
            }
        }

        // 🔹 Actualizar un planeta
        val botonActualizarBDD = findViewById<Button>(R.id.btnEditarP)
        botonActualizarBDD.setOnClickListener {
            val idText = findViewById<EditText>(R.id.inputID).text.toString()
            val nombre = findViewById<EditText>(R.id.inputNombreP).text.toString()
            val diametroText = findViewById<EditText>(R.id.inputDiametroP).text.toString()
            val periodoOrbitalText = findViewById<EditText>(R.id.inputPeriodoOrbitalP).text.toString()
            val esHabitable = findViewById<Switch>(R.id.inputHabitadoP).isChecked
            val temperaturaMediaText = findViewById<EditText>(R.id.inputTemperaturaMediaP).text.toString()
            val sistemaSolarIdText = findViewById<EditText>(R.id.inputIDSistemaSolarP).text.toString()

            val id = idText.toIntOrNull()

            if (id == null || nombre.isEmpty() || diametroText.isEmpty() || periodoOrbitalText.isEmpty() || temperaturaMediaText.isEmpty() || sistemaSolarIdText.isEmpty()) {
                mostrarSnackbar("Todos los campos deben estar llenos y el ID debe ser válido.")
                return@setOnClickListener
            }

            val respuesta = ESqliteHelperPlaneta(this).actualizarPlaneta(
                id,
                nombre,
                diametroText.toDouble(),
                periodoOrbitalText.toInt(),
                esHabitable,
                temperaturaMediaText.toFloat(),
                sistemaSolarIdText.toInt()
            )

            if (respuesta) {
                mostrarSnackbar("Planeta actualizado")
                findViewById<EditText>(R.id.inputID).setText("")
                findViewById<EditText>(R.id.inputNombreP).setText("")
                findViewById<EditText>(R.id.inputDiametroP).setText("")
                findViewById<EditText>(R.id.inputPeriodoOrbitalP).setText("")
                findViewById<Switch>(R.id.inputHabitadoP).isChecked = false
                findViewById<EditText>(R.id.inputTemperaturaMediaP).setText("")
                findViewById<EditText>(R.id.inputIDSistemaSolarP).setText("")
            } else {
                mostrarSnackbar("Fallo al actualizar planeta")
            }
        }
    }
}