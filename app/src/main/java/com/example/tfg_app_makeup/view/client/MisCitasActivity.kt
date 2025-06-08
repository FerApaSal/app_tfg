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
import com.example.tfg_app_makeup.adapter.CitaAdapter
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.utils.Session

/**
 * Pantalla que muestra las citas asociadas al usuario cliente actualmente en sesión.
 */
class MisCitasActivity : AppCompatActivity() {

    private lateinit var rvCitasCliente: RecyclerView
    private lateinit var tvSinCitas: TextView
    private lateinit var btnVolver: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_citas)

        inicializarComponentes()
        cargarCitas()

        btnVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Enlaza los elementos visuales del layout con las variables de clase.
     */
    private fun inicializarComponentes() {
        rvCitasCliente = findViewById(R.id.rvCitasCliente)
        tvSinCitas = findViewById(R.id.tvSinCitas)
        btnVolver = findViewById(R.id.btnVolverMisCitas)
    }

    /**
     * Carga las citas del usuario logueado y actualiza la interfaz según el resultado.
     */
    private fun cargarCitas() {
        val idUsuario = Session.usuarioActual?.id

        try {
            if (idUsuario != null) {
                val citas = CitaController(this).obtenerCitasPorUsuario(idUsuario)

                if (citas.isEmpty()) {
                    mostrarMensajeSinCitas()
                } else {
                    mostrarListaDeCitas(citas)
                }

                Log.d("MisCitasActivity", "Citas cargadas: ${citas.size}")
            } else {
                Log.e("MisCitasActivity", "ID de usuario nulo en sesión")
            }
        } catch (e: Exception) {
            Log.e("MisCitasActivity", "Error al cargar las citas", e)
        }
    }

    /**
     * Muestra el mensaje de "sin citas" y oculta la lista.
     */
    private fun mostrarMensajeSinCitas() {
        rvCitasCliente.visibility = View.GONE
        tvSinCitas.visibility = View.VISIBLE
    }

    /**
     * Muestra la lista de citas en el RecyclerView.
     * @param citas lista de objetos Cita a mostrar.
     */
    private fun mostrarListaDeCitas(citas: List<Cita>) {
        rvCitasCliente.layoutManager = LinearLayoutManager(this)
        rvCitasCliente.adapter = CitaAdapter(citas)
        rvCitasCliente.visibility = View.VISIBLE
        tvSinCitas.visibility = View.GONE
    }
}
