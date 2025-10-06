package edu.ucne.ramonemililopez_ap2_p1.tareas.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada.EntradaDao
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada.EntradaEntity

@Database(
    entities = [
        EntradaEntity::class,
    ],
    version = 1,
    exportSchema = false,
)

abstract class AppDataBase : RoomDatabase() {
    abstract fun entradaDao(): EntradaDao
}