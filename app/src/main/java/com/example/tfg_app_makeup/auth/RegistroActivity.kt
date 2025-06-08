package com.example.tfg_app_makeup.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import java.util.*

class RegistroActivity : AppCompatActivity() {

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

    private var passwordVisible = false
    private var confirmPasswordVisible = false

    private lateinit var usuarioController: UsuarioController
    private var rutaImagenSeleccionada: String? = null // Opcional para imagen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        usuarioController = UsuarioController(this)
        initViews()
        setupListeners()
    }

    private fun initViews() {
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

    private fun setupListeners() {
        btnTogglePassword.setOnClickListener {
            try {
                passwordVisible = !passwordVisible
                etPassword.inputType = if (passwordVisible)
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

                etPassword.setSelection(etPassword.text.length)
                btnTogglePassword.setImageResource(
                    if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility
                )
            } catch (e: Exception) {
                Log.e("RegistroActivity", "Error toggle clave: ${e.message}", e)
            }
        }

        btnToggleConfirmPassword.setOnClickListener {
            try {
                confirmPasswordVisible = !confirmPasswordVisible
                etConfirmarPassword.inputType = if (confirmPasswordVisible)
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

                etConfirmarPassword.setSelection(etConfirmarPassword.text.length)
                btnToggleConfirmPassword.setImageResource(
                    if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility
                )
            } catch (e: Exception) {
                Log.e("RegistroActivity", "Error toggle confirmación: ${e.message}", e)
            }
        }

        btnSubirFoto.setOnClickListener {
            Toast.makeText(this, "Función pendiente: subir foto", Toast.LENGTH_SHORT).show()
            // Aquí podrías abrir un intent para seleccionar imagen
        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etEmail.text.toString().trim()
            val clave = etPassword.text.toString().trim()
            val confirmarClave = etConfirmarPassword.text.toString().trim()

            if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || clave.isEmpty() || confirmarClave.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (clave != confirmarClave) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbAceptarCondiciones.isChecked) {
                Toast.makeText(this, "Debes aceptar los términos de privacidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
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
                finish() // Cierra y vuelve al login
            } else {
                Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }
}
