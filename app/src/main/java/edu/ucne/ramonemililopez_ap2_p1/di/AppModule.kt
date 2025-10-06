package edu.ucne.ramonemililopez_ap2_p1.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent
import edu.ucne.ramonemililopez_ap2_p1.domain.repository.EntradaRepository
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.repository.EntradaRepositoryImpl


@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindJugadorRepository(
        EntradaRepositoryImpl: EntradaRepositoryImpl
    ): EntradaRepository
}