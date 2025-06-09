package com.example.tfg_app_makeup.view.common

import android.content.Intent
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.view.admin.*
import com.example.tfg_app_makeup.view.admin.citas.CitasAdminActivity
import com.example.tfg_app_makeup.view.admin.material.ListaMaterialesActivity
import com.example.tfg_app_makeup.view.admin.toDoList.ListaTareasActivity
import com.google.android.material.navigation.NavigationView

/**
 * Actividad base para todas las vistas que deben incluir el menú lateral (drawer).
 * Proporciona navegación común y mecanismo para cargar contenidos dinámicamente en el contenedor.
 */
open class BaseDrawerActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    /**
     * Sobrescribe setContentView para cargar primero la base del drawer y luego el contenido específico.
     * @param layoutResID ID del layout que se quiere mostrar dentro del contenedor principal del drawer.
     */
    override fun setContentView(layoutResID: Int) {
        // Infla la base con DrawerLayout
        val root = layoutInflater.inflate(R.layout.activity_base_drawer, null)
        val contentFrame = root.findViewById<FrameLayout>(R.id.contenidoPrincipal)
        layoutInflater.inflate(layoutResID, contentFrame, true)
        super.setContentView(root)

        drawerLayout = root.findViewById(R.id.drawerLayout)
        navigationView = root.findViewById(R.id.navView)

        configurarDrawer()
    }

    /**
     * Configura el comportamiento del menú lateral (drawer),
     * incluyendo las acciones para cada opción del menú de navegación.
     */
    private fun configurarDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()

            val intent = when (item.itemId) {
                R.id.nav_inicio -> Intent(this, MenuAdminActivity::class.java)
                R.id.nav_citas -> Intent(this, CitasAdminActivity::class.java)
                R.id.nav_clientes -> Intent(this, ListaClientesActivity::class.java)
                R.id.nav_materiales -> Intent(this, ListaMaterialesActivity::class.java)
                R.id.nav_tareas -> Intent(this, ListaTareasActivity::class.java)
                R.id.nav_servicios_novia -> Intent(this, SeccionNoviasActivity::class.java)
                else -> null
            }

            // Solo lanza el intent si no estamos ya en esa actividad
            intent?.let {
                val claseActual = this::class.java
                val claseDestino = it.component?.className?.let { name -> Class.forName(name) }
                if (claseActual != claseDestino) {
                    startActivity(it)
                }
            }

            true
        }
    }

    /**
     * Asocia el botón de hamburguesa con la apertura del menú lateral.
     * Este método debe llamarse desde cada actividad hija después de inflar su vista.
     */
    fun configurarMenuHamburguesa() {
        findViewById<ImageButton>(R.id.btnMenuHamburguesa)?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    /**
     * Sobrescribe el comportamiento del botón de retroceso para cerrar el menú si está abierto.
     */
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
