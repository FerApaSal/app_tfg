package com.example.tfg_app_makeup.controllers

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.services.UsuarioService

class UsuarioController(context: Context) {

    private val usuarioService = UsuarioService(context)

    /**
     * Registra un nuevo usuario con los datos proporcionados.
     *
     * @param nombre Nombre del usuario.
     * @param apellidos Apellidos del usuario.
     * @param telefono Número de teléfono del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param rol Rol del usuario (por ejemplo, "cliente", "administrador").
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    fun registrarUsuario(
        nombre: String,
        apellidos: String,
        telefono: String,
        email: String,
        password: String,
        rol: String,
        imagenUrl: String?
    ): Boolean {
        return try {
            if (!UsuarioHelper.camposRegistroValidos(nombre, apellidos, telefono, email, password)) {
                Log.d("UserController", "Registro fallido: campos vacíos.")
                return false
            }

            if (!UsuarioHelper.telefonoValido(telefono) || !UsuarioHelper.emailValido(email)) {
                Log.d("UserController", "Registro fallido: formato inválido.")
                return false
            }

            usuarioService.registroUsuario(nombre, apellidos, telefono, email, password, rol, imagenUrl)
        } catch (e: Exception) {
            Log.e("UserController", "Error en registro: ${e.message}", e)
            false
        }
    }

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Usuario si el login fue exitoso, null en caso contrario.
     */

    fun login(email: String, password: String): Usuario? {
        return try {
            if (!UsuarioHelper.camposLoginValidos(email, password)) {
                Log.d("UserController", "Login fallido: campos vacíos.")
                return null
            }

            usuarioService.login(email, password)
        } catch (e: Exception) {
            Log.e("UserController", "Error en login: ${e.message}")
            null
        }
    }

    fun obtenerPorCorreo(correo: String): Usuario? {
        return usuarioService.obtenerPorCorreo(correo)
    }


    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return Lista de usuarios o una lista vacía si ocurre un error.
     */

    fun obtenerTodos(): List<Usuario> {
        return try {
            usuarioService.obtenerTodos()
        } catch (e: Exception) {
            Log.e("UserController", "Error al obtener usuarios: ${e.message}")
            emptyList()
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */

    fun eliminarPorId(id: String): Boolean {
        return try {
            usuarioService.elminarPorId(id)
        } catch (e: Exception) {
            Log.e("UserController", "Error al eliminar usuario: ${e.message}")
            false
        }
    }
}
