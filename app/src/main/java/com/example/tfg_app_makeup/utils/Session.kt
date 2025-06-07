package com.example.tfg_app_makeup.utils

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.services.UsuarioService

/**
 * Sesión actual del usuario en la aplicación.
 */
object Session {

    var usuarioActual: Usuario? = null

    fun iniciarSesion(context: Context, usuario: Usuario) {
        usuarioActual = usuario
        val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("id", usuario.id).apply()
        Log.d("Session", "Sesión iniciada para: ${usuario.correo}")
    }


    fun cerrarSesion(context: Context) {
        usuarioActual = null
        val prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        prefs.edit().remove("usuario_id").apply()
        Log.d("Session", "Sesión cerrada")
    }

    fun restaurarSesion(context: Context, service: UsuarioService) {
        val sharedPreferences = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)

        Log.d("Session", "ID recuperado en restaurarSesion: $id")

        if (id != null) {
            usuarioActual = service.obtenerPorId(id)
            if (usuarioActual != null) {
                Log.d("Session", "Sesión restaurada: ${usuarioActual!!.correo}")
            } else {
                Log.w("Session", "No se pudo restaurar la sesión con ID: $id")
            }
        } else {
            Log.w("Session", "No hay ID guardado en preferencias")
        }
    }

}