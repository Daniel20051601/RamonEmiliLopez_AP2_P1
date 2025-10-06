package edu.ucne.ramonemililopez_ap2_p1.domain.usecase

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import javax.inject.Inject

class UpsertEntradaUseCase @Inject constructor(
    private val repository: EntradaRepository
) {
    suspend operator fun invoke(entrada: Entrada): Result<Int> {
        return try {
            val id = repository.upsert(entrada)
            Result.success(id.toInt())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}