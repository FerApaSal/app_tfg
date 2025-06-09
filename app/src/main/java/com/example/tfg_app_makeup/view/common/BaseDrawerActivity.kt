package com.example.tfg_app_makeup.view.common

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
        // Infla la vista con el drawer
        val root = layoutInflater.inflate(R.layout.activity_base_drawer, null)
        val content = root.findViewById<View>(R.id.contenidoPrincipal) as View
        layoutInflater.inflate(layoutResID, content as ViewGroup)

        super.setContentView(root)

        drawerLayout = root.findViewById(R.id.drawerLayout)
        navigationView = root.findViewById(R.id.navView)

        configurarDrawer()
    }

    private fun configurarDrawer() {
        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.nav_inicio -> startActivity(Intent(this, MenuAdminActivity::class.java))
                R.id.nav_citas -> startActivity(Intent(this, CitasAdminActivity::class.java))
                R.id.nav_clientes -> startActivity(Intent(this, ListaClientesActivity::class.java))
                R.id.nav_materiales -> startActivity(Intent(this, MaterialListActivity::class.java))
                R.id.nav_tareas -> startActivity(Intent(this, ToDoListActivity::class.java))
                R.id.nav_servicios_novia -> startActivity(Intent(this, SeccionNoviasActivity::class.java))
            }
            true
        }
    }

    fun configurarMenuHamburguesa() {
        findViewById<ImageButton>(R.id.btnMenuHamburguesa)?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
