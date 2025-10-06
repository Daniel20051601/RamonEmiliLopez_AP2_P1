package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.edit

data class EditEntradasUiState(
    val entradaId: Int = 0,
    val fecha: String = "",
    val nombreCliente: String = "",
    val cantidad: String = "",
    val precio: String = "",
    val nombreClienteError: String? = null,
    val cantidadError: String? = null,
    val precioError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
    val canBeDeleted: Boolean = false
)
