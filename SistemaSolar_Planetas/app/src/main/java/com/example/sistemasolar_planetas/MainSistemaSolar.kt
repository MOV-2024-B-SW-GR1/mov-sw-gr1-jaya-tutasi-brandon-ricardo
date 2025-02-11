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
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainSistemaSolar : AppCompatActivity() {
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
        setContentView(R.layout.activity_main_sistema_solar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonListarBDD = findViewById<Button>(R.id.btnListarSS)
        val tableLayout = findViewById<TableLayout>(R.id.tableLayoutSS)
        botonListarBDD.setOnClickListener {
            tableLayout.removeAllViews()
            val listaSistemas = ESqliteHelperSistemaSolar(this).listarSistemasSolares()
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

                    tableRow.addView(textID)
                    tableRow.addView(textNombre)
                    tableRow.addView(textFecha)
                    tableRow.addView(textMasDeUnSol)
                    tableRow.addView(textNumPlanetas)
                    tableRow.addView(textDistancia)
                    tableLayout.addView(tableRow)
                }
            } else {
                mostrarSnackbar("No hay sistemas solares registrados.")
            }
        }

        val botonBuscarBDD = findViewById<Button>(R.id.btnBuscarSS)
        botonBuscarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.inputIDSS)
            val sistemaSolar = ESqliteHelperSistemaSolar(this).consultarSistemaSolarPorId(id.text.toString().toInt())


            if (sistemaSolar == null) {
                id.setText("")
                findViewById<EditText>(R.id.inputNombreSS).setText("")
                findViewById<EditText>(R.id.inputFechaDescubrimientoSS).setText("")
                findViewById<Switch>(R.id.inputMasDeUnSolSS).isChecked = false
                findViewById<EditText>(R.id.inputNPlanetasSS).setText("")
                findViewById<EditText>(R.id.inputDistanciaCentroSS).setText("")
                mostrarSnackbar("Sistema solar no encontrado")
            } else {
                findViewById<EditText>(R.id.inputNombreSS).setText(sistemaSolar.nombre)
                findViewById<EditText>(R.id.inputFechaDescubrimientoSS).setText(sistemaSolar.fechaDescubrimiento.toString())
                findViewById<Switch>(R.id.inputMasDeUnSolSS).isChecked = sistemaSolar.masDeUnSol
                findViewById<EditText>(R.id.inputNPlanetasSS).setText(sistemaSolar.numeroDePlanetas.toString())
                findViewById<EditText>(R.id.inputDistanciaCentroSS).setText(sistemaSolar.distanciaAlCentro.toString())
                mostrarSnackbar("Sistema solar ${sistemaSolar.nombre} encontrado")
            }
        }

        val botonCrearBDD = findViewById<Button>(R.id.btnCrearSS)
        botonCrearBDD.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.inputNombreSS)
            val fechaDescubrimiento = findViewById<EditText>(R.id.inputFechaDescubrimientoSS)
            val tieneMasDeUnSol = findViewById<Switch>(R.id.inputMasDeUnSolSS)
            val numeroDePlanetas = findViewById<EditText>(R.id.inputNPlanetasSS)
            val distanciaAlCentro = findViewById<EditText>(R.id.inputDistanciaCentroSS)

            try {
                val fecha = LocalDate.parse(fechaDescubrimiento.text.toString(), DateTimeFormatter.ISO_DATE)

                val respuesta = ESqliteHelperSistemaSolar(this).crearSistemaSolar(
                    nombre.text.toString(),
                    fecha,
                    tieneMasDeUnSol.isChecked,
                    numeroDePlanetas.text.toString().toInt(),
                    distanciaAlCentro.text.toString().toDouble()
                )

                if (respuesta) {
                    mostrarSnackbar("Sistema solar creado")
                    nombre.setText("")
                    fechaDescubrimiento.setText("")
                    tieneMasDeUnSol.isChecked = false
                    numeroDePlanetas.setText("")
                    distanciaAlCentro.setText("")
                } else {
                    mostrarSnackbar("Fallo al crear el sistema solar")
                }
            } catch (e: Exception) {
                mostrarSnackbar("Error en la fecha. Usa el formato YYYY-MM-DD")
            }
        }

        val botonEliminarBDD = findViewById<Button>(R.id.btnEliminarSS)
        botonEliminarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.inputIDSS)

            val respuesta = ESqliteHelperSistemaSolar(this).eliminarSistemaSolar(id.text.toString().toInt())

            if (respuesta) {
                mostrarSnackbar("Sistema solar eliminado")
                id.setText("")
            } else {
                mostrarSnackbar("No encontrado")
            }
        }

        val botonActualizarBDD = findViewById<Button>(R.id.btnEditarSS)
        botonActualizarBDD.setOnClickListener {
            val id = findViewById<EditText>(R.id.inputIDSS)
            val nombre = findViewById<EditText>(R.id.inputNombreSS)
            val fechaDescubrimiento = findViewById<EditText>(R.id.inputFechaDescubrimientoSS)
            val tieneMasDeUnSol = findViewById<Switch>(R.id.inputMasDeUnSolSS)
            val numeroDePlanetas = findViewById<EditText>(R.id.inputNPlanetasSS)
            val distanciaAlCentro = findViewById<EditText>(R.id.inputDistanciaCentroSS)

            val respuesta = ESqliteHelperSistemaSolar(this).actualizarSistemaSolar(
                id.text.toString().toInt(),
                nombre.text.toString(),
                LocalDate.parse(fechaDescubrimiento.text.toString(), DateTimeFormatter.ISO_DATE),
                tieneMasDeUnSol.isChecked,
                numeroDePlanetas.text.toString().toInt(),
                distanciaAlCentro.text.toString().toDouble()
            )

            if (respuesta) {
                mostrarSnackbar("Sistema solar actualizado")
                id.setText("")
                nombre.setText("")
                fechaDescubrimiento.setText("")
                tieneMasDeUnSol.isChecked = false
                numeroDePlanetas.setText("")
                distanciaAlCentro.setText("")
            } else {
                mostrarSnackbar("Fallo")
            }
        }
    }
}