package edu.ucne.ramonemililopez_ap2_p1.domain.usecase

import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import javax.inject.Inject

class GetEntradaUseCase @Inject constructor(
    private val repository: EntradaRepository
) {
    suspend operator fun invoke(id: Int) = repository.getById(id)
}