package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tfg_app_makeup.controllers.UsuarioController

/**
 * Funciones auxiliares específicas para la validación de datos del modelo Usuario.
 */
object UsuarioHelper {

    /**
     * Valida que los campos básicos de registro no estén vacíos.
     *
     * @return true si todos los campos están completos.
     */
    fun camposRegistroValidos(
        nombre: String,
        apellidos: String,
        telefono: String,
        email: String,
        password: String
    ): Boolean {
        return nombre.isNotBlank()
                && apellidos.isNotBlank()
                && telefono.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
    }

    /**
     * Verifica que el teléfono tenga exactamente 9 dígitos.
     */
    fun telefonoValido(telefono: String): Boolean {
        return telefono.matches(Regex("^\\d{9}$"))
    }

    /**
     * Verifica que el email tenga un formato básico válido.
     */
    fun emailValido(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    /**
     * Valida que los campos de login no estén vacíos.
     */
    fun camposLoginValidos(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    /**
     * Comprueba que los correos proporcionados no estén vacíos y sean iguales.
     */

    fun correosValidos(correo1: String, correo2: String): Boolean {
        return correo1.isNotBlank() && correo2.isNotBlank() && correo1 == correo2
    }

    /**
     * Comprueba si ya existe un usuario con el correo proporcionado.
     * Utiliza el controlador de usuario para buscar por correo.
     */

    fun existeUsuarioPorCorreo(correo: String, controller: UsuarioController): Boolean {
        return controller.obtenerPorCorreo(correo) != null
    }

    /**
     * Muestra un diálogo de recuperación de contraseña.
     */

    fun mostrarResultadoRecuperacion(
        contexto: Context,
        correosValidos: Boolean,
        existeUsuario: Boolean,
        dialogo: AlertDialog
    ) {
        when {
            !correosValidos -> {
                Toast.makeText(contexto, "Los correos están vacíos o no coinciden", Toast.LENGTH_SHORT).show()
            }
            !existeUsuario -> {
                Toast.makeText(contexto, "No hay ningún usuario con ese correo", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(contexto, "Se ha enviado un correo de recuperación", Toast.LENGTH_LONG).show()
                dialogo.dismiss()
            }
        }
    }
}
