package com.example.sistemasolar_planetas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ESqliteHelperSistemaSolar(contexto: Context?) : SQLiteOpenHelper(
    contexto,
    "sistema_solar_db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaSistemaSolar = """
            CREATE TABLE SISTEMA_SOLAR (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                fechaDescubrimiento TEXT NOT NULL,
                tieneMasDeUnSol INTEGER NOT NULL,
                numeroDePlanetas INTEGER NOT NULL,
                distanciaAlCentro REAL NOT NULL
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaSistemaSolar)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearSistemaSolar(
        nombre: String,
        fechaDescubrimiento: LocalDate,
        tieneMasDeUnSol: Boolean,
        numeroDePlanetas: Int,
        distanciaAlCentro: Double
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("fechaDescubrimiento", fechaDescubrimiento.format(DateTimeFormatter.ISO_DATE))
            put("tieneMasDeUnSol", if (tieneMasDeUnSol) 1 else 0)
            put("numeroDePlanetas", numeroDePlanetas)
            put("distanciaAlCentro", distanciaAlCentro)
        }
        val resultado = db.insert("SISTEMA_SOLAR", null, valores)
        db.close()
        return resultado != -1L
    }

    fun eliminarSistemaSolar(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("SISTEMA_SOLAR", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun actualizarSistemaSolar(
        id: Int,
        nombre: String,
        fechaDescubrimiento: LocalDate,
        tieneMasDeUnSol: Boolean,
        numeroDePlanetas: Int,
        distanciaAlCentro: Double
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("fechaDescubrimiento", fechaDescubrimiento.format(DateTimeFormatter.ISO_DATE))
            put("tieneMasDeUnSol", if (tieneMasDeUnSol) 1 else 0)
            put("numeroDePlanetas", numeroDePlanetas)
            put("distanciaAlCentro", distanciaAlCentro)
        }
        val resultado = db.update("SISTEMA_SOLAR", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun consultarSistemaSolarPorId(id: Int): SistemaSolar? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, fechaDescubrimiento, tieneMasDeUnSol, numeroDePlanetas, distanciaAlCentro FROM SISTEMA_SOLAR WHERE id = ?", arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            try {
                val sistemaSolar = SistemaSolar(
                    cursor.getInt(0), // ID
                    cursor.getString(1), // Nombre
                    LocalDate.parse(cursor.getString(2), DateTimeFormatter.ISO_DATE), // Fecha Descubrimiento
                    cursor.getInt(3) == 1, // Tiene más de un sol
                    cursor.getInt(4), // Número de planetas
                    cursor.getDouble(5) // Distancia al centro
                )
                cursor.close()
                db.close()
                sistemaSolar
            } catch (e: Exception) {
                //mostrarSnackbar("Error al leer la fecha del sistema solar.")
                null
            }
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun listarSistemasSolares(): List<SistemaSolar> {
        val lista = mutableListOf<SistemaSolar>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, fechaDescubrimiento, tieneMasDeUnSol, numeroDePlanetas, distanciaAlCentro FROM SISTEMA_SOLAR",
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)  // 🔹 Agregar el ID aquí
            val nombre = cursor.getString(1)
            val fechaDescubrimiento = LocalDate.parse(cursor.getString(2), DateTimeFormatter.ISO_DATE)
            val masDeUnSol = cursor.getInt(3) == 1
            val numeroDePlanetas = cursor.getInt(4)
            val distanciaAlCentro = cursor.getDouble(5)

            // 🔹 Ahora estamos pasando el ID junto con todos los otros parámetros esperados
            lista.add(SistemaSolar(id, nombre, fechaDescubrimiento, masDeUnSol, numeroDePlanetas, distanciaAlCentro))
        }

        cursor.close()
        db.close()
        return lista
    }

}
