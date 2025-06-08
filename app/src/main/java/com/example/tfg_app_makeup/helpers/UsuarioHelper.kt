package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.content.Intent
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.admin.MenuAdminActivity
import com.example.tfg_app_makeup.view.client.MenuClienteActivity
import java.io.File

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

    /**
     * Valida y realiza el login de un usuario, incluyendo el cifrado.
     */
    fun realizarLogin(context: Context, email: String, passwordPlano: String): Usuario? {
        return try {
            val controller = UsuarioController(context)
            val passwordCifrada = EncryptUtil.encrypt(passwordPlano)
            val usuario = controller.login(email, passwordCifrada)

            if (usuario != null) {
                Session.iniciarSesion(context, usuario)
                when (usuario.rol.uppercase()) {
                    "ADMIN" -> context.startActivity(Intent(context, MenuAdminActivity::class.java))
                    "CLIENTE" -> context.startActivity(
                        Intent(
                            context,
                            MenuClienteActivity::class.java
                        )
                    )

                    else -> Toast.makeText(context, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                }
                usuario
            } else {
                Toast.makeText(context, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                null
            }
        } catch (e: Exception) {
            Log.e("UsuarioHelper", "Error durante login: ${e.message}", e)
            Toast.makeText(context, "Error al intentar iniciar sesión", Toast.LENGTH_LONG).show()
            null
        }
    }

    /**
     * Alterna visibilidad de un campo de texto para contraseñas.
     * Retorna el nuevo estado (visible = true, oculto = false)
     */
    fun alternarVisibilidad(editText: EditText, visible: Boolean): Boolean {
        editText.inputType = if (!visible)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        else
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        editText.setSelection(editText.text.length)
        return !visible
    }

    /**
     * Valida si los correos ingresados coinciden.
     */
    fun confirmarCorreo(context: Context, correo: String, confirmacion: String): Boolean {
        return if (correo.isEmpty() || confirmacion.isEmpty()) {
            Toast.makeText(context, "Rellena ambos campos", Toast.LENGTH_SHORT).show()
            false
        } else if (correo != confirmacion) {
            Toast.makeText(context, "Los correos no coinciden", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    /**
     * Verifica si alguno de los campos de texto está vacío.
     * @param campos Lista variable de campos a validar.
     * @return true si alguno está vacío, false si todos tienen contenido.
     */
    fun hayCamposVacios(vararg campos: String): Boolean {
        return campos.any { it.isEmpty() }
    }

    /**
     * Comprueba si dos contraseñas coinciden.
     * @param clave Contraseña introducida.
     * @param confirmarClave Confirmación de la contraseña.
     * @return true si ambas coinciden, false en caso contrario.
     */
    fun contraseñasCoinciden(clave: String, confirmarClave: String): Boolean {
        return clave == confirmarClave
    }

    /**
     * Carga la imagen de perfil desde una ruta local usando Glide.
     */
    fun cargarImagenPerfil(context: Context, ruta: String?, imageView: ImageView) {
        if (!ruta.isNullOrBlank()) {
            val archivo = File(ruta)
            Glide.with(context)
                .load(archivo)
                .placeholder(R.drawable.ic_user_placeholder)
                .error(R.drawable.ic_user_placeholder)
                .skipMemoryCache(true)  // <<--- IMPORTANTE
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // <<--- IMPORTANTE
                .circleCrop()
                .into(imageView)
            Log.d("UsuarioHelper", "Imagen cargada (sin caché): ${archivo.path}")
        } else {
            imageView.setImageResource(R.drawable.ic_user_placeholder)
            Log.d("UsuarioHelper", "Ruta vacía. Placeholder usado.")
        }
    }

}