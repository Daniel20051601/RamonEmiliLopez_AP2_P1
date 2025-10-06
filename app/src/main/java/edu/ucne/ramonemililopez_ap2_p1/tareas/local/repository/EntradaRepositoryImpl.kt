package edu.ucne.ramonemililopez_ap2_p1.tareas.local.repository

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada.EntradaDao
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.mapper.toDomain
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EntradaRepositoryImpl @Inject constructor (
    private val entradaDao: EntradaDao
): EntradaRepository {
    override suspend fun upsert(Entrada: Entrada): Long {
        return entradaDao.upsert(Entrada.toEntity())
    }
    override suspend fun delete(Entrada: Entrada) {
        entradaDao.delete(Entrada.toEntity())
    }
    override suspend fun deleteById(id: Int) {
        entradaDao.delete(id)
    }
    override suspend fun getById(id: Int): Entrada? {
        return entradaDao.getById(id)?.toDomain()
    }

    override suspend fun getByNombreCiente(nombre: String): Entrada? {
        return entradaDao.getByNombreCiente(nombre)
    }

    override  fun observeAll(): Flow<List<Entrada>> {
        return entradaDao.observeAll().map { list ->
            list.map { it.toDomain() }
        }
    }

}