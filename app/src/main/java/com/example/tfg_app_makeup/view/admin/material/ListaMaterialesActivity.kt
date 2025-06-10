package com.example.tfg_app_makeup.view.admin.material

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapters.MaterialAdapter
import com.example.tfg_app_makeup.controllers.MaterialController
import com.example.tfg_app_makeup.helpers.MaterialHelper
import com.example.tfg_app_makeup.model.Material
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Pantalla de administradora que muestra y gestiona la lista de materiales.
 * Integra menú lateral (drawer) y acciones CRUD para cada material.
 */
class ListaMaterialesActivity : BaseDrawerActivity() {

    private lateinit var rvMateriales: RecyclerView
    private lateinit var fabAgregarMaterial: FloatingActionButton
    private lateinit var btnVolver: ImageButton
    private lateinit var tvTitulo: TextView

    private lateinit var materialController: MaterialController
    private lateinit var materialAdapter: MaterialAdapter
    private var listaMateriales = mutableListOf<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_materiales)

        // Inicializa el controlador de materiales
        materialController = MaterialController(this)

        inicializarComponentes() // Enlaza los elementos del layout
        configurarListeners() // Configura los eventos de los botones
        configurarMenuHamburguesa() // Configura el menú lateral
    }

    override fun onResume() {
        super.onResume()
        cargarMateriales() // Recarga la lista de materiales al reanudar la actividad
    }

    /**
     * Enlaza variables con los elementos visuales del layout.
     */
    private fun inicializarComponentes() {
        rvMateriales = findViewById(R.id.rvMateriales)
        fabAgregarMaterial = findViewById(R.id.fabAgregarMaterial)
        btnVolver = findViewById(R.id.btnVolverMateriales)
        tvTitulo = findViewById(R.id.tvTituloMateriales)

        // Configura el RecyclerView con un layout lineal
        rvMateriales.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Configura los eventos de los botones.
     */
    private fun configurarListeners() {
        // Botón para agregar un nuevo material
        fabAgregarMaterial.setOnClickListener {
            val intent = Intent(this, FormularioMaterialActivity::class.java)
            startActivity(intent)
        }

        // Botón para volver a la actividad anterior
        btnVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Carga la lista de materiales desde el controlador y configura el adaptador.
     */
    private fun cargarMateriales() {
        try {
            // Obtiene todos los materiales desde el controlador
            listaMateriales = materialController.obtenerTodos().toMutableList()

            // Configura el adaptador con los materiales y las acciones de editar/eliminar
            materialAdapter = MaterialAdapter(
                context = this,
                materiales = listaMateriales,
                onEditar = { material ->
                    // Abre el formulario para editar el material seleccionado
                    val intent = Intent(this, FormularioMaterialActivity::class.java)
                    intent.putExtra("material", material)
                    startActivity(intent)
                },
                onEliminar = { material ->
                    // Confirma y elimina el material seleccionado
                    MaterialHelper.confirmarEliminacion(this, material, materialController) {
                        cargarMateriales() // Recarga la lista tras eliminar
                    }
                }
            )

            // Asigna el adaptador al RecyclerView
            rvMateriales.adapter = materialAdapter
        } catch (e: Exception) {
            // Maneja errores al cargar los materiales
            Log.e("MaterialesActivity", "Error al cargar materiales", e)
            Toast.makeText(this, "Error al cargar materiales", Toast.LENGTH_SHORT).show()
        }
    }
}