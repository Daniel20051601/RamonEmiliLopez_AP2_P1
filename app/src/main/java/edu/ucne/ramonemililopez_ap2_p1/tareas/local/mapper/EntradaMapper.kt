package edu.ucne.ramonemililopez_ap2_p1.tareas.local.mapper

import edu.ucne.ramonemililopez_ap2_p1.domain.model.Entrada
import edu.ucne.ramonemililopez_ap2_p1.tareas.local.Entrada.EntradaEntity


fun EntradaEntity.toDomain() : Entrada = Entrada(
    entradaId = entradaId,
    fecha = fecha,
    nombreCliente = nombreCliente,
    cantidad = cantidad,
    precio = precio,
)

fun Entrada.toEntity() : EntradaEntity = EntradaEntity(
    entradaId = entradaId,
    fecha = fecha,
    nombreCliente = nombreCliente,
    cantidad = cantidad,
    precio = precio,
)