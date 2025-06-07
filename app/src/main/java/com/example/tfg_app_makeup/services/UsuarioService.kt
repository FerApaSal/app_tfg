package com.example.tfg_app_makeup.services

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Usuario

class UsuarioService(private val context: Context) {

    fun obtenerTodos(): List<Usuario> {
        val db = AppDatabase(context).readableDatabase
        val lista = mutableListOf<Usuario>()
        try {
            val cursor = db.query("usuarios", null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                lista.add(Usuario.fromCursor(cursor))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al obtener usuarios: ${e.message}")
        } finally {
            db.close()
        }
        return lista
    }

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

    fun eliminarPorId(id: String): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val filas = db.delete("usuarios", "id = ?", arrayOf(id))
            Log.d("UsuarioService", "Usuario eliminado: $id")
            filas > 0
        } catch (e: Exception) {
            Log.e("UsuarioService", "Error al eliminar usuario: ${e.message}")
            false
        } finally {
            db.close()
        }
    }
}
