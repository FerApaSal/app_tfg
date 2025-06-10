package com.example.tfg_app_makeup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.model.Usuario

/**
 * Adapter para mostrar citas aceptadas en la vista de la maquilladora.
 *
 * @param citas Lista de citas confirmadas o aceptadas.
 * @param mapaUsuarios Mapa de usuarios registrados (id -> Usuario) para mostrar el nombre si está disponible.
 */
class CitaAdminAdapter(
    private val citas: List<Cita>,
    private val mapaUsuarios: Map<String, Usuario>
) : RecyclerView.Adapter<CitaAdminAdapter.CitaViewHolder>() {

    /**
     * ViewHolder que contiene las vistas de cada tarjeta de cita.
     */
    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCliente: TextView = itemView.findViewById(R.id.tvNombreCliente)
        val tvHoraCita: TextView = itemView.findViewById(R.id.tvHoraCita)
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccionCita)
        val tvTipoServicio: TextView = itemView.findViewById(R.id.tvTipoServicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_admin, parent, false)
        return CitaViewHolder(vista)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        // Mostrar nombre del cliente según prioridad: usuario registrado > nombre manual > desconocido
        holder.tvNombreCliente.text = obtenerNombreCliente(cita)

        holder.tvHoraCita.text = cita.hora
        holder.tvDireccion.text = cita.direccion
        holder.tvTipoServicio.text = cita.tipoServicio

        // Asignar color de fondo según el tipo de servicio
        val contexto = holder.itemView.context
        val colorFondo = when (cita.tipoServicio) {
            "Novia" -> R.color.rosaNovia
            "Madrina" -> R.color.lavandaMadrina
            "Invitada" -> R.color.verdeInvitada
            "Madre del novia/a" -> R.color.coralMadreNovio
            "Comunión" -> R.color.azulComunion
            "Bautizo" -> R.color.amarilloBautizo
            else -> android.R.color.darker_gray
        }

        holder.itemView.setBackgroundColor(ContextCompat.getColor(contexto, colorFondo))
    }

    override fun getItemCount(): Int = citas.size

    /**
     * Obtiene el nombre del cliente asociado a una cita.
     * Si hay un usuario registrado, se muestra su nombre.
     * Si no, se muestra el nombre manual (si existe).
     * Si no hay ninguno, se muestra "Cliente desconocido".
     */
    private fun obtenerNombreCliente(cita: Cita): String {
        val usuario = mapaUsuarios[cita.idUsuario]
        return when {
            usuario != null -> usuario.nombre
            !cita.nombreClienteManual.isNullOrBlank() -> cita.nombreClienteManual!!
            else -> "Cliente desconocido"
        }
    }
}
