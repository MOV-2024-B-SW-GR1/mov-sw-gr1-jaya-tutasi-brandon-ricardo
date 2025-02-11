package com.example.sistemasolar_planetas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperPlaneta (contexto: Context?) : SQLiteOpenHelper(
    contexto,
    "planetas_db",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaPlaneta = """
            CREATE TABLE PLANETA(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT,
                diametro REAL,
                periodoOrbital INTEGER,
                esHabitable INTEGER,
                temperaturaMedia REAL,
                sistema_solar_id INTEGER,
                FOREIGN KEY (sistema_solar_id) REFERENCES SISTEMA_SOLAR(id) ON DELETE CASCADE
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaPlaneta)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearPlaneta(
        nombre: String,
        diametro: Double,
        periodoOrbital: Int,
        esHabitable: Boolean,
        temperaturaMedia: Float,
        sistemaSolarId: Int
    ): Boolean {
        val db = writableDatabase
        val valoresGuardar = ContentValues().apply {
            put("nombre", nombre)
            put("diametro", diametro)
            put("periodoOrbital", periodoOrbital)
            put("esHabitable", if (esHabitable) 1 else 0)
            put("temperaturaMedia", temperaturaMedia)
            put("sistema_solar_id", sistemaSolarId)
        }
        val resultado = db.insert("PLANETA", null, valoresGuardar)
        db.close()
        return resultado != -1L
    }

    fun eliminarPlaneta(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("PLANETA", "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun actualizarPlaneta(
        id: Int,
        nombre: String,
        diametro: Double,
        periodoOrbital: Int,
        esHabitable: Boolean,
        temperaturaMedia: Float,
        sistemaSolarId: Int
    ): Boolean {
        val db = writableDatabase
        val valoresActualizar = ContentValues().apply {
            put("nombre", nombre)
            put("diametro", diametro)
            put("periodoOrbital", periodoOrbital)
            put("esHabitable", if (esHabitable) 1 else 0)
            put("temperaturaMedia", temperaturaMedia)
            put("sistema_solar_id", sistemaSolarId)
        }
        val resultado = db.update("PLANETA", valoresActualizar, "id=?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun consultarPlanetaPorId(id: Int): Planeta? {
        val db = readableDatabase
        val consulta =
            "SELECT nombre, diametro, periodoOrbital, esHabitable, temperaturaMedia, sistema_solar_id FROM PLANETA WHERE id = ?"
        val cursor = db.rawQuery(consulta, arrayOf(id.toString()))

        return if (cursor.moveToFirst()) {
            val nombre = cursor.getString(0)
            val diametro = cursor.getDouble(1)
            val periodoOrbital = cursor.getInt(2)
            val esHabitable = cursor.getInt(3) == 1
            val temperaturaMedia = cursor.getFloat(4)
            val sistemaSolarId = cursor.getInt(5) // Obtener sistemaSolarId

            cursor.close()
            db.close()

            Planeta(nombre, diametro, periodoOrbital, esHabitable, temperaturaMedia, sistemaSolarId)
        } else {
            cursor.close()
            db.close()
            null
        }
    }
}