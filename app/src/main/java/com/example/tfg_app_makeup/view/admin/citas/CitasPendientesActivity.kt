package com.example.tfg_app_makeup.view.admin.citas

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapter.CitaPendienteAdapter
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.model.Usuario

/**
 * Muestra la lista de citas pendientes y permite aceptarlas o rechazarlas.
 */
class CitasPendientesActivity : AppCompatActivity() {

    private lateinit var rvCitas: RecyclerView
    private lateinit var btnVolver: ImageButton

    private lateinit var citaController: CitaController
    private lateinit var usuarioController: UsuarioController
    private lateinit var citaAdapter: CitaPendienteAdapter

    private var listaCitas = mutableListOf<Cita>()
    private var mapaUsuarios = emptyMap<String, Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas_pendientes)

        citaController = CitaController(this)
        usuarioController = UsuarioController(this)

        inicializarComponentes()
        configurarListeners()
        cargarCitasPendientes()
    }

    /**
     * Enlaza variables con elementos del layout.
     */
    private fun inicializarComponentes() {
        rvCitas = findViewById(R.id.rvCitasPendientes)
        btnVolver = findViewById(R.id.btnVolverPendientes)
        rvCitas.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Asigna comportamiento al botón de retroceso.
     */
    private fun configurarListeners() {
        btnVolver.setOnClickListener { finish() }
    }

    /**
     * Carga las citas con estado PENDIENTE y las muestra en pantalla.
     */
    private fun cargarCitasPendientes() {
        try {
            listaCitas = citaController.obtenerPorEstado("PENDIENTE").toMutableList()
            mapaUsuarios = usuarioController.obtenerTodos().associateBy { it.id }

            citaAdapter = CitaPendienteAdapter(
                citas = listaCitas,
                mapaUsuarios = mapaUsuarios,
                onAceptar = { cita ->
                    cita.estado = "ACEPTADA"
                    if (citaController.actualizar(cita)) {
                        Toast.makeText(this, "Cita aceptada", Toast.LENGTH_SHORT).show()
                        cargarCitasPendientes()
                    } else {
                        Toast.makeText(this, "Error al aceptar", Toast.LENGTH_SHORT).show()
                    }
                },
                onRechazar = { cita ->
                    CitaHelper.confirmarRechazoCita(this, cita, citaController) {
                        cargarCitasPendientes()
                    }
                }
            )

            rvCitas.adapter = citaAdapter

        } catch (e: Exception) {
            Log.e("CitasPendientesActivity", "Error al cargar citas", e)
            Toast.makeText(this, "Error al cargar citas", Toast.LENGTH_SHORT).show()
        }
    }
}