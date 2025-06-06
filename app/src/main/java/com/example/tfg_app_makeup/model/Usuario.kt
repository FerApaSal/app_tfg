package com.example.tfg_app_makeup.model

import android.content.ContentValues
import android.database.Cursor
import com.example.tfg_app_makeup.utils.CrudEntity

/**
 * Modelo de usuario del sistema.
 *
 * @property id Identificador único (UUID).
 * @property nombre Nombre del usuario.
 * @property apellidos Apellidos completos.
 * @property telefono Número de teléfono.
 * @property email Correo electrónico único.
 * @property password Contraseña cifrada.
 * @property rol Rol del usuario ("CLIENTE" o "ADMIN").
 * @property imagenUrl URL de la imagen del usuario (opcional).
 */
data class Usuario(
    val id: String,
    var nombre: String,
    var apellidos: String,
    var telefono: String,
    var email: String,
    var password: String,
    var rol: String,
    var imagenUrl: String? = null

) : CrudEntity {

    override fun toContentValues(): ContentValues {
        val values = ContentValues()
        values.put("id", id)
        values.put("nombre", nombre)
        values.put("apellidos", apellidos)
        values.put("telefono", telefono)
        values.put("email", email)
        values.put("password", password)
        values.put("rol", rol)
        imagenUrl?.let { values.put("imagenUrl", it) } // Solo agregar si no es nulo
        return values
    }

    companion object {
        fun fromCursor(cursor: Cursor): Usuario {
            return Usuario(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                password = cursor.getString(cursor.getColumnIndexOrThrow("password")),
                rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")),
                imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl"))
            )
        }
    }
}
