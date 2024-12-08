package org.example

import java.io.File
import java.io.Serializable

class Planeta(
    var nombre: String,
    var diametro: Double,
    var periodoOrbital: Int,
    var esHabitable: Boolean,
    var temperaturaMedia: Float
) : Serializable {
    fun actualizarPlaneta(nuevoPlaneta: Planeta) {
        this.nombre = nuevoPlaneta.nombre
        this.diametro = nuevoPlaneta.diametro
        this.periodoOrbital = nuevoPlaneta.periodoOrbital
        this.esHabitable = nuevoPlaneta.esHabitable
        this.temperaturaMedia = nuevoPlaneta.temperaturaMedia
    }

    override fun toString(): String {
        return "Planeta(nombre='$nombre', diametro=$diametro, periodoOrbital=$periodoOrbital, esHabitable=$esHabitable, temperaturaMedia=$temperaturaMedia)"
    }

    companion object {
        private val planetas = mutableListOf<Planeta>()
        private val archivo = File("planetas.txt")

        fun crearPlaneta(planeta: Planeta) {
            planetas.add(planeta)
            guardarEnArchivo()
        }

        fun leerPlanetas(): List<Planeta> {
            cargarDesdeArchivo()
            return planetas
        }

        fun eliminarPlaneta(nombre: String) {
            val planeta = planetas.find { it.nombre == nombre }
            if (planeta != null) {
                planetas.remove(planeta)
                guardarEnArchivo()
            }
        }

        private fun guardarEnArchivo() {
            archivo.printWriter().use { out ->
                planetas.forEach {
                    out.println("${it.nombre}|${it.diametro}|${it.periodoOrbital}|${it.esHabitable}|${it.temperaturaMedia}")
                }
            }
        }

        private fun cargarDesdeArchivo() {
            if (!archivo.exists()) return
            planetas.clear()
            archivo.forEachLine {
                val parts = it.split("|")
                planetas.add(
                    Planeta(
                        parts[0],
                        parts[1].toDouble(),
                        parts[2].toInt(),
                        parts[3].toBoolean(),
                        parts[4].toFloat()
                    )
                )
            }
        }
    }
}