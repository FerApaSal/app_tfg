package com.example.tfg_app_makeup.view.common

import android.content.Intent
import android.widget.FrameLayout
import android.widget.ImageButton
import android.view.ViewGroup
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.view.admin.*
import com.example.tfg_app_makeup.view.admin.citas.CitasAdminActivity
import com.example.tfg_app_makeup.view.admin.material.MaterialListActivity
import com.example.tfg_app_makeup.view.admin.toDoList.ToDoListActivity
import com.google.android.material.navigation.NavigationView

open class BaseDrawerActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun setContentView(layoutResID: Int) {
        // Infla la base con el DrawerLayout
        val root = layoutInflater.inflate(R.layout.activity_base_drawer, null)
        val contentFrame = root.findViewById<FrameLayout>(R.id.contenidoPrincipal)
        layoutInflater.inflate(layoutResID, contentFrame, true)

        super.setContentView(root)

        // Inicializar Drawer y NavigationView
        drawerLayout = root.findViewById(R.id.drawerLayout)
        navigationView = root.findViewById(R.id.navView)

        configurarDrawer()
    }

    private fun configurarDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()

            // Previene abrir la misma actividad
            val intent = when (item.itemId) {
                R.id.nav_inicio -> Intent(this, MenuAdminActivity::class.java)
                R.id.nav_citas -> Intent(this, CitasAdminActivity::class.java)
                R.id.nav_clientes -> Intent(this, ListaClientesActivity::class.java)
                R.id.nav_materiales -> Intent(this, MaterialListActivity::class.java)
                R.id.nav_tareas -> Intent(this, ToDoListActivity::class.java)
                R.id.nav_servicios_novia -> Intent(this, SeccionNoviasActivity::class.java)
                else -> null
            }

            intent?.let {
                // Evita recargar la misma actividad
                if (this::class.java != it.component?.className?.let { name -> Class.forName(name) }) {
                    startActivity(it)
                }
            }

            true
        }
    }

    fun configurarMenuHamburguesa() {
        findViewById<ImageButton>(R.id.btnMenuHamburguesa)?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
