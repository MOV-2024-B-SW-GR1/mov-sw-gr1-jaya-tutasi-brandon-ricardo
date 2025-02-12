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
    2
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaSistemaSolar = """
            CREATE TABLE SISTEMA_SOLAR (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                fechaDescubrimiento TEXT NOT NULL,
                tieneMasDeUnSol INTEGER NOT NULL,
                numeroDePlanetas INTEGER NOT NULL,
                distanciaAlCentro REAL NOT NULL,
                latitud REAL NOT NULL,  -- 🔹 Nueva columna
                longitud REAL NOT NULL  -- 🔹 Nueva columna
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaSistemaSolar)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE SISTEMA_SOLAR ADD COLUMN latitud REAL DEFAULT 0.0")
            db?.execSQL("ALTER TABLE SISTEMA_SOLAR ADD COLUMN longitud REAL DEFAULT 0.0")
        }
    }

    fun crearSistemaSolar(
        nombre: String,
        fechaDescubrimiento: LocalDate,
        tieneMasDeUnSol: Boolean,
        numeroDePlanetas: Int,
        distanciaAlCentro: Double,
        latitud: Double,  // 🔹 Nuevo parámetro
        longitud: Double
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("fechaDescubrimiento", fechaDescubrimiento.format(DateTimeFormatter.ISO_DATE))
            put("tieneMasDeUnSol", if (tieneMasDeUnSol) 1 else 0)
            put("numeroDePlanetas", numeroDePlanetas)
            put("distanciaAlCentro", distanciaAlCentro)
            put("latitud", latitud)  // 🔹 Guardamos latitud
            put("longitud", longitud)  // 🔹 Guardamos longitud
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
        distanciaAlCentro: Double,
        latitud: Double,  // 🔹 Nueva propiedad
        longitud: Double  // 🔹 Nueva propiedad
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("fechaDescubrimiento", fechaDescubrimiento.format(DateTimeFormatter.ISO_DATE))
            put("tieneMasDeUnSol", if (tieneMasDeUnSol) 1 else 0)
            put("numeroDePlanetas", numeroDePlanetas)
            put("distanciaAlCentro", distanciaAlCentro)
            put("latitud", latitud)  // 🔹 Guardamos latitud
            put("longitud", longitud)  // 🔹 Guardamos longitud
        }
        val resultado = db.update("SISTEMA_SOLAR", valores, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun consultarSistemaSolarPorId(id: Int): SistemaSolar? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, nombre, fechaDescubrimiento, tieneMasDeUnSol, numeroDePlanetas, distanciaAlCentro, latitud, longitud FROM SISTEMA_SOLAR WHERE id = ?",
            arrayOf(id.toString())
        )

        return if (cursor.moveToFirst()) {
            try {
                val sistemaSolar = SistemaSolar(
                    cursor.getInt(0),
                    cursor.getString(1),
                    LocalDate.parse(cursor.getString(2), DateTimeFormatter.ISO_DATE),
                    cursor.getInt(3) == 1,
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getDouble(6),  // 🔹 Recuperar latitud
                    cursor.getDouble(7)   // 🔹 Recuperar longitud
                )
                cursor.close()
                db.close()
                sistemaSolar
            } catch (e: Exception) {
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
        val cursor = db.rawQuery("SELECT * FROM SISTEMA_SOLAR", null)

        while (cursor.moveToNext()) {
            val sistemaSolar = SistemaSolar(
                cursor.getInt(0),
                cursor.getString(1),
                LocalDate.parse(cursor.getString(2), DateTimeFormatter.ISO_DATE),
                cursor.getInt(3) == 1,
                cursor.getInt(4),
                cursor.getDouble(5),
                cursor.getDouble(6),  // 🔹 Latitud
                cursor.getDouble(7)   // 🔹 Longitud
            )
            lista.add(sistemaSolar)
        }
        cursor.close()
        db.close()
        return lista
    }

}
