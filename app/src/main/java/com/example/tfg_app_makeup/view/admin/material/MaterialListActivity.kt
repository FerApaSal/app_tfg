package com.example.tfg_app_makeup.view.admin.material

import android.app.AlertDialog
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
import com.example.tfg_app_makeup.model.Material
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.example.tfg_app_makeup.view.admin.materiales.FormularioMaterialActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Muestra la lista de materiales y permite acciones CRUD.
 */
class MaterialListActivity : BaseDrawerActivity() {

    private lateinit var rvMateriales: RecyclerView
    private lateinit var fabAgregarMaterial: FloatingActionButton
    private lateinit var btnVolver: ImageButton
    private lateinit var tvTitulo: TextView

    private lateinit var materialController: MaterialController
    private lateinit var materialAdapter: MaterialAdapter
    private var listaMateriales = mutableListOf<Material>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Drawer gestionado por BaseDrawerActivity
        setContentView(R.layout.activity_lista_materiales)

        materialController = MaterialController(this)

        inicializarComponentes()
        configurarListeners()
        configurarMenuHamburguesa()
    }

    override fun onResume() {
        super.onResume()
        cargarMateriales()
    }

    private fun inicializarComponentes() {
        rvMateriales = findViewById(R.id.rvMateriales)
        fabAgregarMaterial = findViewById(R.id.fabAgregarMaterial)
        btnVolver = findViewById(R.id.btnVolverMateriales)
        tvTitulo = findViewById(R.id.tvTituloMateriales)

        rvMateriales.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarListeners() {
        fabAgregarMaterial.setOnClickListener {
            val intent = Intent(this, FormularioMaterialActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cargarMateriales() {
        try {
            listaMateriales = materialController.obtenerTodos().toMutableList()
            materialAdapter = MaterialAdapter(
                context = this,
                materiales = listaMateriales,
                onEditar = { material ->
                    val intent = Intent(this, FormularioMaterialActivity::class.java)
                    intent.putExtra("material", material)
                    startActivity(intent)
                },
                onEliminar = { material ->
                    mostrarConfirmacionEliminacion(material)
                }
            )
            rvMateriales.adapter = materialAdapter
        } catch (e: Exception) {
            Log.e("MaterialesActivity", "Error al cargar materiales", e)
            Toast.makeText(this, "Error al cargar materiales", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarConfirmacionEliminacion(material: Material) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar material")
            .setMessage("¿Estás seguro de que deseas eliminar este material?")
            .setPositiveButton("Sí") { _, _ ->
                if (materialController.eliminar(material.id)) {
                    Toast.makeText(this, "Material eliminado", Toast.LENGTH_SHORT).show()
                    cargarMateriales()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
