package com.example.tfg_app_makeup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Usuario

/**
 * Adaptador para mostrar una lista de clientes registrados en un RecyclerView.
 *
 * @param listaClientes Lista de objetos de tipo Usuario que representan los clientes registrados.
 */
class ListarClientesAdapter(private val listaClientes: List<Usuario>) :
    RecyclerView.Adapter<ListarClientesAdapter.ClienteViewHolder>() {

    /**
     * ViewHolder que contiene las vistas de cada tarjeta de cliente.
     *
     * @param view Vista correspondiente al ítem.
     */
    inner class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreApellido: TextView = view.findViewById(R.id.tvNombreCliente) // Nombre y apellido del cliente.
        val tvCorreo: TextView = view.findViewById(R.id.tvCorreoCliente) // Correo electrónico del cliente.
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoCliente) // Teléfono del cliente.
    }

    /**
     * Crea un nuevo ViewHolder inflando el diseño del ítem.
     *
     * @param parent Vista padre donde se añadirá el ViewHolder.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de ClienteViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    /**
     * Vincula los datos de un cliente a las vistas del ViewHolder.
     *
     * @param holder ViewHolder que se actualizará con los datos del cliente.
     * @param position Posición del ítem en la lista.
     */
    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]

        // Asigna los datos del cliente a las vistas correspondientes.
        holder.tvNombreApellido.text = "${cliente.nombre} ${cliente.apellido}"
        holder.tvCorreo.text = cliente.correo
        holder.tvTelefono.text = cliente.telefono
    }

    /**
     * Devuelve el número total de ítems en la lista.
     *
     * @return Cantidad de clientes en la lista.
     */
    override fun getItemCount(): Int = listaClientes.size
}