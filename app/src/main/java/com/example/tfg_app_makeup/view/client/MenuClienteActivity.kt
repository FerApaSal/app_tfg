package com.example.tfg_app_makeup.view.client

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.services.UsuarioService
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.common.PerfilActivity
import java.io.File

/**
 * Menú principal del cliente final.
 * Muestra el nombre y la imagen de perfil del usuario logueado.
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

        Session.restaurarSesion(this, UsuarioService(this))

        try {
            tvBienvenida = findViewById(R.id.tvBienvenida)
            ivPerfilUsuario = findViewById(R.id.ivPerfilUsuario)

            btnMisCitas = findViewById(R.id.btnMisCitas)
            btnSolicitarCita = findViewById(R.id.btnSolicitarCita)
            btnContacto = findViewById(R.id.btnContacto)
            btnServiciosNovia = findViewById(R.id.btnServicioNovias)

            btnCerrarSesion = findViewById(R.id.btnCerrarSesion)


            val usuario = Session.usuarioActual
            if (usuario != null) {
                tvBienvenida.text = "¡Hola, ${usuario.nombre}!"

                if (!usuario.imagenUrl.isNullOrBlank()) {
                    val archivo = File(usuario.imagenUrl!!)
                    Glide.with(this)
                        .load(archivo)
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .circleCrop()
                        .into(ivPerfilUsuario)

                    Log.d("MenuClienteActivity", "Imagen de perfil cargada: ${archivo.path}")
                } else {
                    ivPerfilUsuario.setImageResource(R.drawable.ic_user_placeholder)
                    Log.d("MenuClienteActivity", "Sin imagen de perfil. Usando placeholder.")
                }

            } else {
                Log.e("MenuClienteActivity", "No se encontró el usuario en sesión.")
                tvBienvenida.text = "¡Hola!"
            }

        } catch (e: Exception) {
            Log.e("MenuClienteActivity", "Error al mostrar el menú del cliente: ${e.message}", e)
        }

        // Menú de navegación
        btnMisCitas.setOnClickListener {
            try {
                val intent = Intent(this, MisCitasActivity::class.java)
                startActivity(intent)
                Log.d("MenuClienteActivity", "Botón 'Mis Citas' pulsado")
            } catch (e: Exception) {
                Log.e("MenuClienteActivity", "Error al pulsar el botón 'Mis Citas': ${e.message}", e)
            }
        }

        btnSolicitarCita.setOnClickListener {
            try {
                val intent = Intent(this, SolicitarCitaActivity::class.java)
                startActivity(intent)
                Log.d("MenuClienteActivity", "Botón 'Solicitar Cita' pulsado")
            } catch (e: Exception) {
                Log.e("MenuClienteActivity", "Error al pulsar el botón 'Solicitar Cita': ${e.message}", e)
            }
        }

        btnContacto.setOnClickListener {
            try {
                mostrarDialogoContacto()
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error al mostrar diálogo de contacto: ${e.message}", e)
                Toast.makeText(this, "Error al mostrar diálogo de contacto", Toast.LENGTH_LONG)
                    .show()
            }
        }

        btnServiciosNovia.setOnClickListener {
            try {
                val intent = Intent(this, ServiciosNoviaActivity::class.java)
                startActivity(intent)
                Log.d("MenuClienteActivity", "Botón 'Servicios novia' pulsado")
            } catch (e: Exception) {
                Log.e("MenuClienteActivity", "Error al pulsar el botón 'Servicios novia': ${e.message}", e)
            }
        }

        ivPerfilUsuario.setOnClickListener {
            try {
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                Log.d("MenuClienteActivity", "Acceso al perfil desde imagen de usuario")
            } catch (e: Exception) {
                Log.e("MenuClienteActivity", "Error al abrir el perfil: ${e.message}", e)
                Toast.makeText(this, "No se pudo abrir el perfil", Toast.LENGTH_SHORT).show()
            }
        }

        btnCerrarSesion.setOnClickListener {
            try {
                // Limpiar sesión
                Session.cerrarSesion(this)

                // Volver al login
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

                Log.d("MenuClienteActivity", "Sesión cerrada correctamente")
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("MenuClienteActivity", "Error al cerrar sesión: ${e.message}", e)
                Toast.makeText(this, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun mostrarDialogoContacto() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_contacto, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)

        val btnCerrar = dialogView.findViewById<ImageButton>(R.id.btnCerrarContacto)
        val btnWhatsapp = dialogView.findViewById<Button>(R.id.btnWhatsapp)
        val btnInstagram = dialogView.findViewById<Button>(R.id.btnInstagram)

        btnCerrar.setOnClickListener {
            alertDialog.dismiss()
        }

        btnWhatsapp.setOnClickListener {
            val url = "https://wa.me/34608150648"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            alertDialog.dismiss()
        }

        btnInstagram.setOnClickListener {
            val url = "https://instagram.com/merche_makeup"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
