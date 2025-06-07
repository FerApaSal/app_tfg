package com.example.tfg_app_makeup.controllers

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.services.CitaService

class CitaController(context: Context) {

    private val service = CitaService(context)

    fun obtenerTodos(): List<Cita> {
        return service.obtenerTodos()
    }

    fun obtenerId(id: String): Cita? {
        return service.obtenerPorId(id)
    }

    fun obtenerPorUsuario(idUsuario: String): List<Cita> {
        return service.obtenerPorUsuario(idUsuario)
    }

    fun insertar(cita: Cita): Boolean {
        return if (CitaHelper.validar(cita)) {
            val result = service.insertar(cita)
            Log.d("CitaController", "Insert resultado: $result")
            result
        } else {
            Log.w("CitaController", "Cita no válida, inserción cancelada.")
            false
        }
    }

    fun actualizar(cita: Cita): Boolean {
        return if (CitaHelper.validar(cita)) {
            val result = service.actualizar(cita)
            Log.d("CitaController", "Update resultado: $result")
            result
        } else {
            Log.w("CitaController", "Cita no válida, actualización cancelada.")
            false
        }
    }

    fun eliminarPorId(id: String): Boolean {
        val result = service.eliminarPorId(id)
        Log.d("CitaController", "Delete resultado: $result")
        return result
    }

    fun obtenerCitasPorUsuario(idUsuario: String): List<Cita> {
        return try {
            service.obtenerPorUsuario(idUsuario)
        } catch (e: Exception) {
            Log.e("CitaController", "Error al obtener citas por usuario: ${e.message}")
            emptyList()
        }
    }

    fun crearCita(
        tipoServicio: String,
        fecha: String,
        hora: String,
        direccion: String,
        idUsuario: String
    ): Cita {
        return Cita(
            id = java.util.UUID.randomUUID().toString(),
            tipoServicio = tipoServicio,
            fecha = fecha,
            hora = hora,
            direccion = direccion,
            idUsuario = idUsuario
        )
    }

}
