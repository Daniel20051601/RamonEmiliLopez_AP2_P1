package edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import kotlinx.coroutines.flow.Flow

@Dao
interface EntradaDao {
    @Query("SELECT * FROM Entradas ORDER BY entradaId DESC")
    fun observeAll(): Flow<List<EntradaEntity>>

    @Query("SELECT * FROM Entradas WHERE entradaId = :id")
    suspend fun getById(id: Int): EntradaEntity?

    @Query("SELECT * FROM Entradas WHERE nombreCliente COLLATE NOCASE = :name COLLATE NOCASE LIMIT 1")
    suspend fun getByNombreCiente(name: String): Entrada?

    @Upsert
    suspend fun upsert(jugador: EntradaEntity): Long

    @Delete
    suspend fun delete(jugador: EntradaEntity)

    @Query("DELETE FROM Entradas WHERE entradaId = :id")
    suspend fun delete(id: Int)

}