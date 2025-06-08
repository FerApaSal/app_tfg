package com.example.tfg_app_makeup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Usuario

/**
 * Adaptador para mostrar tarjetas con información de cada cliente registrado.
 */
class ListarClientesAdapter(private val listaClientes: List<Usuario>) :
    RecyclerView.Adapter<ListarClientesAdapter.ClienteViewHolder>() {

    inner class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreApellido: TextView = view.findViewById(R.id.tvNombreCliente)
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreoCliente)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]
        holder.tvNombreApellido.text = "${cliente.nombre} ${cliente.apellido}"
        holder.tvCorreo.text = cliente.correo
        holder.tvTelefono.text = cliente.telefono
    }

    override fun getItemCount(): Int = listaClientes.size
}
