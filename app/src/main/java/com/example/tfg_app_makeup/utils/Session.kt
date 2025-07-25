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

    /**
     * Inicia sesión guardando el usuario actual y almacenando su ID en las preferencias compartidas.
     * @param context Contexto de la aplicación.
     * @param usuario Usuario que inicia sesión.
     */
    fun iniciarSesion(context: Context, usuario: Usuario) {
        usuarioActual = usuario
        val prefs = context.getSharedPreferences("usuario_prefs", Context.MODE_PRIVATE)
        prefs.edit().putString("id", usuario.id).apply()
        Log.d("Session", "Sesión iniciada para: ${usuario.correo}")
    }

    /**
     * Cierra la sesión eliminando el usuario actual y su ID de las preferencias compartidas.
     * @param context Contexto de la aplicación.
     */
    fun cerrarSesion(context: Context) {
        usuarioActual = null
        val prefs = context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
        prefs.edit().remove("usuario_id").apply()
        Log.d("Session", "Sesión cerrada")
    }

    /**
     * Restaura la sesión del usuario si existe un ID almacenado en las preferencias compartidas.
     * @param context Contexto de la aplicación.
     * @param service Servicio para obtener el usuario por su ID.
     */
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