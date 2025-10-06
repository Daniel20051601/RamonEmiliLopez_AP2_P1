package edu.ucne.ramonemililopez_ap2_p1.domain.usecase

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveJugadorUseCase @Inject constructor(
    private val repository: EntradaRepository
) {
    operator fun invoke(): Flow<List<Entrada>> {
        return repository.observeAll()
    }
}