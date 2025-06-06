package com.example.tfg_app_makeup.utils

import com.example.tfg_app_makeup.model.Usuario

/**
 * Sesión actual del usuario en la aplicación.
 * Almacena temporalmente el token (si se usa) y el usuario logueado.
 */
object Session {

    /**
     * Token de autenticación (usado si se conecta con una API externa).
     */
    var token: String? = null

    /**
     * Usuario actualmente logueado.
     */
    var usuarioActual: Usuario? = null

    /**
     * Limpia la sesión completamente.
     */
    fun cerrarSesion() {
        token = null
        usuarioActual = null
    }
}