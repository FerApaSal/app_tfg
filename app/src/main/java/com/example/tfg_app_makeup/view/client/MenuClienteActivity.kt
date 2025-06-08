package com.example.tfg_app_makeup.view.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.services.UsuarioService
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.common.PerfilActivity
import java.io.File

/**
 * Menú principal del perfil cliente.
 * Muestra los accesos a funcionalidades principales, imagen de perfil y nombre del usuario logueado.
 */
class MenuClienteActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var ivPerfilUsuario: ImageView
    private lateinit var btnMisCitas: Button
    private lateinit var btnSolicitarCita: Button
    private lateinit var btnContacto: Button
    private lateinit var btnServiciosNovia: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        inicializarComponentes()
        configurarListeners()

        // Restaurar sesión actual
        Session.restaurarSesion(this, UsuarioService(this))

        // Cargar datos del usuario en pantalla
        cargarDatosUsuario()
    }

    override fun onResume() {
        super.onResume()
        cargarDatosUsuario()
    }

    /**
     * Enlaza variables con los elementos del layout.
     */
    private fun inicializarComponentes() {
        tvBienvenida = findViewById(R.id.tvBienvenida)
        ivPerfilUsuario = findViewById(R.id.ivPerfilUsuario)
        btnMisCitas = findViewById(R.id.btnMisCitas)
        btnSolicitarCita = findViewById(R.id.btnSolicitarCita)
        btnContacto = findViewById(R.id.btnContacto)
        btnServiciosNovia = findViewById(R.id.btnServicioNovias)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
    }

    /**
     * Asigna eventos a cada botón de la interfaz.
     */
    private fun configurarListeners() {

        btnMisCitas.setOnClickListener {
            startActivity(Intent(this, MisCitasActivity::class.java))
        }

        btnSolicitarCita.setOnClickListener {
            startActivity(Intent(this, SolicitarCitaActivity::class.java))
        }

        btnContacto.setOnClickListener {
            mostrarDialogoContacto()
        }

        btnServiciosNovia.setOnClickListener {
            startActivity(Intent(this, ServiciosNoviaActivity::class.java))
        }

        ivPerfilUsuario.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    /**
     * Muestra el nombre del usuario y su imagen de perfil (si está disponible).
     */
    private fun cargarDatosUsuario() {
        val usuario = Session.usuarioActual

        tvBienvenida.text = if (usuario != null) {
            "¡Hola, ${usuario.nombre}!"
        } else {
            "¡Hola!"
        }

        UsuarioHelper.cargarImagenPerfil(this, usuario?.imagenUrl, ivPerfilUsuario)
    }

    /**
     * Muestra el diálogo de contacto con enlaces a WhatsApp e Instagram.
     */
    private fun mostrarDialogoContacto() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_contacto, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)

        dialogView.findViewById<ImageButton>(R.id.btnCerrarContacto).setOnClickListener {
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnWhatsapp).setOnClickListener {
            abrirEnlace("https://wa.me/34608150648")
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.btnInstagram).setOnClickListener {
            abrirEnlace("https://instagram.com/merche_makeup")
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    /**
     * Abre un enlace externo en el navegador del sistema.
     */
    private fun abrirEnlace(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    /**
     * Cierra la sesión del usuario y vuelve al login.
     */
    private fun cerrarSesion() {
        Session.cerrarSesion(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }
}
