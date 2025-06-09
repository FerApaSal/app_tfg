package com.example.tfg_app_makeup.view.admin.toDoList

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapter.TareaAdapter
import com.example.tfg_app_makeup.controllers.TareaController
import com.example.tfg_app_makeup.model.Tarea
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Pantalla principal de la sección ToDoList.
 * Muestra las tareas con opciones para agregar, editar, eliminar y marcar como completadas.
 */
class ToDoListActivity : BaseDrawerActivity() {

    private lateinit var rvTareas: RecyclerView
    private lateinit var fabAgregarTarea: FloatingActionButton
    private lateinit var btnVolver: ImageButton
    private lateinit var tvTitulo: TextView

    private lateinit var tareaController: TareaController
    private lateinit var tareaAdapter: TareaAdapter
    private var listaTareas = mutableListOf<Tarea>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solo inflamos el contenido específico, BaseDrawerActivity se encarga del layout base
        setContentView(R.layout.activity_todolist)

        tareaController = TareaController(this)

        inicializarComponentes()
        configurarListeners()
        configurarMenuHamburguesa()
    }

    override fun onResume() {
        super.onResume()
        cargarTareas()
    }

    /**
     * Enlaza elementos visuales y configura el RecyclerView.
     */
    private fun inicializarComponentes() {
        rvTareas = findViewById(R.id.rvTareas)
        fabAgregarTarea = findViewById(R.id.fabAgregarTarea)
        btnVolver = findViewById(R.id.btnVolverToDo)
        tvTitulo = findViewById(R.id.tvTituloToDo)

        rvTareas.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Asigna listeners para los botones de la vista.
     */
    private fun configurarListeners() {
        fabAgregarTarea.setOnClickListener {
            val intent = Intent(this, FormularioTareaActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Carga las tareas desde la base de datos y refresca el adapter.
     */
    private fun cargarTareas() {
        try {
            listaTareas = tareaController.obtenerTodas().toMutableList()
            tareaAdapter = TareaAdapter(
                context = this,
                tareas = listaTareas,
                onEditar = { tarea ->
                    val intent = Intent(this, FormularioTareaActivity::class.java)
                    intent.putExtra("tarea", tarea)
                    startActivity(intent)
                },
                onEliminar = { tarea ->
                    mostrarDialogoConfirmacion(tarea)
                },
                onMarcarCompletada = { tarea, completada ->
                    tarea.completada = completada
                    if (tareaController.actualizar(tarea)) {
                        Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            rvTareas.adapter = tareaAdapter
        } catch (e: Exception) {
            Log.e("ToDoListActivity", "Error al cargar tareas", e)
        }
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar la tarea.
     */
    private fun mostrarDialogoConfirmacion(tarea: Tarea) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                if (tareaController.eliminar(tarea.id)) {
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                    cargarTareas()
                } else {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
