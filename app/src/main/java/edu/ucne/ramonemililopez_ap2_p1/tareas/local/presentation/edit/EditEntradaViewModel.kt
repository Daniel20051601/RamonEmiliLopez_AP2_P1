package edu.ucne.ramonemililopez_ap2_p1.tareas.local.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.domain.usecase.DeleteEntradaUseCase
import edu.ucne.ramonemililopez_ap2_p1.domain.usecase.GetEntradaUseCase
import edu.ucne.ramonemililopez_ap2_p1.domain.usecase.UpsertEntradaUseCase
import edu.ucne.ramonemililopez_ap2_p1.domain.validation.EntradaValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditEntradaViewModel @Inject constructor(
    private val upsertEntradaUseCase: UpsertEntradaUseCase,
    private val getEntradaUseCase: GetEntradaUseCase,
    private val deleteEntradaUseCase: DeleteEntradaUseCase,
    private val entradaValidator: EntradaValidator
): ViewModel() {
    private val _state = MutableStateFlow(EditEntradasUiState())
    val state: StateFlow<EditEntradasUiState> = _state.asStateFlow()

    fun onEvent(event: EditJugadorEvent) {
        when(event) {
            is EditJugadorEvent.Load -> onLoad(event.entradaId)
            is EditJugadorEvent.NombreChanged -> _state.update {
                it.copy(nombreCliente = event.nombreCliente, nombreClienteError = null)
            }
            is EditJugadorEvent.FechaChanged -> _state.update {
                it.copy(fecha = event.fecha)
            }
            is EditJugadorEvent.CantidadChanged -> _state.update {
                it.copy(
                    cantidad = event.cantidad.toString(),
                    cantidadError = null
                )
            }
            is EditJugadorEvent.PrecioChanged -> _state.update {
                it.copy(
                    precio = event.precio.toString(),
                    precioError = null
                )
            }
            EditJugadorEvent.Save -> onSave()
            EditJugadorEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(entradaId: Int?) {
        if (entradaId == null || entradaId == 0) {
            val fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            _state.update {
                it.copy(
                    isLoading = false,
                    entradaId = 0,
                    fecha = fechaActual,
                    nombreCliente = "",
                    cantidad = "",
                    precio = "",
                    errorMessage = null,
                    isSaved = false,
                    isSaving = false,
                    isDeleting = false,
                    canBeDeleted = false
                )
            }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val entrada = getEntradaUseCase(entradaId)
                _state.update {
                    if (entrada != null) {
                        it.copy(
                            entradaId = entrada.entradaId,
                            fecha = entrada.fecha,
                            nombreCliente = entrada.nombreCliente,
                            cantidad = entrada.cantidad.toString(),
                            precio = entrada.precio.toString(),
                            isLoading = false,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    } else {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Entrada con ID $entradaId no encontrada.",
                            entradaId = 0,
                            fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            nombreCliente = "",
                            cantidad = "",
                            precio = "",
                            canBeDeleted = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido al cargar la entrada.",
                        canBeDeleted = false
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombreCliente = state.value.nombreCliente
        val cantidadStr = state.value.cantidad
        val precioStr = state.value.precio
        val fecha = state.value.fecha
        val currentEntradaId = state.value.entradaId

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSaving = true,
                    nombreClienteError = null,
                    cantidadError = null,
                    precioError = null,
                    errorMessage = null
                )
            }

            val nombreValidation = entradaValidator.validateNombre(nombreCliente)
            val cantidadValidation = entradaValidator.validateCantidad(cantidadStr)
            val nombreUnicoValidation = entradaValidator.validateNombreClienteUnico(nombreCliente, currentEntradaId)
            val precioValidation = entradaValidator.validatePrecio(precioStr)

            if (!nombreValidation.isValid || !cantidadValidation.isValid || !nombreUnicoValidation.isValid || !precioValidation.isValid) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreClienteError = nombreValidation.error ?: nombreUnicoValidation.error,
                        cantidadError = cantidadValidation.error,
                        precioError = precioValidation.error,
                        errorMessage = "Por favor, corrija los errores en el formulario."
                    )
                }
                return@launch
            }

            try {
                val entrada = Entrada(
                    entradaId = currentEntradaId,
                    nombreCliente = nombreCliente,
                    fecha = fecha,
                    cantidad = cantidadStr.toInt(),
                    precio = precioStr.toDouble()
                )

                val result = upsertEntradaUseCase(entrada)
                result.onSuccess { newId ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            isSaved = true,
                            entradaId = if (currentEntradaId == 0) newId else currentEntradaId,
                            errorMessage = null,
                            canBeDeleted = true
                        )
                    }
                }.onFailure { e ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = e.message ?: "Error desconocido al guardar la entrada."
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "Error inesperado al procesar la entrada."
                    )
                }
            }
        }
    }


    private fun onDelete() {
        val entradaId = _state.value.entradaId
        if (entradaId == 0) {
            _state.update { it.copy(errorMessage = "No se puede eliminar una entrada sin ID válido.") }
            return
        }

        _state.update { it.copy(isDeleting = true, isSaved = false, errorMessage = null) }

        viewModelScope.launch {
            try {
                deleteEntradaUseCase(entradaId)
                _state.update {
                    it.copy(
                        isDeleting = false,
                        isSaved = true,
                        errorMessage = null,
                        entradaId = 0,
                        fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        nombreCliente = "",
                        cantidad = "",
                        precio = "",
                        canBeDeleted = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "Error desconocido al eliminar la entrada."
                    )
                }
            }
        }
    }
}
