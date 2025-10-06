package edu.ucne.ramonemililopez_ap2_p1.tareas.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada.EntradaDao
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.database.AppDataBase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDb(@ApplicationContext appContext: Context) : AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "Entrada.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideEntradaDao(db: AppDataBase): EntradaDao {
        return db.entradaDao()
    }
}