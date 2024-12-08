package org.example

import java.io.File
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SistemaSolar(
    var nombre: String,
    var fechaDescubrimiento: LocalDate,
    var esHabitado: Boolean,
    var numeroDePlanetas: Int,
    var distanciaAlCentro: Double
) : Serializable {
    val planetas = mutableListOf<Planeta>()

    // Método para actualizar un sistema solar completo
    fun actualizarSistemaSolar(
        nuevoNombre: String,
        nuevaFechaDescubrimiento: LocalDate,
        nuevoEsHabitado: Boolean,
        nuevoNumeroDePlanetas: Int,
        nuevaDistanciaAlCentro: Double
    ) {
        this.nombre = nuevoNombre
        this.fechaDescubrimiento = nuevaFechaDescubrimiento
        this.esHabitado = nuevoEsHabitado
        this.numeroDePlanetas = nuevoNumeroDePlanetas
        this.distanciaAlCentro = nuevaDistanciaAlCentro
    }

    fun agregarPlaneta(planeta: Planeta) {
        planetas.add(planeta)
    }

    fun eliminarPlaneta(nombre: String) {
        val planeta = planetas.find { it.nombre == nombre }
        if (planeta != null) {
            planetas.remove(planeta)
        }
    }

    override fun toString(): String {
        return "Sistemas Solares(nombre='$nombre', fechaDescubrimiento=$fechaDescubrimiento, esHabitado=$esHabitado, numeroDePlanetas=$numeroDePlanetas, distanciaAlCentro=$distanciaAlCentro, planetas=${planetas.map { it.nombre }})"
    }

    companion object {
        private val sistemasSolares = mutableListOf<SistemaSolar>()
        private val archivo = File("sistemas_solares.txt")
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun crearSistemaSolar(sistema: SistemaSolar) {
            sistemasSolares.removeIf { it.nombre == sistema.nombre }
            sistemasSolares.add(sistema)
            guardarEnArchivo()
        }

        fun leerSistemasSolares(): List<SistemaSolar> {
            cargarDesdeArchivo()
            return sistemasSolares
        }

        fun buscarSistemaSolar(nombre: String): SistemaSolar? {
            return sistemasSolares.find { it.nombre == nombre }
        }

        fun eliminarSistemaSolar(nombre: String) {
            val sistema = sistemasSolares.find { it.nombre == nombre }
            if (sistema != null) {
                sistemasSolares.remove(sistema)
                guardarEnArchivo()
            }
        }

        private fun guardarEnArchivo() {
            archivo.printWriter().use { out ->
                sistemasSolares.forEach { sistema ->
                    val planetas = sistema.planetas.joinToString(";") {
                        "${it.nombre},${it.diametro},${it.periodoOrbital},${it.esHabitable},${it.temperaturaMedia}"
                    }
                    out.println("${sistema.nombre}|${sistema.fechaDescubrimiento}|${sistema.esHabitado}|${sistema.numeroDePlanetas}|${sistema.distanciaAlCentro}|$planetas")
                }
            }
        }

        private fun cargarDesdeArchivo() {
            if (!archivo.exists()) return
            sistemasSolares.clear()
            archivo.forEachLine { line ->
                try {
                    val parts = line.split("|")
                    if (parts.size < 5) {
                        println("Línea mal formateada: $line")
                        return@forEachLine
                    }
                    val sistema = SistemaSolar(
                        parts[0],
                        LocalDate.parse(parts[1], formatter),
                        parts[2].toBoolean(),
                        parts[3].toInt(),
                        parts[4].toDouble()
                    )
                    if (parts.size > 5) {
                        val planetas = parts[5].split(";")
                        planetas.forEach { planetaData ->
                            val planetaParts = planetaData.split(",")
                            if (planetaParts.size == 5) {
                                sistema.planetas.add(
                                    Planeta(
                                        planetaParts[0],
                                        planetaParts[1].toDouble(),
                                        planetaParts[2].toInt(),
                                        planetaParts[3].toBoolean(),
                                        planetaParts[4].toFloat()
                                    )
                                )
                            } else {
                                println("Datos del planeta: $planetaData")
                            }
                        }
                    }
                    sistemasSolares.add(sistema)
                } catch (e: Exception) {
                    println("Error procesando línea: $line")
                    e.printStackTrace()
                }
            }
        }
    }
}