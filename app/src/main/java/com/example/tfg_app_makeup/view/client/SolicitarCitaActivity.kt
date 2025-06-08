package com.example.tfg_app_makeup.view.client

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.helpers.UsuarioHelper
import com.example.tfg_app_makeup.services.UsuarioService
import com.example.tfg_app_makeup.utils.Session
import java.util.*

/**
 * Pantalla para que el usuario cliente solicite una nueva cita.
 * Incluye selección de tipo de servicio, fecha, hora y dirección.
 */
class SolicitarCitaActivity : AppCompatActivity() {

    private lateinit var spinnerTipo: Spinner
    private lateinit var etFecha: EditText
    private lateinit var etHora: EditText
    private lateinit var etDireccion: EditText
    private lateinit var cbAceptarCondiciones: CheckBox
    private lateinit var btnSolicitar: Button
    private lateinit var btnVolver: ImageButton

    private lateinit var citaController: CitaController

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_cita)

        Session.restaurarSesion(this, UsuarioService(this))

        try {
            citaController = CitaController(this)

            if (Session.usuarioActual == null) {
                Toast.makeText(this, "Debes iniciar sesión para solicitar una cita", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        } catch (e: Exception) {
            Log.e("SolicitarCitaActivity", "Error al inicializar: ${e.message}", e)
            Toast.makeText(this, "Error al cargar la pantalla de solicitud", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        inicializarComponentes()
        configurarSelectorTipoServicio()
        configurarPickerFecha()
        configurarPickerHora()

        cbAceptarCondiciones.setOnCheckedChangeListener { _, isChecked ->
            btnSolicitar.isEnabled = isChecked
        }

        btnSolicitar.setOnClickListener {
            procesarSolicitudCita()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Enlaza elementos visuales con las variables de clase.
     */
    private fun inicializarComponentes() {
        spinnerTipo = findViewById(R.id.spinnerTipo)
        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        etDireccion = findViewById(R.id.etDireccion)
        cbAceptarCondiciones = findViewById(R.id.cbAceptarCondiciones)
        btnSolicitar = findViewById(R.id.btnSolicitar)
        btnVolver = findViewById(R.id.btnVolverSolicitar)
    }

    /**
     * Configura las opciones disponibles en el selector de tipo de servicio.
     */
    private fun configurarSelectorTipoServicio() {
        val tipos = arrayOf("Novia", "Madrina", "Invitada", "Madre Novio/a", "Comunión", "Bautizo")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
    }

    /**
     * Abre un selector de fecha con formato dd/MM/yyyy.
     */
    private fun configurarPickerFecha() {
        etFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                ContextThemeWrapper(this, R.style.PickerDialogTheme),
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
    }

    /**
     * Abre un selector de hora en formato 24h.
     */
    private fun configurarPickerHora() {
        etHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                ContextThemeWrapper(this, R.style.PickerDialogTheme),
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
     * Valida los campos y crea una nueva cita en base a los datos del formulario.
     */
    private fun procesarSolicitudCita() {
        val tipo = spinnerTipo.selectedItem.toString()
        val fecha = etFecha.text.toString()
        val hora = etHora.text.toString()
        val direccion = etDireccion.text.toString()

        if (UsuarioHelper.hayCamposVacios(tipo, fecha, hora, direccion)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuario = Session.usuarioActual
        if (usuario == null || usuario.id.isBlank()) {
            Toast.makeText(this, "Sesión expirada o inválida", Toast.LENGTH_SHORT).show()
            Log.w("SolicitarCita", "ID de usuario no disponible al crear cita")
            return
        }

        val cita = citaController.crearCita(tipo, fecha, hora, direccion, usuario.id)

        if (cita != null) {
            citaController.insertar(cita)
            Toast.makeText(this, "Cita solicitada correctamente", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 1000)
        } else {
            Toast.makeText(this, "Error al crear la cita", Toast.LENGTH_SHORT).show()
        }
    }
}