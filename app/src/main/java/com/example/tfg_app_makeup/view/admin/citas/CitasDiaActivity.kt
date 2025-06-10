package com.example.tfg_app_makeup.view.admin.citas

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.adapter.CitaAdminAdapter
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.helpers.CitaHelper
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity

/**
 * Muestra las citas aceptadas para una fecha específica.
 * Los datos se cargan desde la base local y se ordenan por hora ascendente.
 */
class CitasDiaActivity : BaseDrawerActivity() {

    private lateinit var rvCitas: RecyclerView
    private lateinit var btnVolver: ImageButton
    private lateinit var tvTitulo: TextView

    private lateinit var citaController: CitaController
    private lateinit var usuarioController: UsuarioController
    private lateinit var citaAdapter: CitaAdminAdapter

    private var listaCitas: List<Cita> = emptyList()
    private var mapaUsuarios: Map<String, Usuario> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_drawer)

        // Infla el contenido específico de esta actividad dentro del layout base
        val contenido = layoutInflater.inflate(R.layout.activity_citas_del_dia, findViewById(R.id.contenidoPrincipal))

        // Inicializa los componentes de la interfaz gráfica
        rvCitas = contenido.findViewById(R.id.rvCitasDia)
        btnVolver = contenido.findViewById(R.id.btnVolverCitasDia)
        tvTitulo = contenido.findViewById(R.id.tvTituloCitasDia)

        rvCitas.layoutManager = LinearLayoutManager(this)

        // Inicializa los controladores
        citaController = CitaController(this)
        usuarioController = UsuarioController(this)

        configurarListeners()
        cargarDatos() // Carga y muestra las citas del día
        configurarMenuHamburguesa() // Configura el menú lateral (heredado de BaseDrawerActivity)
    }

    /**
     * Carga y muestra las citas del día con los datos del cliente asociados.
     */
    private fun cargarDatos() {
        val fechaSeleccionada = intent.getStringExtra("fechaSeleccionada") ?: return
        tvTitulo.text = "Citas del $fechaSeleccionada"

        // Obtener usuarios CLIENTE mapeados
        mapaUsuarios = usuarioController.obtenerUsuariosMapeados()

        // Obtener y filtrar citas aceptadas para la fecha seleccionada
        listaCitas = citaController.obtenerCitasAceptadas().filter { it.fecha == fechaSeleccionada }

        // Ordenar por hora ascendente
        listaCitas = listaCitas.sortedBy { CitaHelper.convertirHoraAHoraEntera(it.hora) }

        // Registrar posibles inconsistencias
        for (cita in listaCitas) {
            if (mapaUsuarios[cita.idUsuario] == null) {
                Log.w("CitasDiaActivity", "Usuario no encontrado para la cita con ID: ${cita.id}")
            }
        }

        // Cargar adaptador con las citas y los usuarios asociados
        citaAdapter = CitaAdminAdapter(listaCitas, mapaUsuarios)
        rvCitas.adapter = citaAdapter
    }

    private fun configurarListeners() {
        // Listener para el botón de volver
        btnVolver.setOnClickListener {
            finish()
        }
    }
}