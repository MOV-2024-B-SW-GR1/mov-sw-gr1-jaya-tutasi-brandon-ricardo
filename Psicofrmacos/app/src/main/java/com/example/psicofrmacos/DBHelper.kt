package com.example.psicofrmacos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Sentencias SQL para crear las tablas (solo si aún no existen)
    private val CREATE_TABLE_FAMILIAS = """
        CREATE TABLE IF NOT EXISTS Familias (
            id_familia INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre_familia TEXT,
            descripcion_familia TEXT
        )
    """

    private val CREATE_TABLE_GRUPOS = """
        CREATE TABLE IF NOT EXISTS Grupos (
            id_grupo INTEGER PRIMARY KEY AUTOINCREMENT,
            familia_id INTEGER,
            nombre_grupo TEXT,
            descripcion_grupo TEXT,
            FOREIGN KEY(familia_id) REFERENCES Familias(id_familia)
        )
    """

    private val CREATE_TABLE_MEDICAMENTOS = """
        CREATE TABLE IF NOT EXISTS Medicamentos (
            id_medicamento INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre_comercial TEXT,
            nombre_generico TEXT,
            descripcion TEXT,
            grupo_id INTEGER,
            familia_id INTEGER,
            contraindicaciones TEXT,
            dosis_recomendada TEXT,
            advertencia TEXT,
            FOREIGN KEY(grupo_id) REFERENCES Grupos(id_grupo),
            FOREIGN KEY(familia_id) REFERENCES Familias(id_familia)
        )
    """

    private val CREATE_TABLE_HISTORIAL = """
        CREATE TABLE IF NOT EXISTS Historial (
            id_historial INTEGER PRIMARY KEY AUTOINCREMENT,
            medicamento_id INTEGER,
            termino_busqueda TEXT,
            FOREIGN KEY(medicamento_id) REFERENCES Medicamentos(id_medicamento)
        )
    """

    private val CREATE_TABLE_FAVORITOS = """
        CREATE TABLE IF NOT EXISTS Favoritos (
            id_favorito INTEGER PRIMARY KEY AUTOINCREMENT,
            medicamento_id INTEGER,
            FOREIGN KEY(medicamento_id) REFERENCES Medicamentos(id_medicamento)
        )
    """

    companion object {
        const val DATABASE_NAME = "psicofarmacos.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear las tablas
        db.execSQL(CREATE_TABLE_FAMILIAS)
        db.execSQL(CREATE_TABLE_GRUPOS)
        db.execSQL(CREATE_TABLE_MEDICAMENTOS)
        db.execSQL(CREATE_TABLE_HISTORIAL)
        db.execSQL(CREATE_TABLE_FAVORITOS)

        // Insertar los datos directamente en las tablas
        insertarFamilias(db)
        insertarGrupos(db)
        insertarMedicamentos(db)
    }

    // Si se necesita actualizar la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Familias")
        db.execSQL("DROP TABLE IF EXISTS Grupos")
        db.execSQL("DROP TABLE IF EXISTS Medicamentos")
        db.execSQL("DROP TABLE IF EXISTS Historial")
        db.execSQL("DROP TABLE IF EXISTS Favoritos")
        onCreate(db)
    }

    // Método para insertar datos en la tabla Familias
    fun insertarFamilias(db: SQLiteDatabase) {
        val familias = listOf(
            arrayOf("Antidepresivos", "Medicamentos para tratar la depresión."),
            arrayOf("Ansiolíticos", "Medicamentos para la ansiedad."),
            arrayOf("Estimulantes", "Medicamentos para tratar el TDAH.")
        )
        familias.forEach { familia ->
            val values = ContentValues().apply {
                put("nombre_familia", familia[0])
                put("descripcion_familia", familia[1])
            }
            db.insert("Familias", null, values)
        }
    }

    // Método para insertar datos en la tabla Grupos
    fun insertarGrupos(db: SQLiteDatabase) {
        val grupos = listOf(
            // Asegúrate de que cada valor es del tipo correcto
            arrayOf(1, "Inhibidores Selectivos de la Recaptura de Serotonina", "Grupo de antidepresivos que aumentan la serotonina."),
            arrayOf(2, "Benzodiacepinas", "Grupo de ansiolíticos que actúan sobre el sistema nervioso."),
            arrayOf(3, "Metilfenidatos", "Grupo de estimulantes utilizados para el TDAH.")
        )

        grupos.forEach { grupo ->
            val values = ContentValues().apply {
                // Asegúrate de que familia_id sea un Int
                put("familia_id", grupo[0] as Int)

                // Asegúrate de que nombre_grupo y descripcion_grupo sean String
                put("nombre_grupo", grupo[1] as String)
                put("descripcion_grupo", grupo[2] as String)
            }

            // Inserta los valores en la base de datos
            db.insert("Grupos", null, values)
        }
    }


    // Método para insertar datos en la tabla Medicamentos
    fun insertarMedicamentos(db: SQLiteDatabase) {
        val medicamentos = listOf(
            arrayOf(1, "Zoloft", "Sertralina", "Antidepresivo utilizado para tratar la depresión mayor.", 101, 1, "No usar con inhibidores de la MAO.", "50 mg/día", "Puede causar efectos secundarios como insomnio o nauseas."),
            arrayOf(2, "Xanax", "Alprazolam", "Ansiolítico para el tratamiento de la ansiedad.", 102, 2, "Evitar en pacientes con antecedentes de abuso de sustancias.", "0.5 mg a 1 mg 3 veces al día", "Puede generar dependencia si se usa durante un largo periodo."),
            arrayOf(3, "Ritalin", "Metilfenidato", "Estimulante utilizado en el tratamiento del TDAH.", 103, 3, "Evitar en pacientes con enfermedades cardiovasculares graves.", "10 mg/día", "Debe ser usado con precaución en niños y adolescentes.")
        )
        medicamentos.forEach { medicamento ->
            val values = ContentValues().apply {
                put("id_medicamento", medicamento[0] as Int)
                put("nombre_comercial", medicamento[1] as String)
                put("nombre_generico", medicamento[2] as String)
                put("descripcion", medicamento[3] as String)
                put("grupo_id", medicamento[4] as Int)
                put("familia_id", medicamento[5] as Int)
                put("contraindicaciones", medicamento[6] as String)
                put("dosis_recomendada", medicamento[7] as String)
                put("advertencia", medicamento[8] as String)
            }
            db.insert("Medicamentos", null, values)
        }
    }
    // Método para obtener medicamentos por palabra clave
    fun obtenerMedicamentosPorPalabraClave(query: String): List<Medicamento> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Medicamentos WHERE nombre_comercial LIKE ? OR nombre_generico LIKE ? OR descripcion LIKE ? OR contraindicaciones LIKE ? OR dosis_recomendada LIKE ? OR advertencia LIKE ?",
            arrayOf("%$query%", "%$query%", "%$query%", "%$query%", "%$query%", "%$query%")
        )

        val medicamentos = mutableListOf<Medicamento>()

        if (cursor.moveToFirst()) {
            do {
                // Validar si la columna existe
                val idMedicamentoIndex = cursor.getColumnIndex("id_medicamento")
                val nombreComercialIndex = cursor.getColumnIndex("nombre_comercial")
                val nombreGenericoIndex = cursor.getColumnIndex("nombre_generico")
                val descripcionIndex = cursor.getColumnIndex("descripcion")
                val grupoIdIndex = cursor.getColumnIndex("grupo_id")
                val familiaIdIndex = cursor.getColumnIndex("familia_id")
                val contraindicacionesIndex = cursor.getColumnIndex("contraindicaciones")
                val dosisRecomendadaIndex = cursor.getColumnIndex("dosis_recomendada")
                val advertenciaIndex = cursor.getColumnIndex("advertencia")

                // Verificar que los índices de las columnas son válidos (mayores o iguales a 0)
                if (idMedicamentoIndex >= 0 && nombreComercialIndex >= 0 &&
                    nombreGenericoIndex >= 0 && descripcionIndex >= 0 &&
                    grupoIdIndex >= 0 && familiaIdIndex >= 0 &&
                    contraindicacionesIndex >= 0 && dosisRecomendadaIndex >= 0 &&
                    advertenciaIndex >= 0) {

                    // Obtener los valores solo si las columnas existen
                    val idMedicamento = cursor.getLong(idMedicamentoIndex)
                    val nombreComercial = cursor.getString(nombreComercialIndex)
                    val nombreGenerico = cursor.getString(nombreGenericoIndex)
                    val descripcion = cursor.getString(descripcionIndex)
                    val grupoId = cursor.getLong(grupoIdIndex)
                    val familiaId = cursor.getLong(familiaIdIndex)
                    val contraindicaciones = cursor.getString(contraindicacionesIndex)
                    val dosisRecomendada = cursor.getString(dosisRecomendadaIndex)
                    val advertencia = cursor.getString(advertenciaIndex)

                    // Crear y añadir el objeto Medicamento
                    medicamentos.add(Medicamento(idMedicamento, nombreComercial, nombreGenerico, descripcion, grupoId, familiaId, contraindicaciones, dosisRecomendada, advertencia))
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return medicamentos
    }

    // Método para obtener todos los medicamentos de la base de datos
    fun obtenerMedicamentos(): List<Medicamento> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Medicamentos", null)

        val medicamentos = mutableListOf<Medicamento>()

        if (cursor.moveToFirst()) {
            do {
                val idMedicamentoIndex = cursor.getColumnIndex("id_medicamento")
                val nombreComercialIndex = cursor.getColumnIndex("nombre_comercial")
                val nombreGenericoIndex = cursor.getColumnIndex("nombre_generico")
                val descripcionIndex = cursor.getColumnIndex("descripcion")
                val grupoIdIndex = cursor.getColumnIndex("grupo_id")
                val familiaIdIndex = cursor.getColumnIndex("familia_id")
                val contraindicacionesIndex = cursor.getColumnIndex("contraindicaciones")
                val dosisRecomendadaIndex = cursor.getColumnIndex("dosis_recomendada")
                val advertenciaIndex = cursor.getColumnIndex("advertencia")

                // Asegúrate de que todos los índices sean válidos (>= 0)
                if (idMedicamentoIndex >= 0 && nombreComercialIndex >= 0 &&
                    nombreGenericoIndex >= 0 && descripcionIndex >= 0 &&
                    grupoIdIndex >= 0 && familiaIdIndex >= 0 &&
                    contraindicacionesIndex >= 0 && dosisRecomendadaIndex >= 0 &&
                    advertenciaIndex >= 0) {
                    val idMedicamento = cursor.getLong(idMedicamentoIndex)
                    val nombreComercial = cursor.getString(nombreComercialIndex)
                    val nombreGenerico = cursor.getString(nombreGenericoIndex)
                    val descripcion = cursor.getString(descripcionIndex)
                    val grupoId = cursor.getLong(grupoIdIndex)
                    val familiaId = cursor.getLong(familiaIdIndex)
                    val contraindicaciones = cursor.getString(contraindicacionesIndex)
                    val dosisRecomendada = cursor.getString(dosisRecomendadaIndex)
                    val advertencia = cursor.getString(advertenciaIndex)

                    medicamentos.add(Medicamento(idMedicamento, nombreComercial, nombreGenerico, descripcion, grupoId, familiaId, contraindicaciones, dosisRecomendada, advertencia))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return medicamentos
    }
    fun obtenerMedicamentoPorNombre(nombreComercial: String): Medicamento {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Medicamentos WHERE nombre_comercial = ?", arrayOf(nombreComercial))

        var medicamento = Medicamento(0, "", "", "", 0, 0, "", "", "")

        if (cursor.moveToFirst()) {
            // Obtén los índices de las columnas
            val idMedicamentoIndex = cursor.getColumnIndex("id_medicamento")
            val nombreComercialIndex = cursor.getColumnIndex("nombre_comercial")
            val nombreGenericoIndex = cursor.getColumnIndex("nombre_generico")
            val descripcionIndex = cursor.getColumnIndex("descripcion")
            val grupoIdIndex = cursor.getColumnIndex("grupo_id")
            val familiaIdIndex = cursor.getColumnIndex("familia_id")
            val contraindicacionesIndex = cursor.getColumnIndex("contraindicaciones")
            val dosisRecomendadaIndex = cursor.getColumnIndex("dosis_recomendada")
            val advertenciaIndex = cursor.getColumnIndex("advertencia")

            // Verifica si los índices son válidos (mayores o iguales a 0)
            if (idMedicamentoIndex >= 0 && nombreComercialIndex >= 0 && nombreGenericoIndex >= 0 && descripcionIndex >= 0 &&
                grupoIdIndex >= 0 && familiaIdIndex >= 0 && contraindicacionesIndex >= 0 && dosisRecomendadaIndex >= 0 && advertenciaIndex >= 0) {

                // Accede a los valores solo si los índices son válidos
                val idMedicamento = cursor.getLong(idMedicamentoIndex)
                val nombreComercial = cursor.getString(nombreComercialIndex)
                val nombreGenerico = cursor.getString(nombreGenericoIndex)
                val descripcion = cursor.getString(descripcionIndex)
                val grupoId = cursor.getLong(grupoIdIndex)
                val familiaId = cursor.getLong(familiaIdIndex)
                val contraindicaciones = cursor.getString(contraindicacionesIndex)
                val dosisRecomendada = cursor.getString(dosisRecomendadaIndex)
                val advertencia = cursor.getString(advertenciaIndex)

                // Crear el objeto Medicamento
                medicamento = Medicamento(idMedicamento, nombreComercial, nombreGenerico, descripcion, grupoId, familiaId, contraindicaciones, dosisRecomendada, advertencia)
            } else {
                // Si no se encuentran índices válidos, puedes lanzar una excepción o manejarlo de alguna otra forma
                // Ejemplo: lanzar una excepción
                throw IllegalArgumentException("Algunas columnas no se encontraron en la base de datos.")
            }
        }
        cursor.close()
        return medicamento
    }


}