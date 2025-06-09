package com.example.tfg_app_makeup.view.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.admin.citas.CitasAdminActivity
import com.example.tfg_app_makeup.view.admin.material.MaterialListActivity
import com.example.tfg_app_makeup.view.admin.toDoList.ToDoListActivity
import com.example.tfg_app_makeup.view.common.PerfilActivity

/**
 * Menú principal del perfil administradora (maquilladora).
 * Muestra la imagen y nombre del usuario logueado.
 * Contiene accesos a funcionalidades específicas del perfil.
 */
class MenuAdminActivity : BaseDrawerActivity() {

    private lateinit var ivPerfilAdmin: ImageView
    private lateinit var tvBienvenida: TextView

    // Botones de navegación
    private lateinit var btnCitas: Button
    private lateinit var btnClientes: Button
    private lateinit var btnMateriales: Button
    private lateinit var btnToDoList: Button
    private lateinit var btnNovias: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_base_drawer)

        // Inflar el layout específico dentro del contenedor
        val contenido = layoutInflater.inflate(R.layout.activity_menu_admin, findViewById(R.id.contenidoPrincipal))

        // Acceder a los componentes dentro del layout inflado
        ivPerfilAdmin = contenido.findViewById(R.id.ivPerfilAdmin)
        tvBienvenida = contenido.findViewById(R.id.tvBienvenida)

        btnCitas = contenido.findViewById(R.id.btnGestionCitas)
        btnClientes = contenido.findViewById(R.id.btnGestionUsuarios)
        btnMateriales = contenido.findViewById(R.id.btnGestionMateriales)
        btnToDoList = contenido.findViewById(R.id.btnGestionTareas)
        btnNovias = contenido.findViewById(R.id.btnSeccionNovias)
        btnCerrarSesion = contenido.findViewById(R.id.btnCerrarSesion)

        cargarDatosUsuario()
        configurarListeners()
        configurarMenuHamburguesa()
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

    /**
     * Configura los listeners para los botones de navegación.
     * Se activarán progresivamente al completar las pantallas correspondientes.
     */
    private fun configurarListeners() {

         btnCitas.setOnClickListener {
             startActivity(Intent(this, CitasAdminActivity::class.java))
         }

         btnClientes.setOnClickListener {
             startActivity(Intent(this, ListaClientesActivity::class.java))
         }

         btnMateriales.setOnClickListener {
             startActivity(Intent(this, MaterialListActivity::class.java))
         }

         btnToDoList.setOnClickListener {
             startActivity(Intent(this, ToDoListActivity::class.java))
         }

        btnNovias.setOnClickListener {
            startActivity(Intent(this, SeccionNoviasActivity::class.java))
        }

        ivPerfilAdmin.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            Log.d("MenuAdminActivity", "Acceso a perfil desde imagen admin")
        }


        btnCerrarSesion.setOnClickListener {
            Session.cerrarSesion(this)
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        }
    }
}
