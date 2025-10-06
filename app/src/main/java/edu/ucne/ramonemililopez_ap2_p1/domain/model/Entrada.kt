package edu.ucne.ramonemililopez_ap2_p1.domain.model

data class Entrada(
    val entradaId: Int = 0,
    val fecha: String,
    val nombreCliente: String,
    val cantidad: Int,
    val precio: Double,
)
