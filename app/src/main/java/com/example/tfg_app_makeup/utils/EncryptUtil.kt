package com.example.tfg_app_makeup.utils

import java.security.MessageDigest

/**
 * Utilidad para cifrar contraseñas usando SHA-256.
 */
object EncryptUtil {

    /**
     * Cifra una cadena de texto con algoritmo SHA-256.
     *
     * @param input Texto plano a cifrar.
     * @return Cadena cifrada en hexadecimal.
     */
    fun encrypt(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(input.toByteArray())
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // En caso de error, se devuelve un string vacío.
            ""
        }
    }
}
