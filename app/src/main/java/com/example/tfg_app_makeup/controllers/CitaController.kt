package com.example.tfg_app_makeup.controllers

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.services.CitaService

/**
 * Controlador para gestionar las operaciones relacionadas con las citas.
 * Se encarga de interactuar con el servicio de citas y realizar validaciones necesarias.
 *
 * @param context Contexto de la aplicación, necesario para inicializar el servicio.
 */
class CitaController(context: Context) {

    private val service = CitaService(context)

    /**
     * Obtiene todas las citas que tienen el estado "ACEPTADA".
     *
     * @return Lista de citas aceptadas.
     */
    fun obtenerCitasAceptadas(): List<Cita> {
        return service.obtenerTodos().filter { it.estado.uppercase() == "ACEPTADA" }
    }

    /**
     * Inserta una nueva cita en la base de datos si es válida.
     *
     * @param cita Objeto de tipo Cita que se desea insertar.
     * @return `true` si la cita fue insertada correctamente, `false` en caso contrario.
     */
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

    /**
     * Actualiza una cita existente en la base de datos si es válida.
     *
     * @param cita Objeto de tipo Cita que se desea actualizar.
     * @return `true` si la cita fue actualizada correctamente, `false` en caso contrario.
     */
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

    /**
     * Obtiene todas las citas asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario cuyas citas se desean obtener.
     * @return Lista de citas asociadas al usuario. Si ocurre un error, devuelve una lista vacía.
     */
    fun obtenerCitasPorUsuario(idUsuario: String): List<Cita> {
        return try {
            service.obtenerPorUsuario(idUsuario)
        } catch (e: Exception) {
            Log.e("CitaController", "Error al obtener citas por usuario: ${e.message}")
            emptyList()
        }
    }

    /**
     * Crea un objeto de tipo Cita con los datos proporcionados.
     *
     * @param tipoServicio Tipo de servicio de la cita.
     * @param fecha Fecha de la cita en formato "dd/MM/yyyy".
     * @param hora Hora de la cita.
     * @param direccion Dirección donde se realizará la cita.
     * @param idUsuario ID del usuario asociado a la cita.
     * @return Objeto de tipo Cita con los datos proporcionados.
     */
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

    /**
     * Obtiene todas las citas que tienen un estado específico.
     *
     * @param estado Estado de las citas que se desean obtener.
     * @return Lista de citas con el estado especificado.
     */
    fun obtenerPorEstado(estado: String): List<Cita> {
        return service.obtenerPorEstado(estado)
    }

}