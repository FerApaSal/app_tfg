package com.example.tfg_app_makeup.view.admin

import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapter.ListarClientesAdapter
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity

/**
 * Pantalla que muestra la lista de clientes registrados.
 * Vista exclusiva para la administradora, con datos básicos de contacto.
 */
class ListaClientesActivity : BaseDrawerActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var adapter: ListarClientesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)

        inicializarComponentes()
        configurarListeners()
        configurarMenuHamburguesa()
        cargarClientes()
    }

    private fun inicializarComponentes() {
        recyclerView = findViewById(R.id.rvListaClientes)
        btnVolver = findViewById(R.id.btnVolverListaClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarListeners() {
        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarClientes() {
        val clientes: List<Usuario> = UsuarioHelper.obtenerListaClientes(this)
        adapter = ListarClientesAdapter(clientes)
        recyclerView.adapter = adapter
    }
}
