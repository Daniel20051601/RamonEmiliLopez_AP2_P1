package edu.ucne.ramonemililopez_ap2_p1.domain.validation

import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import javax.inject.Inject

class EntradaValidator @Inject constructor(
    private val repository: EntradaRepository
){
    fun validateNombre(nombreCliente: String): ValidationResult {
        if (nombreCliente.isBlank()) {
            return ValidationResult(isValid = false, error = "Debe agregar el nombre del Cliente.")
        }
        if (nombreCliente.length < 3) {
            return ValidationResult(isValid = false, error = "El nombre es demasiado corto.")
        }
        if (nombreCliente.length > 100) {
            return ValidationResult(isValid = false, error = "El nombre no puede contener mas de 100 caracteres.")
        }
        return ValidationResult(isValid = true)
    }

    fun validateCantidad(cantidadStr: String): ValidationResult {
        if (cantidadStr.isBlank()) {
            return ValidationResult(isValid = false, error = "Debe agregar la cantidad.")
        }
        val cantidad = cantidadStr.toIntOrNull()
        if (cantidad == null) {
            return ValidationResult(isValid = false, error = "Debe ser un número entero.")
        }
        if (cantidad < 0) {
            return ValidationResult(isValid = false, error = "Debe ser un valor positivo.")
        }
        if (cantidad > 1000) {
            return ValidationResult(isValid = false, error = "El valor no puede ser mayor a 1000.")
        }
        return ValidationResult(isValid = true)
    }

    suspend fun validateNombreClienteUnico(nombreCliente: String, currentEntradaId: Int? = null): ValidationResult {
        val basicValidation = validateNombre(nombreCliente)
        if (!basicValidation.isValid) {
            return basicValidation
        }

        val existingCliente = repository.getByNombreCiente(nombreCliente)

        if (existingCliente != null) {
            if (currentEntradaId != null && existingCliente.entradaId == currentEntradaId) {
                return ValidationResult(isValid = true)
            } else {
                return ValidationResult(isValid = false, error = "Ya existe una entrada con este nombre de cliente.")
            }
        }
        return ValidationResult(isValid = true)
    }

    fun validatePrecio(precioStr: String): ValidationResult {
        if(precioStr.isBlank()){
            return ValidationResult(isValid = false, error = "Debe agregar el precio.")
        }
        val precio = precioStr.toDoubleOrNull()
        if (precio == null) {
            return ValidationResult(isValid = false, error = "Debe ser un número válido.")
        }
        if (precio < 0.0) {
            return ValidationResult(isValid = false, error = "El precio no puede ser negativo.")
        }
        if (precio > 100000.0) {
            return ValidationResult(isValid = false, error = "El precio no puede ser mayor a 10,000.")
        }
        return ValidationResult(isValid = true)
    }


}