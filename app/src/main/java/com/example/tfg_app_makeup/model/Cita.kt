package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor

/**
 * Modelo de cita para servicios de maquillaje.
 *
 * @property id Identificador único.
 * @property tipoServicio Tipo de servicio solicitado (ej. "Novia", "Madrina", etc.).
 * @property fecha Fecha de la cita en formato "YYYY-MM-DD".
 * @property hora Hora de la cita en formato "HH:MM".
 * @property direccion Dirección del lugar de la cita.
 * @property estado Estado de la cita (pendiente, aceptada, rechazada...).
 * @property idUsuario ID del usuario que solicitó la cita.
 * @property nombreClienteManual Nombre del cliente ingresado manualmente (opcional).
 * @property telefonoClienteManual Teléfono del cliente ingresado manualmente (opcional).
 */
data class Cita(
    val id: String,
    var tipoServicio: String,
    var fecha: String,
    var hora: String,
    var direccion: String,
    var estado: String = "PENDIENTE",
    val idUsuario: String,

    var nombreClienteManual: String? = null,
    var telefonoClienteManual: String? = null
) {
    /**
     * Convierte el objeto `Cita` a un objeto `ContentValues` para operaciones con SQLite.
     * @return Objeto `ContentValues` con los datos de la cita.
     */
    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("tipoServicio", tipoServicio)
            put("fecha", fecha)
            put("hora", hora)
            put("direccion", direccion)
            put("estado", estado)
            put("idUsuario", idUsuario)

            put("nombreClienteManual", nombreClienteManual)
            put("telefonoClienteManual", telefonoClienteManual)
        }
    }

    companion object {
        /**
         * Crea un objeto `Cita` a partir de un cursor de SQLite.
         * @param cursor Cursor que contiene los datos de la cita.
         * @return Objeto `Cita` con los datos extraídos del cursor.
         */
        fun fromCursor(cursor: Cursor): Cita {
            return Cita(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow("tipoServicio")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion")),
                estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                idUsuario = cursor.getString(cursor.getColumnIndexOrThrow("idUsuario")),

                nombreClienteManual = cursor.getString(cursor.getColumnIndexOrThrow("nombreClienteManual")),
                telefonoClienteManual = cursor.getString(cursor.getColumnIndexOrThrow("telefonoClienteManual")),
            )
        }
    }
}