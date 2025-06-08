package com.example.tfg_app_makeup.view.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import com.example.tfg_app_makeup.utils.Session
import java.io.File

/**
 * Pantalla de edición de perfil, disponible tanto para clientes como para la administradora.
 * Permite modificar los datos personales esenciales: nombre, apellidos, teléfono, contraseña e imagen de perfil.
 */
class PerfilActivity : AppCompatActivity() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etClavePerfil: EditText
    private lateinit var etConfirmarClavePerfil: EditText
    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnToggleConfirmPassword: ImageButton
    private lateinit var btnGuardar: Button
    private lateinit var btnAtras: ImageButton
    private lateinit var btnSubirFoto: Button

    private lateinit var usuarioController: UsuarioController
    private var usuario: Usuario? = null
    private var rutaImagenSeleccionada: String? = null

    private var passwordVisible = false
    private var confirmPasswordVisible = false

    private val REQUEST_CODE_PICK_IMAGE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        usuarioController = UsuarioController(this)
        usuario = Session.usuarioActual

        if (usuario == null) {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        inicializarComponentes()
        cargarDatosUsuario()
        configurarListeners()
    }

    /**
     * Enlaza los elementos visuales del layout con las variables de clase.
     */
    private fun inicializarComponentes() {
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil)
        etNombre = findViewById(R.id.etNombrePerfil)
        etApellido = findViewById(R.id.etApellidosPerfil)
        etTelefono = findViewById(R.id.etTelefonoPerfil)
        etClavePerfil = findViewById(R.id.etPasswordPerfil)
        etConfirmarClavePerfil = findViewById(R.id.etConfirmarPasswordPerfil)
        btnTogglePassword = findViewById(R.id.btnTogglePasswordPerfil)
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmarPasswordPerfil)
        btnGuardar = findViewById(R.id.btnGuardarPerfil)
        btnAtras = findViewById(R.id.btnVolverPerfil)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
    }

    /**
     * Carga los datos actuales del usuario en los campos de edición.
     */
    private fun cargarDatosUsuario() {
        usuario?.let {
            etNombre.setText(it.nombre)
            etApellido.setText(it.apellido)
            etTelefono.setText(it.telefono)

            if (!it.imagenUrl.isNullOrBlank()) {
                val archivo = File(it.imagenUrl!!)
                Glide.with(this)
                    .load(archivo)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .circleCrop()
                    .into(ivFotoPerfil)
            } else {
                ivFotoPerfil.setImageResource(R.drawable.ic_user_placeholder)
            }
        }
    }

    /**
     * Configura los eventos de botones para subir imagen, mostrar contraseñas y guardar cambios.
     */
    private fun configurarListeners() {
        btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnGuardar.setOnClickListener {
            guardarCambiosPerfil()
        }

        btnTogglePassword.setOnClickListener {
            passwordVisible = UsuarioHelper.alternarVisibilidad(etClavePerfil, passwordVisible)
        }

        btnToggleConfirmPassword.setOnClickListener {
            confirmPasswordVisible =
                UsuarioHelper.alternarVisibilidad(etConfirmarClavePerfil, confirmPasswordVisible)
        }

        btnAtras.setOnClickListener {
            finish()
        }
    }

    /**
     * Valida y guarda los cambios realizados en el perfil del usuario, incluyendo imagen y contraseña.
     */
    private fun guardarCambiosPerfil() {
        val nuevoNombre = etNombre.text.toString().trim()
        val nuevoApellido = etApellido.text.toString().trim()
        val nuevoTelefono = etTelefono.text.toString().trim()
        val nuevaClavePlano = etClavePerfil.text.toString().trim()
        val confirmarClavePlano = etConfirmarClavePerfil.text.toString().trim()

        if (UsuarioHelper.hayCamposVacios(nuevoNombre, nuevoApellido)) {
            Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        usuario?.let { u ->
            u.nombre = nuevoNombre
            u.apellido = nuevoApellido
            u.telefono = nuevoTelefono

            if (!rutaImagenSeleccionada.isNullOrBlank()) {
                u.imagenUrl = rutaImagenSeleccionada
            }

            if (nuevaClavePlano.isNotEmpty() || confirmarClavePlano.isNotEmpty()) {
                if (nuevaClavePlano != confirmarClavePlano) {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    return
                }

                if (nuevaClavePlano.length < 6) {
                    Toast.makeText(
                        this,
                        "La contraseña debe tener al menos 6 caracteres",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                u.clave = EncryptUtil.encrypt(nuevaClavePlano)
            }

            if (usuarioController.actualizar(u)) {
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                Session.usuarioActual = u
            } else {
                Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Maneja el resultado de la selección de imagen desde la galería y guarda localmente la nueva foto.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val uri = data.data
            try {
                // Eliminar imágenes antiguas
                filesDir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("perfil_")) file.delete()
                }

                val inputStream = contentResolver.openInputStream(uri!!)
                val extension = contentResolver.getType(uri)?.let {
                    when {
                        it.contains("png") -> ".png"
                        it.contains("jpeg") || it.contains("jpg") -> ".jpg"
                        else -> ".img"
                    }
                } ?: ".img"

                val timestamp = System.currentTimeMillis()
                val file = File(filesDir, "perfil_$timestamp$extension")

                file.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }

                rutaImagenSeleccionada = file.absolutePath
                Session.usuarioActual?.imagenUrl = rutaImagenSeleccionada

                Glide.with(this)
                    .load(file)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
                    .circleCrop()
                    .into(ivFotoPerfil)

                Toast.makeText(this, "Imagen actualizada", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("PerfilActivity", "Error al guardar imagen", e)
                Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
