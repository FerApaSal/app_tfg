package com.example.tfg_app_makeup.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Cita
import java.util.Locale

/**
 * Adapter para mostrar una lista de citas del cliente.
 *
 * @param citas Lista de citas extendidas con datos del usuario asociados.
 */
class CitaAdapter(
    private var citas: List<Cita>,
) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    /**
     * ViewHolder que representa un ítem de cita.
     */
    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipoServicio: TextView = itemView.findViewById(R.id.tvTipoServicio)
        val tvFechaHora: TextView = itemView.findViewById(R.id.tvFechaHora)
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccion)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_cliente, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        holder.tvTipoServicio.text = cita.tipoServicio
        holder.tvFechaHora.text = "${cita.fecha} a las ${cita.hora}"
        holder.tvDireccion.text = cita.direccion
        holder.tvEstado.text = cita.estado.uppercase()

        // Cambia el color del estado si es necesario
        // Ejemplo: si el estado es "pendiente", podrá usar un color específico

        when (cita.estado.lowercase()) {
            "pendiente" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorPendiente))
            "confirmada" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorConfirmada))
            "cancelada" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorCancelada))
            else -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorDefault))
        }
    }



    override fun getItemCount(): Int = citas.size
}
