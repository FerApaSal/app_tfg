package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor

data class Usuario(
    val id: String,
    var nombre: String,
    var apellido: String,
    var correo: String,
    var clave: String,
    var rol: String,
    var imagenUrl: String? = null
) {
    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("nombre", nombre)
            put("apellido", apellido)
            put("correo", correo)
            put("clave", clave)
            put("rol", rol)
            put("imagenUrl", imagenUrl)
        }
    }

    companion object {
        fun fromCursor(cursor: Cursor): Usuario {
            return Usuario(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                clave = cursor.getString(cursor.getColumnIndexOrThrow("clave")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")),
                imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"))
            )
        }
    }
}
