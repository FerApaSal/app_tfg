package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.model.Usuario

object UsuarioHelper {

    fun validar(usuario: Usuario): Boolean {
        if (usuario.nombre.isBlank() || usuario.apellido.isBlank()) {

            Log.w("UsuarioHelper", "Nombre o apellido vacío.")
            return false
        }
        if (!usuario.correo.contains("@")) {
            Log.w("UsuarioHelper", "Correo inválido: ${usuario.correo}")
            return false
        }
        if (usuario.clave.length < 6) {
            Log.w("UsuarioHelper", "Contraseña inválida: debe tener al menos 6 caracteres.")
            return false
        }
        if (usuario.rol.isBlank()) {
            Log.w("UsuarioHelper", "Rol no especificado.")
            return false
        }
        return true
    }

    fun confirmarCorreo(
        context: Context,
        correo1: String,
        correo2: String
    ): Boolean {
        if (correo1.isBlank() || correo2.isBlank()) {
            Log.w("UsuarioHelper", "Correos vacíos.")
            return false
        }
        if (correo1 != correo2) {
            Log.w("UsuarioHelper", "Correos no coinciden: $correo1 != $correo2")
            return false
        }
        return true
    }

}