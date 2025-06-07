package com.example.tfg_app_makeup.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.admin.MenuAdminActivity
import com.example.tfg_app_makeup.view.client.MenuClienteActivity
import com.example.tfg_app_makeup.view.common.PoliticaPrivacidadActivity

/**
 * Pantalla de inicio de sesión.
 * Utiliza controlador de usuario para validar credenciales.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnRegistrarse: Button
    private lateinit var btnRecuperarClave: Button
    private lateinit var tvPoliticaPrivacidad: TextView

    private lateinit var usuarioController: UsuarioController
    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            usuarioController = UsuarioController(this)
            initViews()
            setupListeners()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al iniciar la aplicación", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        btnRecuperarClave = findViewById(R.id.btnRecuperarClave)
        tvPoliticaPrivacidad = findViewById(R.id.tvPoliticaPrivacidad)
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            try {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                val usuario = usuarioController.login(email, password)
                if (usuario != null) {
                    Session.usuarioActual = usuario
                    Log.d("LoginActivity", "Usuario logueado correctamente: ${usuario.email}")

                    when (usuario.rol.uppercase()) {
                        "ADMIN" -> startActivity(Intent(this, MenuAdminActivity::class.java))
                        "CLIENTE" -> startActivity(Intent(this, MenuClienteActivity::class.java))
                        else -> {
                            Log.e("LoginActivity", "Rol desconocido: ${usuario.rol}")
                            Toast.makeText(this, "Rol no reconocido", Toast.LENGTH_SHORT).show()
                        }
                    }

                    finish()
                } else {
                    Log.d("LoginActivity", "Login fallido: credenciales incorrectas")
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error durante login: ${e.message}", e)
                Toast.makeText(this, "Error al intentar iniciar sesión", Toast.LENGTH_LONG).show()
            }
        }

        btnTogglePassword.setOnClickListener {
            try {
                passwordVisible = !passwordVisible
                etPassword.inputType = if (passwordVisible)
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

                etPassword.setSelection(etPassword.text.length)
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error al cambiar visibilidad de contraseña: ${e.message}", e)
            }
        }

        btnRegistrarse.setOnClickListener {
            try {
                startActivity(Intent(this, RegistroActivity::class.java))
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error al abrir registro: ${e.message}", e)
            }
        }

        btnRecuperarClave.setOnClickListener {
            try {
                mostrarDialogoRecuperacion()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error al mostrar diálogo de recuperación: ${e.message}", e)
                Toast.makeText(this, "Error al intentar recuperar la contraseña", Toast.LENGTH_LONG)
                    .show()
            }
        }

        tvPoliticaPrivacidad.setOnClickListener {
            val intent = Intent(this, PoliticaPrivacidadActivity::class.java)
            startActivity(intent)
        }

    }

    private fun mostrarDialogoRecuperacion() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_recuperar_clave, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)

        val btnCerrar = dialogView.findViewById<ImageButton>(R.id.btnCerrarDialogo)
        val etCorreo = dialogView.findViewById<EditText>(R.id.etEmailDialogo)
        val etConfirmacion = dialogView.findViewById<EditText>(R.id.etEmailConfirmacion)
        val btnEnviar = dialogView.findViewById<Button>(R.id.btnEnviarRecuperacion)

        btnCerrar.setOnClickListener {
            alertDialog.dismiss()
        }

        btnEnviar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val confirmacion = etConfirmacion.text.toString().trim()

            val correosValidos = UsuarioHelper.correosValidos(correo, confirmacion)
            val usuarioExiste = UsuarioHelper.existeUsuarioPorCorreo(correo, usuarioController)

            UsuarioHelper.mostrarResultadoRecuperacion(
                contexto = this,
                correosValidos = correosValidos,
                existeUsuario = usuarioExiste,
                dialogo = alertDialog
            )
        }

        alertDialog.show()
    }
}
