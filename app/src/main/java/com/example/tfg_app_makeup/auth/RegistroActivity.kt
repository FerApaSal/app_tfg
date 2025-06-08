package com.example.tfg_app_makeup.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import java.io.File
import java.util.*

/**
 * Pantalla de registro para nuevos usuarios con rol CLIENTE.
 * Permite completar los datos personales y crear una cuenta local simulada.
 */
class RegistroActivity : AppCompatActivity() {

    // Componentes visuales
    private lateinit var ivFotoPerfil: ImageView
    private lateinit var btnSubirFoto: Button
    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmarPassword: EditText
    private lateinit var cbAceptarCondiciones: CheckBox
    private lateinit var btnRegistrar: Button
    private lateinit var btnCancelar: Button
    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnToggleConfirmPassword: ImageButton

    // Estados para mostrar u ocultar contraseña
    private var passwordVisible = false
    private var confirmPasswordVisible = false

    private lateinit var usuarioController: UsuarioController
    private var rutaImagenSeleccionada: String? = null

    private val REQUEST_CODE_PICK_IMAGE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        usuarioController = UsuarioController(this)
        inicializarComponentes()
        configurarListeners()
    }

    /**
     * Enlaza los elementos visuales del layout con las variables de clase.
     */
    private fun inicializarComponentes() {
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword)
        cbAceptarCondiciones = findViewById(R.id.cbAceptarCondiciones)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnCancelar)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword)
    }

    /**
     * Configura los eventos para botones y campos interactivos.
     */
    private fun configurarListeners() {
        btnTogglePassword.setOnClickListener {
            passwordVisible = UsuarioHelper.alternarVisibilidad(etPassword, passwordVisible)
        }

        btnToggleConfirmPassword.setOnClickListener {
            confirmPasswordVisible = UsuarioHelper.alternarVisibilidad(etConfirmarPassword, confirmPasswordVisible)
        }

        btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnRegistrar.setOnClickListener { procesarRegistro() }

        btnCancelar.setOnClickListener { finish() }
    }

    /**
     * Valida los datos del formulario y registra un nuevo usuario si todo es correcto.
     */
    private fun procesarRegistro() {
        val nombre = etNombre.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val correo = etEmail.text.toString().trim()
        val clave = etPassword.text.toString().trim()
        val confirmarClave = etConfirmarPassword.text.toString().trim()

        if (UsuarioHelper.hayCamposVacios(nombre, apellidos, correo, clave, confirmarClave)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!UsuarioHelper.contraseñasCoinciden(clave, confirmarClave)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (!cbAceptarCondiciones.isChecked) {
            Toast.makeText(this, "Debes aceptar los términos de privacidad", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = Usuario(
            id = UUID.randomUUID().toString(),
            nombre = nombre,
            apellido = apellidos,
            correo = correo,
            clave = EncryptUtil.encrypt(clave),
            imagenUrl = rutaImagenSeleccionada,
            rol = "CLIENTE"
        )

        if (usuarioController.insertar(usuario)) {
            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Maneja el resultado de la selección de imagen desde la galería.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val uri = data.data
            try {
                // Eliminar imágenes antiguas del registro
                filesDir.listFiles()?.forEach { file ->
                    if (file.name.startsWith("registro_")) file.delete()
                }

                val extension = contentResolver.getType(uri!!)?.let {
                    when {
                        it.contains("png") -> ".png"
                        it.contains("jpeg") || it.contains("jpg") -> ".jpg"
                        else -> ".img"
                    }
                } ?: ".img"

                val timestamp = System.currentTimeMillis()
                val file = File(filesDir, "registro_$timestamp$extension")

                contentResolver.openInputStream(uri).use { input ->
                    file.outputStream().use { output ->
                        input?.copyTo(output)
                    }
                }

                rutaImagenSeleccionada = file.absolutePath

                Glide.with(this)
                    .load(file)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .into(ivFotoPerfil)

                Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("RegistroActivity", "Error al guardar imagen", e)
                Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
