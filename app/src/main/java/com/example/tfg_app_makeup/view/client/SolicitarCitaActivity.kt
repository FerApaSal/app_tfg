@file:Suppress("MissingInflatedId")

package com.example.tfg_app_makeup.view.client

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.services.UsuarioService
import com.example.tfg_app_makeup.utils.Session
import java.util.*

/**
 * Actividad para que el cliente solicite una nueva cita.
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
        Log.d("SolicitarCita", "Usuario actual tras restaurar: ${Session.usuarioActual?.correo}")

        try {
            citaController = CitaController(this)

            // Comprobar si el usuario está autenticado
            if (Session.usuarioActual == null) {
                Toast.makeText(this, "Debes iniciar sesión para solicitar una cita", Toast.LENGTH_SHORT).show()
                finish() // Cerrar actividad si no hay usuario autenticado
                return
            }
        } catch (e: Exception) {
            Log.e("SolicitarCitaActivity", "Error al inicializar la actividad: ${e.message}", e)
            Toast.makeText(this, "Error al cargar la pantalla de solicitud de cita", Toast.LENGTH_LONG).show()
            finish() // Cerrar actividad en caso de error
            return
        }

        // Referencias
        spinnerTipo = findViewById(R.id.spinnerTipo)
        etFecha = findViewById(R.id.etFecha)
        etHora = findViewById(R.id.etHora)
        etDireccion = findViewById(R.id.etDireccion)
        cbAceptarCondiciones = findViewById(R.id.cbAceptarCondiciones)
        btnSolicitar = findViewById(R.id.btnSolicitar)
        btnVolver = findViewById(R.id.btnVolverSolicitar)

        // Adaptar spinner
        val tipos = arrayOf("Novia", "Madrina", "Invitada", "Madre Novio/a", "Comunión", "Bautizo")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        // Fecha
        etFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, day ->
                    val fechaFormateada = String.format("%02d/%02d/%04d", day, month + 1, year)
                    etFecha.setText(fechaFormateada)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Hora
        etHora.setOnClickListener {
            val calendario = Calendar.getInstance()
            val timePicker = TimePickerDialog(this,
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

        // Activar botón solo si se aceptan condiciones
        cbAceptarCondiciones.setOnCheckedChangeListener { _, isChecked ->
            btnSolicitar.isEnabled = isChecked
        }

        btnSolicitar.setOnClickListener {
            val tipo = spinnerTipo.selectedItem.toString()
            val fecha = etFecha.text.toString()
            val hora = etHora.text.toString()
            val direccion = etDireccion.text.toString()

            // Validar campos
            if (tipo.isEmpty() || fecha.isEmpty() || hora.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = Session.usuarioActual
            if (usuario == null || usuario.id.isBlank()) {
                Toast.makeText(this, "Sesión expirada o inválida", Toast.LENGTH_SHORT).show()
                Log.w("SolicitarCita", "ID de usuario no disponible al crear cita")
                return@setOnClickListener
            }

            val cita = citaController.crearCita(tipo, fecha, hora, direccion, usuario.id)

            if (cita != null) {
                citaController.insertar(cita)

                Toast.makeText(this, "Cita solicitada correctamente", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1000)

            } else {
                Toast.makeText(this, "Error al crear la cita", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
