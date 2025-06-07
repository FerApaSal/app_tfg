package com.example.tfg_app_makeup.services

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.helpers.UserDatabaseHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import java.util.*

/**
 * Servicio para gestionar operaciones relacionadas con usuarios.
 *
 * @param context Contexto de la aplicación.
 */
class UsuarioService(context: Context) {

    private val dbHelper = UserDatabaseHelper(context)

    /**
     * Registra un nuevo usuario tras comprobar que no exista.
     *
     * @param nombre Nombre.
     * @param apellidos Apellidos.
     * @param telefono Teléfono.
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     * @param rol Rol del usuario ("CLIENTE" o "ADMIN").
     * @return true si se registró correctamente, false si ya existe o hay error.
     */
    fun registroUsuario(
        nombre: String,
        apellidos: String,
        telefono: String,
        email: String,
        password: String,
        rol: String,
        imagenUrl: String?
    ): Boolean {
        try {
            if (dbHelper.getByEmail(email) != null) {
                Log.d("UserService", "El usuario con email $email ya existe.")
                return false
            }

            val newUser = Usuario(
                id = UUID.randomUUID().toString(),
                nombre = nombre,
                apellidos = apellidos,
                telefono = telefono,
                email = email,
                password = EncryptUtil.encrypt(password),
                rol = rol,
                imagenUrl = imagenUrl
            )

            return dbHelper.insert(newUser)
        } catch (e: Exception) {
            Log.e("UserService", "Error al registrar usuario: ${e.message}", e)
            return false
        }
    }

    /**
     * Intenta iniciar sesión con las credenciales dadas.
     *
     * @param email Correo electrónico.
     * @param password Contraseña en texto plano.
     * @return Usuario si las credenciales son válidas, null si fallan.
     */
    fun login(email: String, password: String): Usuario? {
        return try {
            val user = dbHelper.getByEmail(email)
            val encryptedInput = EncryptUtil.encrypt(password)

            if (user != null && user.password == encryptedInput) {
                Log.d("UserService", "Inicio de sesión correcto para $email.")
                user
            } else {
                Log.d("UserService", "Credenciales incorrectas para $email.")
                null
            }
        } catch (e: Exception) {
            Log.e("UserService", "Error durante login: ${e.message}")
            null
        }
    }

    /**
     * Obtiene un usuario por su correo electrónico.
     *
     * @param email Correo electrónico del usuario.
     * @return Usuario si existe, null si no.
     */

    fun obtenerPorCorreo(correo: String): Usuario? {
        val usuarios = obtenerTodos()
        return usuarios.find { it.email.equals(correo, ignoreCase = true) }
    }


    /**
     * Elimina un usuario por ID.
     */
    fun elminarPorId(id: String): Boolean {
        return dbHelper.deleteById(id)
    }

    /**
     * Obtiene todos los usuarios registrados.
     */
    fun obtenerTodos(): List<Usuario> {
        return dbHelper.getAll()
    }
}
