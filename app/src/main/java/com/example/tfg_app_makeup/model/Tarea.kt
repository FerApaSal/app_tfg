package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor
import java.io.Serializable

/**
 * Modelo de datos para representar una tarea.
 * Incluye información básica como título, descripción, prioridad y estado de completado.
 *
 * @property id Identificador único de la tarea.
 * @property titulo Título de la tarea.
 * @property descripcion Descripción detallada de la tarea.
 * @property prioridad Nivel de prioridad de la tarea (ej. "Alta", "Media", "Baja").
 * @property completada Indica si la tarea está completada (true) o no (false).
 */
data class Tarea(
    val id: String,
    var titulo: String,
    var descripcion: String,
    var prioridad: String,
    var completada: Boolean
) : Serializable {

    /**
     * Convierte el objeto `Tarea` a un objeto `ContentValues` para operaciones con SQLite.
     * @return Objeto `ContentValues` con los datos de la tarea.
     */
    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("titulo", titulo)
            put("descripcion", descripcion)
            put("prioridad", prioridad)
            put("completada", if (completada) 1 else 0) // 1 para true, 0 para false
        }
    }

    companion object {
        /**
         * Crea un objeto `Tarea` a partir de un cursor de SQLite.
         * @param cursor Cursor que contiene los datos de la tarea.
         * @return Objeto `Tarea` con los datos extraídos del cursor.
         */
        fun fromCursor(cursor: Cursor): Tarea {
            return Tarea(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                prioridad = cursor.getString(cursor.getColumnIndexOrThrow("prioridad")),
                completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada")) == 1 // true si es 1, false si es 0
            )
        }
    }
}