package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.domain.usecase.ObserveJugadorUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListEntradaViewModel @Inject constructor(
    private val observeJugadorUseCase: ObserveJugadorUseCase
) : ViewModel() {

    private val _filterType = MutableStateFlow(FilterType.NOMBRE_CLIENTE)
    private val _filterValue = MutableStateFlow("")
    private val _filterDate = MutableStateFlow("")
    private val _uiState = MutableStateFlow(ListEntradaUiState())

    private var allEntradas = listOf<Entrada>()

    val uiState: StateFlow<ListEntradaUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            observeJugadorUseCase().collect { entradas ->
                allEntradas = entradas
                applyFilters()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    fun onFilterChange(filterType: FilterType, filterValue: String, filterDate: String) {
        viewModelScope.launch {
            _filterType.value = filterType
            _filterValue.value = filterValue
            _filterDate.value = filterDate
            applyFilters()
        }
    }
    private fun applyFilters() {
        val filteredEntradas = allEntradas.filter { entrada ->
            val matchesFilterValue = when (_filterType.value) {
                FilterType.ENTRADA_ID -> {
                    if (_filterValue.value.isBlank()) true
                    else _filterValue.value.toIntOrNull()?.let { entrada.entradaId == it } ?: false
                }
                FilterType.NOMBRE_CLIENTE -> {
                    if (_filterValue.value.isBlank()) true
                    else entrada.nombreCliente.contains(_filterValue.value, ignoreCase = true)
                }
                FilterType.CANTIDAD -> {
                    if (_filterValue.value.isBlank()) true
                    else _filterValue.value.toIntOrNull()?.let { entrada.cantidad == it } ?: false
                }
                FilterType.PRECIO -> {
                    if (_filterValue.value.isBlank()) true
                    else _filterValue.value.toDoubleOrNull()?.let {
                        entrada.precio == it || entrada.precio.toString().contains(_filterValue.value)
                    } ?: false
                }
            }

            val matchesDate = if (_filterDate.value.isBlank()) true
            else entrada.fecha.contains(_filterDate.value)

            matchesFilterValue && matchesDate
        }

        _uiState.update { it.copy(entradas = filteredEntradas) }
    }
}
