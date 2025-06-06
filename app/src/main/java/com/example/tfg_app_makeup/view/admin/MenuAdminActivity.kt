package com.example.tfg_app_makeup.view.admin

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.utils.Session

/**
 * Menú principal del perfil administradora (maquilladora).
 * Muestra la imagen y nombre del usuario logueado.
 */
class MenuAdminActivity : AppCompatActivity() {

    private lateinit var ivPerfilAdmin: ImageView
    private lateinit var tvBienvenida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_admin)

        ivPerfilAdmin = findViewById(R.id.ivPerfilAdmin)
        tvBienvenida = findViewById(R.id.tvBienvenida)

        cargarDatosUsuario()
    }

    /**
     * Muestra los datos del usuario actual en la interfaz.
     */
    private fun cargarDatosUsuario() {
        val usuario = Session.usuarioActual

        if (usuario == null) {
            Log.e("MenuAdminActivity", "No hay usuario activo en sesión.")
            tvBienvenida.text = "¡Hola!"
            return
        }

        tvBienvenida.text = "¡Hola, ${usuario.nombre}!"

        try {
            if (!usuario.imagenUrl.isNullOrBlank()) {
                Glide.with(this)
                    .load(usuario.imagenUrl)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .error(R.drawable.ic_user_placeholder)
                    .circleCrop()
                    .into(ivPerfilAdmin)

                Log.d("MenuAdminActivity", "Imagen de perfil cargada: ${usuario.imagenUrl}")
            } else {
                ivPerfilAdmin.setImageResource(R.drawable.ic_user_placeholder)
                Log.d("MenuAdminActivity", "Usuario sin imagen. Se usa imagen por defecto.")
            }
        } catch (e: Exception) {
            Log.e("MenuAdminActivity", "Error al cargar imagen de perfil: ${e.message}", e)
        }
    }
}
