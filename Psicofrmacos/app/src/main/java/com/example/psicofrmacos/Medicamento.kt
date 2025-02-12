package com.example.psicofrmacos

class Medicamento (
    val idMedicamento: Long,
    val nombreComercial: String,
    val nombreGenerico: String,
    val descripcion: String,
    val grupoId: Long,
    val familiaId: Long,
    val contraindicaciones: String,
    val dosisRecomendada: String,
    val advertencia: String
)