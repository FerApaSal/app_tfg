package com.example.tfg_app_makeup.view.admin.citas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import java.text.SimpleDateFormat
import java.util.*

class CitasAdminActivity : BaseDrawerActivity() {

    private lateinit var citaController: CitaController
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var etFechaBuscar: EditText
    private lateinit var btnBuscarCita: ImageButton
    private lateinit var btnVolver: ImageButton
    private lateinit var btnCitasPendientes: LinearLayout
    private lateinit var fabNuevaCita: View

    private var listaCitas = listOf<Cita>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_citas)

        citaController = CitaController(this)
        inicializarComponentes()
        configurarListeners()
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

        // Al seleccionar una fecha del calendario
        calendarView.setOnDateChangedListener { _, date, _ ->
            val fechaSeleccionada = "%02d/%02d/%04d".format(date.day, date.month + 1, date.year)
            val intent = Intent(this, CitasDiaActivity::class.java)
            intent.putExtra("fechaSeleccionada", fechaSeleccionada)
            startActivity(intent)
        }

        // Al pulsar el botón de búsqueda por fecha manual
        btnBuscarCita.setOnClickListener {
            val fechaTexto = etFechaBuscar.text.toString().trim()

            if (fechaTexto.isEmpty()) {
                Toast.makeText(this, "Introduce una fecha válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación básica de formato
            val formatoEsperado = Regex("""\d{2}/\d{2}/\d{4}""")
            if (!formatoEsperado.matches(fechaTexto)) {
                Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, CitasDiaActivity::class.java)
            intent.putExtra("fechaSeleccionada", fechaTexto)
            startActivity(intent)
        }

        btnCitasPendientes.setOnClickListener {
            val intent = Intent(this, CitasPendientesActivity::class.java)
            startActivity(intent)
        }

        btnVolver.setOnClickListener {
            finish()
        }

        fabNuevaCita.setOnClickListener {
            val intent = Intent(this, FormularioCitaManualActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarCitasAceptadasYDecorar() {
        try {
            listaCitas = citaController.obtenerCitasAceptadas()

            calendarView.removeDecorators()
            calendarView.invalidateDecorators()

            val diasConCitas = listaCitas.mapNotNull { cita ->
                try {
                    val parts = cita.fecha.split("/")
                    CalendarDay.from(
                        parts[2].toInt(),          // Año
                        parts[1].toInt() - 1,      // Mes (0-based en CalendarDay)
                        parts[0].toInt()           // Día
                    )
                } catch (e: Exception) {
                    null
                }
            }.toSet()

            calendarView.addDecorator(object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay): Boolean {
                    return diasConCitas.contains(day)
                }

                override fun decorate(view: DayViewFacade) {
                    view.addSpan(DotSpan(8f, getColor(R.color.rosaRoto)))
                }
            })

        } catch (e: Exception) {
            Log.e("CitasAdmin", "Error al cargar o decorar calendario", e)
        }
    }
}
