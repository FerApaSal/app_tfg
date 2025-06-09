package com.example.tfg_app_makeup.view.admin

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.adapter.ListarClientesAdapter
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity

/**
 * Muestra una lista de clientes registrados, visible para la administradora.
 * Los datos mostrados incluyen nombre, apellidos, correo y teléfono.
 */
class ListaClientesActivity : BaseDrawerActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var adapter: ListarClientesAdapter
    private lateinit var controller: UsuarioController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)

        recyclerView = findViewById(R.id.rvListaClientes)
        btnVolver = findViewById(R.id.btnVolverListaClientes)

        controller = UsuarioController(this)
        val clientes: List<Usuario> = controller.obtenerUsuariosPorRol("CLIENTE")

        adapter = ListarClientesAdapter(clientes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
