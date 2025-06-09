package com.example.tfg_app_makeup.services

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Usuario

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios en la base de datos.
 *
 * @param context Contexto de la aplicación, necesario para inicializar la base de datos.
 */
class UsuarioService(private val context: Context) {

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id Identificador único del usuario.
     * @return Objeto de tipo Usuario si se encuentra, `null` en caso contrario.
     */
    fun obtenerPorId(id: String): Usuario? {
        val db = AppDatabase(context).readableDatabase
        var usuario: Usuario? = null
        try {
            val cursor = db.query("usuarios", null, "id = ?", arrayOf(id), null, null, null)
            if (cursor.moveToFirst()) {
                usuario = Usuario.fromCursor(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al buscar usuario por ID: ${e.message}")
        } finally {
            db.close()
        }
        return usuario
    }

    /**
     * Obtiene un usuario por su correo.
     *
     * @param correo Correo del usuario.
     * @return Objeto de tipo Usuario si se encuentra, `null` en caso contrario.
     */
    fun obtenerPorCorreo(correo: String): Usuario? {
        val db = AppDatabase(context).readableDatabase
        var usuario: Usuario? = null
        try {
            val cursor = db.query("usuarios", null, "correo = ?", arrayOf(correo), null, null, null)
            if (cursor.moveToFirst()) {
                usuario = Usuario.fromCursor(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al buscar usuario por correo: ${e.message}")
        } finally {
            db.close()
        }
        return usuario
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto de tipo Usuario que se desea insertar.
     * @return `true` si el usuario fue insertado correctamente, `false` en caso contrario.
     */
    fun insertar(usuario: Usuario): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val result = db.insert("usuarios", null, usuario.toContentValues())
            Log.d("UsuarioService", "Usuario insertado: ${usuario.id}")
            result != -1L
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al insertar usuario: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    /**
     * Actualiza un usuario existente en la base de datos.
     *
     * @param usuario Objeto de tipo Usuario que se desea actualizar.
     * @return `true` si el usuario fue actualizado correctamente, `false` en caso contrario.
     */
    fun actualizar(usuario: Usuario): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val filas = db.update("usuarios", usuario.toContentValues(), "id = ?", arrayOf(usuario.id))
            Log.d("UsuarioService", "Usuario actualizado: ${usuario.id}")
            filas > 0
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al actualizar usuario: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    /**
     * Obtiene todos los usuarios registrados en la base de datos.
     *
     * @return Lista de objetos de tipo Usuario.
     */
    fun obtenerTodos(): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        val db = AppDatabase(context).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM usuarios", null)

        if (cursor.moveToFirst()) {
            do {
                usuarios.add(Usuario.fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return usuarios
    }
}