package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.edit

interface EditJugadorEvent {
    data class Load(val entradaId: Int?) : EditJugadorEvent
    data class NombreChanged(val nombreCliente: String) : EditJugadorEvent
    data class FechaChanged(val fecha: String) : EditJugadorEvent
    data class CantidadChanged(val cantidad: Int) : EditJugadorEvent
    data class PrecioChanged(val precio: Double) : EditJugadorEvent
    data object Save: EditJugadorEvent
    data object Delete: EditJugadorEvent
}