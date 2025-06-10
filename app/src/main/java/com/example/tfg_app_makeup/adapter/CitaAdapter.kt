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
 * Adaptador para gestionar y mostrar una lista de citas en un RecyclerView.
 *
 * @param citas Lista de objetos de tipo Cita que se mostrarán.
 */
class CitaAdapter(
    private var citas: List<Cita>,
) : RecyclerView.Adapter<CitaAdapter.CitaViewHolder>() {

    /**
     * ViewHolder que contiene las vistas de un ítem de la lista de citas.
     *
     * @param itemView Vista correspondiente al ítem.
     */
    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTipoServicio: TextView = itemView.findViewById(R.id.tvTipoServicio) // Muestra el tipo de servicio.
        val tvFechaHora: TextView = itemView.findViewById(R.id.tvFechaHora) // Muestra la fecha y hora de la cita.
        val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccion) // Muestra la dirección de la cita.
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado) // Muestra el estado de la cita.
    }

    /**
     * Crea un nuevo ViewHolder inflando el diseño del ítem.
     *
     * @param parent Vista padre donde se añadirá el ViewHolder.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de CitaViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_cliente, parent, false)
        return CitaViewHolder(view)
    }

    /**
     * Vincula los datos de una cita a las vistas del ViewHolder.
     *
     * @param holder ViewHolder que se actualizará con los datos de la cita.
     * @param position Posición del ítem en la lista.
     */
    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citas[position]

        // Asigna los datos de la cita a las vistas correspondientes.
        holder.tvTipoServicio.text = cita.tipoServicio
        holder.tvFechaHora.text = "${cita.fecha} a las ${cita.hora}"
        holder.tvDireccion.text = cita.direccion
        holder.tvEstado.text = cita.estado.uppercase()

        // Cambia el color del texto del estado según su valor.
        when (cita.estado.lowercase()) {
            "pendiente" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorPendiente))
            "confirmada" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorConfirmada))
            "cancelada" -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorCancelada))
            else -> holder.tvEstado.setTextColor(holder.itemView.context.getColor(R.color.colorDefault))
        }
    }

    /**
     * Devuelve el número total de ítems en la lista.
     *
     * @return Cantidad de citas en la lista.
     */
    override fun getItemCount(): Int = citas.size
}