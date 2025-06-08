package com.example.tfg_app_makeup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Cita
import com.example.tfg_app_makeup.model.Usuario

class CitaPendienteAdapter(
    private val citas: List<Cita>,
    private val mapaUsuarios: Map<String, Usuario>,
    private val onAceptar: (Cita) -> Unit,
    private val onRechazar: (Cita) -> Unit
) : RecyclerView.Adapter<CitaPendienteAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreCliente: TextView = view.findViewById(R.id.tvNombreCliente)
        val tvNumeroTelefono: TextView = view.findViewById(R.id.tvNumeroTelefono)
        val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHora)
        val tvTipoServicio: TextView = view.findViewById(R.id.tvTipoServicio)
        val btnAceptar: Button = view.findViewById(R.id.btnAceptarCita)
        val btnRechazar: Button = view.findViewById(R.id.btnRechazarCita)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cita_pendiente, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int = citas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cita = citas[position]
        val usuario = mapaUsuarios[cita.idUsuario]

        holder.tvNombreCliente.text = "${usuario?.nombre} ${usuario?.apellido}"
        holder.tvNumeroTelefono.text = usuario?.telefono ?: "Sin número"
        holder.tvFechaHora.text = "${cita.fecha} - ${cita.hora}"
        holder.tvTipoServicio.text = cita.tipoServicio

        holder.btnAceptar.setOnClickListener { onAceptar(cita) }
        holder.btnRechazar.setOnClickListener { onRechazar(cita) }
    }
}
