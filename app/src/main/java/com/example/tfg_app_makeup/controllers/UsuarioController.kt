package com.example.tfg_app_makeup.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.services.UsuarioService

class UsuarioController(private val context: Context) {

    private val service = UsuarioService(context)
    private val dbHelper = AppDatabase(context)

    fun insertar(usuario: Usuario): Boolean {
        return if (UsuarioHelper.validar(usuario)) {
            val result = service.insertar(usuario)
            Log.d("UsuarioController", "Insert resultado: $result")
            result
        } else {
            Log.w("UsuarioController", "Usuario no válido, inserción cancelada.")
            false
        }
    }


    fun actualizar(usuario: Usuario): Boolean {
        return if (UsuarioHelper.validar(usuario)) {
            val result = service.actualizar(usuario)
            Log.d("UsuarioController", "Update resultado: $result")
            result
        } else {
            Log.w("UsuarioController", "Usuario no válido, actualización cancelada.")
            false
        }
    }


    fun eliminarPorId(id: String): Boolean {
        val result = service.eliminarPorId(id)
        Log.d("UsuarioController", "Delete resultado: $result")
        return result
    }

    fun login(correo: String, password: String): Usuario? {
        val usuario = service.obtenerPorCorreo(correo)

        return if (correo.isBlank() || password.isBlank()) {
            Log.w("UsuarioController", "Correo o contraseña vacíos.")
            Toast.makeText(context, "Correo y contraseña no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
            null
        } else if (usuario != null && usuario.clave == password) {
            Log.d("UsuarioController", "Login exitoso para: $correo")
            usuario
        } else {
            Log.w("UsuarioController", "Login fallido para: $correo")
            Toast.makeText(context, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun existeCorreo(correo: String): Boolean {
        return service.obtenerPorCorreo(correo) != null
    }

    fun obtenerUsuariosPorRol(rolBuscado: String): List<Usuario> {
        val lista = mutableListOf<Usuario>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "usuarios", null,
            "rol = ?", arrayOf(rolBuscado),
            null, null, "nombre ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                lista.add(Usuario.fromCursor(it))
            }
        }

        return lista
    }

}
