package com.example.tfg_app_makeup.view.admin.citas

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapter.CitaAdminAdapter
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.model.Usuario

/**
 * Muestra las citas aceptadas de un día específico para el perfil de administradora.
 */
class CitasDiaActivity : AppCompatActivity() {

    private lateinit var rvCitas: RecyclerView
    private lateinit var tvFecha: TextView
    private lateinit var btnVolver: ImageButton

    private lateinit var citaController: CitaController
    private lateinit var citaAdapter: CitaAdminAdapter
    private lateinit var usuariosPorId: Map<String, Usuario>

    private var listaCitas = mutableListOf<Cita>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_citas_del_dia)

        citaController = CitaController(this)
        usuariosPorId = UsuarioController(this).obtenerUsuariosMapeados()

        inicializarComponentes()
        configurarListeners()
        cargarCitasDelDia()
    }

    private fun inicializarComponentes() {
        rvCitas = findViewById(R.id.rvCitasDia)
        tvFecha = findViewById(R.id.tvFechaSeleccionada)
        btnVolver = findViewById(R.id.btnVolverCitasDia)

        rvCitas.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarListeners() {
        btnVolver.setOnClickListener { finish() }
    }

    private fun cargarCitasDelDia() {
        val fecha = intent.getStringExtra("fechaSeleccionada")

        if (fecha.isNullOrBlank()) {
            Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvFecha.text = "Citas del $fecha"

        try {
            listaCitas = citaController
                .obtenerPorFechaYEstado(fecha, "ACEPTADA")
                .sortedBy { it.hora }
                .toMutableList()

            if (listaCitas.isEmpty()) {
                Toast.makeText(this, "No hay citas aceptadas para esta fecha", Toast.LENGTH_SHORT).show()
            }

            citaAdapter = CitaAdminAdapter(listaCitas, usuariosPorId)
            rvCitas.adapter = citaAdapter

        } catch (e: Exception) {
            Log.e("CitasDiaActivity", "Error al cargar citas", e)
            Toast.makeText(this, "Error al cargar citas", Toast.LENGTH_SHORT).show()
        }
    }
}
