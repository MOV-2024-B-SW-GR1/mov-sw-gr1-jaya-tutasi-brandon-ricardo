package com.example.sistemasolar_planetas

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainSistemaSolar : AppCompatActivity() {
    private lateinit var inputID: EditText
    private lateinit var inputNombre: EditText
    private lateinit var inputFechaDescubrimiento: EditText
    private lateinit var inputNPlanetas: EditText
    private lateinit var inputDistanciaCentro: EditText
    private lateinit var inputMasDeUnSol: Switch
    private lateinit var inputLatitud: EditText
    private lateinit var inputLongitud: EditText
    private lateinit var dbHelper: ESqliteHelperSistemaSolar

    fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_sistema_solar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización de vistas
        inputID = findViewById(R.id.inputIDSS)
        inputNombre = findViewById(R.id.inputNombreSS)
        inputFechaDescubrimiento = findViewById(R.id.inputFechaDescubrimientoSS)
        inputNPlanetas = findViewById(R.id.inputNPlanetasSS)
        inputDistanciaCentro = findViewById(R.id.inputDistanciaCentroSS)
        inputMasDeUnSol = findViewById(R.id.inputMasDeUnSolSS)
        inputLatitud = findViewById(R.id.inputLatitudSS)
        inputLongitud = findViewById(R.id.inputLongitudSS)

        dbHelper = ESqliteHelperSistemaSolar(this)

        // Botón Listar Sistemas Solares
        findViewById<Button>(R.id.btnListarSS).setOnClickListener {
            listarSistemasSolares()
        }

        // Botón Buscar Sistema Solar
        findViewById<Button>(R.id.btnBuscarSS).setOnClickListener {
            buscarSistemaSolar()
        }

        // Botón Crear Sistema Solar
        findViewById<Button>(R.id.btnCrearSS).setOnClickListener {
            crearSistemaSolar()
        }

        // Botón Eliminar Sistema Solar
        findViewById<Button>(R.id.btnEliminarSS).setOnClickListener {
            eliminarSistemaSolar()
        }

        // Botón Editar Sistema Solar
        findViewById<Button>(R.id.btnEditarSS).setOnClickListener {
            actualizarSistemaSolar()
        }
    }

    private fun listarSistemasSolares() {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayoutSS)
        tableLayout.removeAllViews()  // Limpiar la tabla antes de volver a llenarla
        val listaSistemas = dbHelper.listarSistemasSolares()

        if (listaSistemas.isNotEmpty()) {
            for (sistema in listaSistemas) {
                val tableRow = TableRow(this)

                val textID = TextView(this)
                textID.text = sistema.id.toString()
                textID.gravity = Gravity.CENTER

                val textNombre = TextView(this)
                textNombre.text = sistema.nombre
                textNombre.gravity = Gravity.CENTER

                val textFecha = TextView(this)
                textFecha.text = sistema.fechaDescubrimiento.toString()
                textFecha.gravity = Gravity.CENTER

                val textMasDeUnSol = TextView(this)
                textMasDeUnSol.text = if (sistema.masDeUnSol) "Sí" else "No"
                textMasDeUnSol.gravity = Gravity.CENTER

                val textNumPlanetas = TextView(this)
                textNumPlanetas.text = sistema.numeroDePlanetas.toString()
                textNumPlanetas.gravity = Gravity.CENTER

                val textDistancia = TextView(this)
                textDistancia.text = sistema.distanciaAlCentro.toString()
                textDistancia.gravity = Gravity.CENTER

                val textLatitud = TextView(this)
                textLatitud.text = sistema.latitud.toString()
                textLatitud.gravity = Gravity.CENTER

                val textLongitud = TextView(this)
                textLongitud.text = sistema.longitud.toString()
                textLongitud.gravity = Gravity.CENTER

                // Añadir las vistas de la tabla
                tableRow.addView(textID)
                tableRow.addView(textNombre)
                tableRow.addView(textFecha)
                tableRow.addView(textMasDeUnSol)
                tableRow.addView(textNumPlanetas)
                tableRow.addView(textDistancia)
                tableRow.addView(textLatitud)
                tableRow.addView(textLongitud)
                tableLayout.addView(tableRow)
            }
        } else {
            mostrarSnackbar("No hay sistemas solares registrados.")
        }
    }

    private fun buscarSistemaSolar() {
        try {
            val id = inputID.text.toString().toInt()
            val sistemaSolar = dbHelper.consultarSistemaSolarPorId(id)

            if (sistemaSolar != null) {
                inputNombre.setText(sistemaSolar.nombre)
                inputFechaDescubrimiento.setText(sistemaSolar.fechaDescubrimiento.toString())
                inputMasDeUnSol.isChecked = sistemaSolar.masDeUnSol
                inputNPlanetas.setText(sistemaSolar.numeroDePlanetas.toString())
                inputDistanciaCentro.setText(sistemaSolar.distanciaAlCentro.toString())
                inputLatitud.setText(sistemaSolar.latitud.toString())
                inputLongitud.setText(sistemaSolar.longitud.toString())
                mostrarSnackbar("Sistema solar ${sistemaSolar.nombre} encontrado.")
            } else {
                mostrarSnackbar("Sistema solar no encontrado.")
            }
        } catch (e: Exception) {
            mostrarSnackbar("ID inválido.")
        }
    }

    private fun crearSistemaSolar() {
        try {
            val nombre = inputNombre.text.toString()
            val fechaDescubrimiento = LocalDate.parse(inputFechaDescubrimiento.text.toString(), DateTimeFormatter.ISO_DATE)
            val tieneMasDeUnSol = inputMasDeUnSol.isChecked
            val numeroDePlanetas = inputNPlanetas.text.toString().toInt()
            val distanciaAlCentro = inputDistanciaCentro.text.toString().toDouble()
            val latitud = inputLatitud.text.toString().toDouble()
            val longitud = inputLongitud.text.toString().toDouble()

            val respuesta = dbHelper.crearSistemaSolar(nombre, fechaDescubrimiento, tieneMasDeUnSol, numeroDePlanetas, distanciaAlCentro, latitud, longitud)

            if (respuesta) {
                mostrarSnackbar("Sistema solar creado")
                limpiarCampos()
            } else {
                mostrarSnackbar("Fallo al crear el sistema solar")
            }
        } catch (e: Exception) {
            mostrarSnackbar("Error en la entrada de datos. Verifica los valores ingresados.")
        }
    }

    private fun eliminarSistemaSolar() {
        try {
            val id = inputID.text.toString().toInt()
            val respuesta = dbHelper.eliminarSistemaSolar(id)

            if (respuesta) {
                mostrarSnackbar("Sistema solar eliminado")
                inputID.setText("")
            } else {
                mostrarSnackbar("No encontrado")
            }
        } catch (e: Exception) {
            mostrarSnackbar("ID inválido.")
        }
    }

    private fun actualizarSistemaSolar() {
        try {
            val id = inputID.text.toString().toInt()
            val nombre = inputNombre.text.toString()
            val fechaDescubrimiento = LocalDate.parse(inputFechaDescubrimiento.text.toString(), DateTimeFormatter.ISO_DATE)
            val tieneMasDeUnSol = inputMasDeUnSol.isChecked
            val numeroDePlanetas = inputNPlanetas.text.toString().toInt()
            val distanciaAlCentro = inputDistanciaCentro.text.toString().toDouble()
            val latitud = inputLatitud.text.toString().toDouble()
            val longitud = inputLongitud.text.toString().toDouble()

            val respuesta = dbHelper.actualizarSistemaSolar(id, nombre, fechaDescubrimiento, tieneMasDeUnSol, numeroDePlanetas, distanciaAlCentro, latitud, longitud)

            if (respuesta) {
                mostrarSnackbar("Sistema solar actualizado")
                limpiarCampos()
                listarSistemasSolares()  // Actualizar la tabla con los datos modificados
            } else {
                mostrarSnackbar("Fallo")
            }
        } catch (e: Exception) {
            mostrarSnackbar("Error en la entrada de datos.")
        }
    }

    private fun limpiarCampos() {
        inputID.setText("")
        inputNombre.setText("")
        inputFechaDescubrimiento.setText("")
        inputNPlanetas.setText("")
        inputDistanciaCentro.setText("")
        inputMasDeUnSol.isChecked = false
        inputLatitud.setText("")
        inputLongitud.setText("")
    }
}
