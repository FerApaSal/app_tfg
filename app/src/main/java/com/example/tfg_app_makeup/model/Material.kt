package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor
import java.io.Serializable

/**
 * Modelo de datos para representar un material de trabajo de la maquilladora.
 * Incluye información básica como nombre, tipo, cantidad y estado.
 */
data class Material(
    val id: String,
    var nombre: String,
    var descripcion: String,
    var tipo: String,
    var cantidad: Int,
    var estado: String,
    var imagenUrl: String? = null
): Serializable {
    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("descripcion", descripcion)
            put("tipo", tipo)
            put("cantidad", cantidad)
            put("estado", estado)
            put("imagenUrl", imagenUrl)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): Material {
            return Material(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")),
                estado = cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"))
            )
        }
    }
}
