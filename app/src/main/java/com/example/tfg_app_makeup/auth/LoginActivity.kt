package com.example.tfg_app_makeup.auth

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.view.common.PoliticaPrivacidadActivity
import java.io.IOException

/**
 * Pantalla de inicio de sesión de la aplicación.
 * Permite ingresar las credenciales para iniciar sesión y navegar
 * según el rol del usuario.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnRegistrarse: Button
    private lateinit var btnRecuperarClave: Button
    private lateinit var tvPoliticaPrivacidad: TextView

    private var passwordVisible = false

    /**
     * Método principal del ciclo de vida.
     * Inicializa vistas, listeners y carga imagen del logo desde assets.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        try {
            initViews()
            setupListeners()
            cargarLogoDesdeAssets()
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error en onCreate: ${e.message}", e)
            Toast.makeText(this, "Error al iniciar la aplicación", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Inicializa las vistas de la interfaz.
     */
    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnTogglePassword = findViewById(R.id.btnTogglePassword)
        btnRegistrarse = findViewById(R.id.btnRegistrarse)
        btnRecuperarClave = findViewById(R.id.btnRecuperarClave)
        tvPoliticaPrivacidad = findViewById(R.id.tvPoliticaPrivacidad)
    }

    /**
     * Configura los listeners para cada botón de la pantalla.
     */
    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val passwordPlano = etPassword.text.toString().trim()
            UsuarioHelper.realizarLogin(this, email, passwordPlano)?.let {
                finish()
            }
        }

        btnTogglePassword.setOnClickListener {
            passwordVisible = UsuarioHelper.alternarVisibilidad(etPassword, passwordVisible)
        }

        btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        btnRecuperarClave.setOnClickListener {
            mostrarDialogoRecuperacion()
        }

        tvPoliticaPrivacidad.setOnClickListener {
            startActivity(Intent(this, PoliticaPrivacidadActivity::class.java))
        }
    }

    /**
     * Carga la imagen del logo desde la carpeta `assets`.
     */
    private fun cargarLogoDesdeAssets() {
        try {
            val inputStream = assets.open("logo.png")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            findViewById<ImageView>(R.id.ivLogo).setImageBitmap(bitmap)
        } catch (e: IOException) {
            Log.e("LoginActivity", "Error al cargar imagen desde assets: ${e.message}", e)
        }
    }

    /**
     * Muestra un diálogo personalizado para recuperación de clave.
     */
    private fun mostrarDialogoRecuperacion() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_recuperar_clave, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)

        val btnCerrar = dialogView.findViewById<ImageButton>(R.id.btnCerrarDialogo)
        val etCorreo = dialogView.findViewById<EditText>(R.id.etEmailDialogo)
        val etConfirmacion = dialogView.findViewById<EditText>(R.id.etEmailConfirmacion)
        val btnEnviar = dialogView.findViewById<Button>(R.id.btnEnviarRecuperacion)

        btnCerrar.setOnClickListener { alertDialog.dismiss() }

        btnEnviar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val confirmacion = etConfirmacion.text.toString().trim()

            if (UsuarioHelper.confirmarCorreo(this, correo, confirmacion)) {
                Toast.makeText(this, "Solicitud de recuperación enviada a $correo", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
}