package org.example

import java.time.LocalDate
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("\nSeleccione una opción:")
        println("1. Crear Sistema Solar")
        println("2. Leer Sistemas Solares")
        println("3. Actualizar Sistema Solar")
        println("4. Eliminar Sistema Solar")
        println("5. Crear Planeta")
        println("6. Leer Planetas de un Sistema Solar")
        println("7. Eliminar Planeta de un Sistema Solar")
        println("0. Salir")

        opcion = scanner.nextInt()
        scanner.nextLine()

        when (opcion) {
            1 -> {
                println("Ingrese el nombre del sistema solar:")
                val nombre = scanner.nextLine()
                println("Ingrese la fecha de descubrimiento (yyyy-MM-dd):")
                val fechaDescubrimiento = LocalDate.parse(scanner.nextLine())
                println("¿Es habitado? (true/false):")
                val esHabitado = scanner.nextBoolean()
                println("Ingrese el número de planetas:")
                val numeroDePlanetas = scanner.nextInt()
                println("Ingrese la distancia al centro:")
                val distanciaAlCentro = scanner.nextDouble()
                SistemaSolar.crearSistemaSolar(
                    SistemaSolar(nombre, fechaDescubrimiento, esHabitado, numeroDePlanetas, distanciaAlCentro)
                )
                println("Sistema Solar creado.")
            }
            2 -> {
                val sistemas = SistemaSolar.leerSistemasSolares()
                println("Sistemas Solares:")
                sistemas.forEach { println(it) }
            }
            3 -> {
                println("Ingrese el nombre del sistema solar a actualizar:")
                val nombre = scanner.nextLine()
                val sistema = SistemaSolar.buscarSistemaSolar(nombre)
                if (sistema != null) {
                    println("Ingrese los nuevos datos:")
                    println("Ingrese el nuevo nombre:")
                    val nuevoNombre = scanner.nextLine()
                    println("Ingrese la nueva fecha de descubrimiento (yyyy-MM-dd):")
                    val nuevaFechaDescubrimiento = LocalDate.parse(scanner.nextLine())
                    println("¿Es habitado? (true/false):")
                    val nuevoEsHabitado = scanner.nextBoolean()
                    println("Ingrese el nuevo número de planetas:")
                    val nuevoNumeroDePlanetas = scanner.nextInt()
                    println("Ingrese la nueva distancia al centro:")
                    val nuevaDistanciaAlCentro = scanner.nextDouble()
                    sistema.actualizarSistemaSolar(
                        nuevoNombre,
                        nuevaFechaDescubrimiento,
                        nuevoEsHabitado,
                        nuevoNumeroDePlanetas,
                        nuevaDistanciaAlCentro
                    )
                    SistemaSolar.crearSistemaSolar(sistema) // Reescribir cambios
                    println("Sistema Solar actualizado.")
                } else {
                    println("Sistema Solar no encontrado.")
                }
            }
            4 -> {
                println("Ingrese el nombre del sistema solar a eliminar:")
                val nombre = scanner.nextLine()
                SistemaSolar.eliminarSistemaSolar(nombre)
                println("Sistema Solar eliminado.")
            }
            5 -> {
                println("Ingrese el nombre del sistema solar al que desea agregar el planeta:")
                val nombreSistema = scanner.nextLine()
                val sistema = SistemaSolar.buscarSistemaSolar(nombreSistema)
                if (sistema != null) {
                    println("Ingrese el nombre del planeta:")
                    val nombre = scanner.nextLine()
                    println("Ingrese el diámetro:")
                    val diametro = scanner.nextDouble()
                    println("Ingrese el período orbital:")
                    val periodoOrbital = scanner.nextInt()
                    println("¿Es habitable? (true/false):")
                    val esHabitable = scanner.nextBoolean()
                    println("Ingrese la temperatura media:")
                    val temperaturaMedia = scanner.nextFloat()
                    val planeta = Planeta(nombre, diametro, periodoOrbital, esHabitable, temperaturaMedia)
                    sistema.agregarPlaneta(planeta)
                    SistemaSolar.crearSistemaSolar(sistema) // Actualizar el sistema solar
                    println("Planeta agregado.")
                } else {
                    println("Sistema Solar no encontrado.")
                }
            }
            6 -> {
                println("Ingrese el nombre del sistema solar:")
                val nombreSistema = scanner.nextLine()
                val sistema = SistemaSolar.buscarSistemaSolar(nombreSistema)
                if (sistema != null) {
                    println("Planetas en el sistema solar $nombreSistema:")
                    sistema.planetas.forEach { println(it) }
                } else {
                    println("Sistema Solar no encontrado.")
                }
            }
            7 -> {
                println("Ingrese el nombre del sistema solar:")
                val nombreSistema = scanner.nextLine()
                val sistema = SistemaSolar.buscarSistemaSolar(nombreSistema)
                if (sistema != null) {
                    println("Ingrese el nombre del planeta a eliminar:")
                    val nombrePlaneta = scanner.nextLine()
                    sistema.eliminarPlaneta(nombrePlaneta)
                    SistemaSolar.crearSistemaSolar(sistema) // Actualizar el sistema solar
                    println("Planeta eliminado.")
                } else {
                    println("Sistema Solar no encontrado.")
                }
            }
        }
    } while (opcion != 0)
    println("Programa finalizado.")
}