package com.example.tfg_app_makeup.view.admin.citas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

/**
 * Calendario principal del perfil administradora para ver y acceder a citas.
 * Permite ver días con citas aceptadas y navegar a cada sección relacionada.
 */
class CitasAdminActivity : BaseDrawerActivity() {

    private lateinit var citaController: CitaController
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var etFechaBuscar: EditText
    private lateinit var btnBuscarCita: ImageButton
    private lateinit var btnVolver: ImageButton
    private lateinit var btnCitasPendientes: LinearLayout
    private lateinit var fabNuevaCita: View

    private var listaCitas = listOf<Cita>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usa directamente el layout de contenido (NO inflar el base aquí)
        setContentView(R.layout.activity_admin_citas)

        citaController = CitaController(this)

        inicializarComponentes()
        configurarListeners()
        configurarMenuHamburguesa()
        cargarCitasAceptadasYDecorar()
    }

    override fun onResume() {
        super.onResume()
        cargarCitasAceptadasYDecorar()
    }

    private fun inicializarComponentes() {
        calendarView = findViewById(R.id.calendarView)
        etFechaBuscar = findViewById(R.id.etFechaBuscarCita)
        btnBuscarCita = findViewById(R.id.btnBuscarCita)
        btnVolver = findViewById(R.id.btnVolverAdminCitas)
        btnCitasPendientes = findViewById(R.id.btnCitasPendientes)
        fabNuevaCita = findViewById(R.id.fabNuevaCita)
    }

    private fun configurarListeners() {
        calendarView.setOnDateChangedListener { _, date, _ ->
            val fechaSeleccionada = "%02d/%02d/%04d".format(date.day, date.month + 1, date.year)
            val intent = Intent(this, CitasDiaActivity::class.java)
            intent.putExtra("fechaSeleccionada", fechaSeleccionada)
            startActivity(intent)
        }

        btnBuscarCita.setOnClickListener {
            val fechaTexto = etFechaBuscar.text.toString().trim()
            if (fechaTexto.isEmpty()) {
                Toast.makeText(this, "Introduce una fecha válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!CitaHelper.validarFormatoFecha(this, fechaTexto)) return@setOnClickListener

            val intent = Intent(this, CitasDiaActivity::class.java)
            intent.putExtra("fechaSeleccionada", fechaTexto)
            startActivity(intent)
        }

        btnCitasPendientes.setOnClickListener {
            startActivity(Intent(this, CitasPendientesActivity::class.java))
        }

        btnVolver.setOnClickListener {
            finish()
        }

        fabNuevaCita.setOnClickListener {
            startActivity(Intent(this, FormularioCitaManualActivity::class.java))
        }
    }

    private fun cargarCitasAceptadasYDecorar() {
        try {
            listaCitas = citaController.obtenerCitasAceptadas()
            CitaHelper.decorarFechasConCitas(this, calendarView, listaCitas)
        } catch (e: Exception) {
            Log.e("CitasAdmin", "Error al cargar o decorar calendario", e)
        }
    }
}
