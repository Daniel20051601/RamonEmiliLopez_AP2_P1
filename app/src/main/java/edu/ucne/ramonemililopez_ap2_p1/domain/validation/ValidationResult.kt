package edu.ucne.ramonemililopez_ap2_p1.domain.validation

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)
