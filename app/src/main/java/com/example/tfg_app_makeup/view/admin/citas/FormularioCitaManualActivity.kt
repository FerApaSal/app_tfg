package com.example.tfg_app_makeup.view.admin.citas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.utils.Session
import java.util.*

/**
 * Formulario para que la administradora cree citas manuales para clientes externos.
 */
class FormularioCitaManualActivity : AppCompatActivity() {

    private lateinit var etNombreCliente: EditText
    private lateinit var etTelefonoCliente: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etFecha: EditText
    private lateinit var etHora: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnVolver: ImageButton

    private lateinit var citaController: CitaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_cita_manual)

        citaController = CitaController(this)
        inicializarComponentes()
        configurarPickers()
        configurarListeners()
    }

    /**
     * Enlaza variables con los elementos visuales del layout.
     */
    private fun inicializarComponentes() {
        etNombreCliente = findViewById(R.id.etNombreCliente)
        etTelefonoCliente = findViewById(R.id.etTelefonoCliente)
        etDireccion = findViewById(R.id.etDireccionManual)
        etFecha = findViewById(R.id.etFechaManual)
        etHora = findViewById(R.id.etHoraManual)
        spinnerTipo = findViewById(R.id.spinnerTipoServicioManual)
        btnGuardar = findViewById(R.id.btnGuardarCitaManual)
        btnVolver = findViewById(R.id.btnVolverFormularioCitaManual)

        val tipos = arrayOf("Novia", "Madrina", "Invitada", "Madre del novia/a", "Comunión", "Bautizo")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
    }

    /**
     * Configura los DatePicker y TimePicker para los campos de fecha y hora.
     */
    private fun configurarPickers() {
        etFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                R.style.PickerDialogTheme,
                { _, year, month, day ->
                    val fechaFormateada = String.format("%02d/%02d/%04d", day, month + 1, year)
                    etFecha.setText(fechaFormateada)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis()
            datePicker.show()
        }

        etHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                this,
                R.style.PickerDialogTheme,
                { _, hour, minute ->
                    val horaFormateada = String.format("%02d:%02d", hour, minute)
                    etHora.setText(horaFormateada)
                },
                calendario.get(Calendar.HOUR_OF_DAY),
                calendario.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }
    }

    /**
     * Asigna comportamiento a los botones de guardar y volver.
     */
    private fun configurarListeners() {
        btnGuardar.setOnClickListener {
            val nombre = etNombreCliente.text.toString().trim()
            val telefono = etTelefonoCliente.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val hora = etHora.text.toString().trim()
            val tipo = spinnerTipo.selectedItem.toString()

            if (CitaHelper.hayCamposVacios(this, nombre, telefono, direccion, fecha, hora)) return@setOnClickListener

            val idUsuario = Session.usuarioActual?.id
            if (idUsuario.isNullOrBlank()) {
                Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Se incluyen campos manuales para nombre y teléfono del cliente externo
            val nuevaCita = Cita(
                id = UUID.randomUUID().toString(),
                tipoServicio = tipo,
                fecha = fecha,
                hora = hora,
                direccion = direccion,
                estado = "ACEPTADA",
                idUsuario = idUsuario,
                nombreClienteManual = nombre,
                telefonoClienteManual = telefono
            )

            if (citaController.insertar(nuevaCita)) {
                Toast.makeText(this, "Cita creada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "No se pudo crear la cita", Toast.LENGTH_SHORT).show()
            }
        }

        btnVolver.setOnClickListener { finish() }
    }
}
