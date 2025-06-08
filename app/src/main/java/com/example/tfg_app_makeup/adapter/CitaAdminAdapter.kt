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
 */
class CitaAdminAdapter(
    private val citas: List<Cita>,
    private val mapaUsuarios: Map<String, Usuario>
) : RecyclerView.Adapter<CitaAdminAdapter.CitaViewHolder>() {

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
        val usuario = mapaUsuarios[cita.idUsuario]

        holder.tvNombreCliente.text = usuario?.nombre ?: "Cliente desconocido"
        holder.tvHoraCita.text = cita.hora
        holder.tvDireccion.text = cita.direccion
        holder.tvTipoServicio.text = cita.tipoServicio

        // Color según tipo de servicio
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
}
