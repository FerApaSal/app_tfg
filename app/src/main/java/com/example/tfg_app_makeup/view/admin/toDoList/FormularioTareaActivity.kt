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

        // Inicializa el controlador de tareas
        tareaController = TareaController(this)

        // Verifica si se está editando una tarea existente
        tareaExistente = intent.getSerializableExtra("tarea") as? Tarea

        inicializarComponentes() // Enlaza los elementos del layout
        configurarFormulario() // Configura el formulario según el contexto (nuevo o edición)
        configurarListeners() // Asigna comportamiento a los botones
    }

    /**
     * Enlaza variables con los elementos visuales del layout.
     */
    private fun inicializarComponentes() {
        etTitulo = findViewById(R.id.etTituloTarea)
        etDescripcion = findViewById(R.id.etDescripcionTarea)
        spinnerPrioridad = findViewById(R.id.spinnerPrioridad)
        cbCompletada = findViewById(R.id.cbCompletada)
        btnGuardar = findViewById(R.id.btnGuardarTarea)
        btnVolver = findViewById(R.id.btnVolverFormulario)

        // Configura el spinner con las prioridades disponibles
        val prioridades = arrayOf("Baja", "Media", "Alta", "Urgente")
        spinnerPrioridad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, prioridades)
    }

    /**
     * Configura el formulario dependiendo de si se está creando o editando una tarea.
     */
    private fun configurarFormulario() {
        if (tareaExistente != null) {
            title = "Editar tarea"
            findViewById<TextView>(R.id.tvTituloFormulario).text = "Editar tarea"

            // Rellena los campos con los datos de la tarea existente
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

    /**
     * Asigna comportamiento a los botones de guardar y volver.
     */
    private fun configurarListeners() {
        // Botón para volver a la actividad anterior
        btnVolver.setOnClickListener {
            finish()
        }

        // Botón para guardar la tarea
        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val prioridad = spinnerPrioridad.selectedItem.toString()
            val completada = cbCompletada.isChecked

            // Valida los campos obligatorios
            if (!TareaHelper.validarCampos(this, titulo, descripcion, prioridad)) return@setOnClickListener

            // Inserta o actualiza la tarea según corresponda
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

    /**
     * Devuelve el índice correspondiente a la prioridad seleccionada.
     */
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