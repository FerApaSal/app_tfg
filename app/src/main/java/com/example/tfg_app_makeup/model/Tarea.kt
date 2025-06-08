package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor
import java.io.Serializable

data class Tarea(
    val id: String,
    var titulo: String,
    var descripcion: String,
    var prioridad: String,
    var completada: Boolean
): Serializable {
    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("titulo", titulo)
            put("descripcion", descripcion)
            put("prioridad", prioridad)
            put("completada", if (completada) 1 else 0)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): Tarea {
            return Tarea(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                prioridad = cursor.getString(cursor.getColumnIndexOrThrow("prioridad")),
                completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada")) == 1
            )
        }
    }
}
