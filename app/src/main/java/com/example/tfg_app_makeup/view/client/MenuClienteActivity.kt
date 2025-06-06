package com.example.tfg_app_makeup.view.client

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.utils.Session
import java.io.File

/**
 * Menú principal del cliente final.
 * Muestra el nombre y la imagen de perfil del usuario logueado.
 */
class MenuClienteActivity : AppCompatActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var ivPerfilUsuario: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_cliente)

        try {
            tvBienvenida = findViewById(R.id.tvBienvenida)
            ivPerfilUsuario = findViewById(R.id.ivPerfilUsuario)

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
    }
}
