package com.example.tfg_app_makeup.view.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.view.admin.citas.CitasAdminActivity
import com.example.tfg_app_makeup.view.admin.material.ListaMaterialesActivity
import com.example.tfg_app_makeup.view.admin.toDoList.ListaTareasActivity
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.example.tfg_app_makeup.view.common.PerfilActivity

class MenuAdminActivity : BaseDrawerActivity() {

    private lateinit var tvBienvenida: TextView
    private lateinit var ivPerfilAdmin: ImageView
    private lateinit var btnCitas: Button
    private lateinit var btnClientes: Button
    private lateinit var btnMateriales: Button
    private lateinit var btnToDoList: Button
    private lateinit var btnNovias: Button
    private lateinit var btnCerrarSesion: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ¡Solo esto! BaseDrawerActivity se encarga del resto
        setContentView(R.layout.activity_menu_admin)

        inicializarComponentes()
        configurarListeners()
        configurarMenuHamburguesa()
        cargarDatosUsuario()
    }

    override fun onResume() {
        super.onResume()
        cargarDatosUsuario()
    }

    private fun inicializarComponentes() {
        tvBienvenida = findViewById(R.id.tvBienvenida)
        ivPerfilAdmin = findViewById(R.id.ivPerfilAdmin)
        btnCitas = findViewById(R.id.btnGestionCitas)
        btnClientes = findViewById(R.id.btnGestionUsuarios)
        btnMateriales = findViewById(R.id.btnGestionMateriales)
        btnToDoList = findViewById(R.id.btnGestionTareas)
        btnNovias = findViewById(R.id.btnSeccionNovias)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
    }

    private fun configurarListeners() {
        btnCitas.setOnClickListener { startActivity(Intent(this, CitasAdminActivity::class.java)) }
        btnClientes.setOnClickListener { startActivity(Intent(this, ListaClientesActivity::class.java)) }
        btnMateriales.setOnClickListener { startActivity(Intent(this, ListaMaterialesActivity::class.java)) }
        btnToDoList.setOnClickListener { startActivity(Intent(this, ListaTareasActivity::class.java)) }
        btnNovias.setOnClickListener { startActivity(Intent(this, SeccionNoviasActivity::class.java)) }
        ivPerfilAdmin.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
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

    private fun cargarDatosUsuario() {
        val usuario = Session.usuarioActual
        tvBienvenida.text = usuario?.let { "¡Hola, ${it.nombre}!" } ?: "¡Hola!"
        UsuarioHelper.cargarImagenPerfil(this, usuario?.imagenUrl, ivPerfilAdmin)
    }
}
