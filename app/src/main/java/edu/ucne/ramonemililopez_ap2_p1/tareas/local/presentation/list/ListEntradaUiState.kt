package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.list

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada

data class ListEntradaUiState (
    val entradas: List<Entrada> = emptyList(),
    val isLoading: Boolean = false
)