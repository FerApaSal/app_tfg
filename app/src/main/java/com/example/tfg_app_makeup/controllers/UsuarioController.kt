package com.example.tfg_app_makeup.controllers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.services.UsuarioService

class UsuarioController(private val context: Context) {

    private val service = UsuarioService(context)

    fun obtenerTodos(): List<Usuario> {
        return service.obtenerTodos()
    }

    fun obtenerPorId(id: String): Usuario? {
        return service.obtenerPorId(id)
    }

    fun obtenerPorCorreo(correo: String): Usuario? {
        return service.obtenerPorCorreo(correo)
    }

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

    fun crearUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        clave: String,
        rutaImagenLocal: String? = null
    ): Usuario? {
        val usuario = Usuario(
            id = java.util.UUID.randomUUID().toString(),
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            clave = clave,
            rol = "CLIENTE",
            imagenUrl = rutaImagenLocal
        )

        return if (UsuarioHelper.validar(usuario)) {
            Log.d("UsuarioController", "Usuario creado: $correo")
            usuario
        } else {
            Log.w("UsuarioController", "Usuario no válido, creación cancelada.")
            null
        }
    }

    fun existeCorreo(correo: String): Boolean {
        return service.obtenerPorCorreo(correo) != null
    }

    fun validarCorreo(correo: String): Boolean {
        return Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$").matches(correo)
    }
}
