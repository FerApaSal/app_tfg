package com.example.tfg_app_makeup.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.ImageHelper
import com.example.tfg_app_makeup.helpers.PermissionHelper
import com.example.tfg_app_makeup.utils.EncryptUtil
import java.io.File

/**
 * Pantalla de registro de nuevos usuarios (rol CLIENTE).
 * Permite seleccionar imagen, validar campos y guardar usuario en BBDD.
 */
class RegistroActivity : AppCompatActivity() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var btnSubirFoto: Button
    private lateinit var btnRegistrar: Button
    private lateinit var btnCancelar: Button

    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmarPassword: EditText
    private lateinit var cbAceptarCondiciones: CheckBox

    private lateinit var usuarioController: UsuarioController
    private var rutaImagenLocal: String? = null

    companion object {
        private const val REQUEST_SELECT_IMAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        try {
            usuarioController = UsuarioController(this)
            initViews()
            setupListeners()
        } catch (e: Exception) {
            Log.e("RegistroActivity", "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al iniciar la pantalla de registro", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun initViews() {
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnCancelar = findViewById(R.id.btnCancelar)

        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword)
        cbAceptarCondiciones = findViewById(R.id.cbAceptarCondiciones)
    }

    private fun setupListeners() {
        btnSubirFoto.setOnClickListener {
            if (PermissionHelper.tienePermisoGaleria(this)) {
                abrirGaleria()
            } else {
                PermissionHelper.solicitarPermisoGaleria(this)
            }
        }

        btnRegistrar.setOnClickListener {
            try {
                val nombre = etNombre.text.toString().trim()
                val apellidos = etApellidos.text.toString().trim()
                val email = etEmail.text.toString().trim()

                val confirmarPassword = etConfirmarPassword.text.toString().trim()

                val passwordPlano = etPassword.text.toString().trim()
                val passwordCifrada = EncryptUtil.encrypt(passwordPlano)


                if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() ||
                    passwordPlano.isEmpty() || confirmarPassword.isEmpty() || !cbAceptarCondiciones.isChecked
                ) {
                    Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (passwordPlano != confirmarPassword) {
                    Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!usuarioController.validarCorreo(email)) {
                    Toast.makeText(this, "Correo electrónico inválido.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (usuarioController.existeCorreo(email)) {
                    Toast.makeText(this, "El correo ya está registrado.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val usuario = usuarioController.crearUsuario(
                    nombre, apellidos, email, passwordCifrada, rutaImagenLocal
                )

                if (usuario != null && usuarioController.insertar(usuario)) {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RegistroActivity", "Error al registrar: ${e.message}", e)
                Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_LONG).show()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_SELECT_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            val uriOriginal = data.data
            if (uriOriginal != null) {
                rutaImagenLocal = ImageHelper.copiarImagenInterna(this, uriOriginal)

                if (rutaImagenLocal != null) {
                    val archivo = File(rutaImagenLocal!!)

                    Glide.with(this)
                        .load(archivo)
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .circleCrop()
                        .into(ivFotoPerfil)

                    ImageHelper.indexarImagen(this, archivo)

                    Log.d("RegistroActivity", "Imagen copiada e indexada: ${archivo.absolutePath}")
                } else {
                    Toast.makeText(this, "No se pudo acceder a la imagen", Toast.LENGTH_SHORT).show()
                    Log.e("RegistroActivity", "rutaImagenLocal es null")
                }
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionHelper.REQUEST_GALLERY_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                abrirGaleria()
            } else {
                PermissionHelper.manejarPermisoDenegado(this)
            }
        }
    }
}
