package edu.ucne.ramonemililopez_ap2_p1.domain.repository

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import kotlinx.coroutines.flow.Flow

interface EntradaRepository {
    suspend fun upsert(Entrada: Entrada): Long
    suspend fun delete(Entrada: Entrada)
    suspend fun deleteById(id: Int)
    suspend fun getById(id: Int): Entrada?
    suspend fun getByNombreCiente(nombre: String): Entrada?
    fun observeAll(): Flow<List<Entrada>>
}