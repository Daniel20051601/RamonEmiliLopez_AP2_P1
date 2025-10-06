package edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Entradas")
data class EntradaEntity(
    @PrimaryKey(autoGenerate = true)
    val entradaId: Int = 0,
    val fecha: String,
    val nombreCliente: String,
    val cantidad: Int,
    val precio: Double
)
