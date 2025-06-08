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
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import com.example.tfg_app_makeup.utils.Session
import java.io.File

class PerfilActivity : AppCompatActivity() {

    private lateinit var ivFotoPerfil: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
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

        initViews()
        cargarDatosUsuario()
        setupListeners()
    }

    private fun initViews() {
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil)
        etNombre = findViewById(R.id.etNombrePerfil)
        etApellido = findViewById(R.id.etApellidosPerfil)
        etClavePerfil = findViewById(R.id.etPasswordPerfil)
        etConfirmarClavePerfil = findViewById(R.id.etConfirmarPasswordPerfil)
        btnTogglePassword = findViewById(R.id.btnTogglePasswordPerfil)
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmarPasswordPerfil)
        btnGuardar = findViewById(R.id.btnGuardarPerfil)
        btnAtras = findViewById(R.id.btnVolverPerfil)
        btnSubirFoto = findViewById(R.id.btnSubirFoto)
    }

    private fun cargarDatosUsuario() {
        usuario?.let {
            etNombre.setText(it.nombre)
            etApellido.setText(it.apellido)

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

    private fun setupListeners() {
        btnSubirFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnGuardar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString().trim()
            val nuevoApellido = etApellido.text.toString().trim()
            val nuevaClavePlano = etClavePerfil.text.toString().trim()
            val confirmarClavePlano = etConfirmarClavePerfil.text.toString().trim()

            if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty()) {
                Toast.makeText(this, "Los campos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            usuario?.let { u ->
                u.nombre = nuevoNombre
                u.apellido = nuevoApellido

                if (!rutaImagenSeleccionada.isNullOrBlank()) {
                    u.imagenUrl = rutaImagenSeleccionada
                }

                if (nuevaClavePlano.isNotEmpty() || confirmarClavePlano.isNotEmpty()) {
                    if (nuevaClavePlano != confirmarClavePlano) {
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    if (nuevaClavePlano.length < 6) {
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
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

        var passwordVisible = false
        var confirmPasswordVisible = false

        btnTogglePassword.setOnClickListener {
            try {
                passwordVisible = !passwordVisible
                etClavePerfil.inputType = if (passwordVisible)
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

                etClavePerfil.setSelection(etClavePerfil.text.length)
            } catch (e: Exception) {
                Log.e("PerfilActivity", "Error al cambiar visibilidad de contraseña", e)
            }
        }

        btnToggleConfirmPassword.setOnClickListener {
            try {
                confirmPasswordVisible = !confirmPasswordVisible
                etConfirmarClavePerfil.inputType = if (confirmPasswordVisible)
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

                etConfirmarClavePerfil.setSelection(etConfirmarClavePerfil.text.length)
            } catch (e: Exception) {
                Log.e("PerfilActivity", "Error al cambiar visibilidad de confirmación", e)
            }
        }

        btnAtras.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val file = File(filesDir, "perfil.jpg")
                file.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }
                rutaImagenSeleccionada = file.absolutePath
                Glide.with(this)
                    .load(file)
                    .placeholder(R.drawable.ic_user_placeholder)
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
