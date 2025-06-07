package com.example.tfg_app_makeup.view.client

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.utils.Session
import com.example.tfg_app_makeup.adapter.CitaAdapter

/**
 * Activity que muestra las citas del cliente actual.
 */
class MisCitasActivity : AppCompatActivity() {

    private lateinit var rvCitasCliente: RecyclerView
    private lateinit var tvSinCitas: TextView
    private lateinit var btnVolver: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas)

        rvCitasCliente = findViewById(R.id.rvCitasCliente)
        tvSinCitas = findViewById(R.id.tvSinCitas)
        btnVolver = findViewById(R.id.btnVolverMisCitas)

        val idUsuario = Session.usuarioActual?.id

        try {
            if (idUsuario != null) {
                val citas = CitaController(this).obtenerCitasPorUsuario(idUsuario)

                if (citas.isEmpty()) {
                    rvCitasCliente.visibility = View.GONE
                    tvSinCitas.visibility = View.VISIBLE
                } else {
                    rvCitasCliente.layoutManager = LinearLayoutManager(this)
                    rvCitasCliente.adapter = CitaAdapter(citas)
                    rvCitasCliente.visibility = View.VISIBLE
                    tvSinCitas.visibility = View.GONE
                }

                Log.d("MisCitasActivity", "Citas cargadas: ${citas.size}")
            } else {
                Log.e("MisCitasActivity", "ID de usuario nulo en sesión")
            }

        } catch (e: Exception) {
            Log.e("MisCitasActivity", "Error al cargar las citas", e)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}
