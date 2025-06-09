package com.example.tfg_app_makeup.view.admin.toDoList

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.TareaController
import com.example.tfg_app_makeup.helpers.TareaHelper
import com.example.tfg_app_makeup.model.Tarea
import java.util.*

/**
 * Pantalla de formulario para crear o editar una tarea.
 * Si se recibe una tarea por intent, se usa modo edición.
 */
class FormularioTareaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var spinnerPrioridad: Spinner
    private lateinit var cbCompletada: CheckBox
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: ImageButton

    private var tareaExistente: Tarea? = null
    private lateinit var tareaController: TareaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_tarea)

        tareaController = TareaController(this)
        tareaExistente = intent.getSerializableExtra("tarea") as? Tarea

        inicializarComponentes()
        configurarFormulario()
        configurarListeners()
    }

    private fun inicializarComponentes() {
        etTitulo = findViewById(R.id.etTituloTarea)
        etDescripcion = findViewById(R.id.etDescripcionTarea)
        spinnerPrioridad = findViewById(R.id.spinnerPrioridad)
        cbCompletada = findViewById(R.id.cbCompletada)
        btnGuardar = findViewById(R.id.btnGuardarTarea)
        btnVolver = findViewById(R.id.btnVolverFormulario)

        val prioridades = arrayOf("Baja", "Media", "Alta", "Urgente")
        spinnerPrioridad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prioridades)
    }

    private fun configurarFormulario() {
        if (tareaExistente != null) {
            title = "Editar tarea"
            findViewById<TextView>(R.id.tvTituloFormulario).text = "Editar tarea"

            etTitulo.setText(tareaExistente!!.titulo)
            etDescripcion.setText(tareaExistente!!.descripcion)
            spinnerPrioridad.setSelection(obtenerIndicePrioridad(tareaExistente!!.prioridad))
            cbCompletada.isChecked = tareaExistente!!.completada
            cbCompletada.visibility = CheckBox.VISIBLE
        } else {
            title = "Nueva tarea"
            cbCompletada.visibility = CheckBox.GONE
        }
    }

    private fun configurarListeners() {
        btnVolver.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val prioridad = spinnerPrioridad.selectedItem.toString()
            val completada = cbCompletada.isChecked

            if (!TareaHelper.validarCampos(this, titulo, descripcion, prioridad)) return@setOnClickListener

            if (tareaExistente == null) {
                val nuevaTarea = Tarea(
                    id = UUID.randomUUID().toString(),
                    titulo = titulo,
                    descripcion = descripcion,
                    prioridad = prioridad,
                    completada = false
                )
                tareaController.insertar(nuevaTarea)
                Toast.makeText(this, "Tarea creada", Toast.LENGTH_SHORT).show()
            } else {
                tareaExistente!!.titulo = titulo
                tareaExistente!!.descripcion = descripcion
                tareaExistente!!.prioridad = prioridad
                tareaExistente!!.completada = completada

                tareaController.actualizar(tareaExistente!!)
                Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    private fun obtenerIndicePrioridad(valor: String): Int {
        return when (valor.lowercase()) {
            "baja" -> 0
            "media" -> 1
            "alta" -> 2
            "urgente" -> 3
            else -> 0
        }
    }
}
