package com.example.tfg_app_makeup.view.admin.material

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.MaterialController
import com.example.tfg_app_makeup.helpers.MaterialHelper
import com.example.tfg_app_makeup.model.Material
import java.io.File
import java.util.*

/**
 * Actividad para gestionar el formulario de creación o edición de materiales.
 */
class FormularioMaterialActivity : AppCompatActivity() {

    private lateinit var ivPreviewMaterial: ImageView
    private lateinit var btnSubirImagen: Button
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var etCantidad: EditText
    private lateinit var spinnerEstado: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: ImageButton

    private var materialExistente: Material? = null
    private lateinit var materialController: MaterialController
    private var rutaImagenSeleccionada: String? = null

    private val REQUEST_CODE_PICK_IMAGE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_material)

        // Inicializa el controlador y verifica si se está editando un material existente
        materialController = MaterialController(this)
        materialExistente = intent.getSerializableExtra("material") as? Material

        inicializarComponentes() // Enlaza los elementos del layout
        configurarFormulario() // Configura el formulario según el contexto (nuevo o edición)
        configurarListeners() // Asigna comportamiento a los botones
    }

    /**
     * Enlaza variables con los elementos visuales del layout.
     */
    private fun inicializarComponentes() {
        ivPreviewMaterial = findViewById(R.id.ivPreviewMaterial)
        btnSubirImagen = findViewById(R.id.btnSubirImagenMaterial)
        etNombre = findViewById(R.id.etNombreMaterial)
        etDescripcion = findViewById(R.id.etDescripcionMaterial)
        spinnerTipo = findViewById(R.id.spinnerTipoMaterial)
        etCantidad = findViewById(R.id.etCantidadMaterial)
        spinnerEstado = findViewById(R.id.spinnerEstadoMaterial)
        btnGuardar = findViewById(R.id.btnGuardarMaterial)
        btnVolver = findViewById(R.id.btnVolverFormularioMaterial)

        // Configura el spinner de tipos de material
        val tipos = arrayOf("Paletas de sombras", "Pestañas postizas", "Esponjas",
            "Brochas", "Borlas", "Bronceador",
            "Colorete", "Iluminador", "Polvos matificantes",
            "Lápiz labios", "Lápiz ojos", "Lápiz cejas",
            "Pegamento de pestaña postiza", "Glitter", "Desmaquillante",
            "Bases de maquillaje", "Correctores", "Máscara de pestaña",
            "Pintalabios", "Eyeliner")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Configura el spinner de estados del material
        val estados = arrayOf("Nuevo", "Sucio", "Normal", "Reponer", "Viejo", "Roto", "Caducado")
        spinnerEstado.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
    }

    /**
     * Configura el formulario dependiendo de si se está creando o editando un material.
     */
    private fun configurarFormulario() {
        if (materialExistente != null) {
            title = "Editar material"
            findViewById<TextView>(R.id.tvTituloFormularioMaterial).text = "Editar material"

            // Rellena los campos con los datos del material existente
            etNombre.setText(materialExistente!!.nombre)
            etDescripcion.setText(materialExistente!!.descripcion)
            etCantidad.setText(materialExistente!!.cantidad.toString())

            val indexTipo = (spinnerTipo.adapter as ArrayAdapter<String>).getPosition(materialExistente!!.tipo)
            spinnerTipo.setSelection(indexTipo)

            val indexEstado = (spinnerEstado.adapter as ArrayAdapter<String>).getPosition(materialExistente!!.estado)
            spinnerEstado.setSelection(indexEstado)

            // Carga la imagen del material si existe
            rutaImagenSeleccionada = materialExistente!!.imagenUrl
            rutaImagenSeleccionada?.let {
                Glide.with(this)
                    .load(File(it))
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(ivPreviewMaterial)
            }
        } else {
            title = "Nuevo material"
        }
    }

    /**
     * Asigna comportamiento a los botones de guardar, volver y subir imagen.
     */
    private fun configurarListeners() {
        btnVolver.setOnClickListener { finish() }

        btnSubirImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()
            val tipo = spinnerTipo.selectedItem.toString()
            val cantidadTexto = etCantidad.text.toString().trim()
            val estado = spinnerEstado.selectedItem.toString()

            // Validaciones de campos obligatorios y cantidad
            if (MaterialHelper.hayCamposVacios(nombre, cantidadTexto)) {
                Toast.makeText(this, "Completa los campos obligatorios: Nombre y cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!MaterialHelper.esCantidadValida(cantidadTexto)) {
                Toast.makeText(this, "Cantidad inválida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadTexto.toInt()

            // Inserta o actualiza el material según corresponda
            if (materialExistente == null) {
                val nuevoMaterial = Material(
                    id = UUID.randomUUID().toString(),
                    nombre = nombre,
                    descripcion = descripcion,
                    tipo = tipo,
                    cantidad = cantidad,
                    estado = estado,
                    imagenUrl = rutaImagenSeleccionada
                )
                materialController.insertar(nuevoMaterial)
                Toast.makeText(this, "Material creado", Toast.LENGTH_SHORT).show()
            } else {
                materialExistente!!.apply {
                    this.nombre = nombre
                    this.descripcion = descripcion
                    this.tipo = tipo
                    this.cantidad = cantidad
                    this.estado = estado
                    this.imagenUrl = rutaImagenSeleccionada ?: this.imagenUrl
                }
                materialController.actualizar(materialExistente!!)
                Toast.makeText(this, "Material actualizado", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

    /**
     * Maneja el resultado de la selección de imagen desde la galería.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            rutaImagenSeleccionada = MaterialHelper.guardarImagenDesdeGaleria(this, data.data!!)
            if (rutaImagenSeleccionada != null) {
                Glide.with(this)
                    .load(File(rutaImagenSeleccionada))
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(ivPreviewMaterial)
                Toast.makeText(this, "Imagen cargada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}