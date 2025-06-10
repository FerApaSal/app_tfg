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
    * Adaptador para mostrar citas aceptadas en la vista de la maquilladora.
    *
    * @param citas Lista de citas confirmadas o aceptadas.
    * @param mapaUsuarios Mapa que relaciona los IDs de usuarios con sus datos (id -> Usuario).
    */
   class CitaAdminAdapter(
       private val citas: List<Cita>,
       private val mapaUsuarios: Map<String, Usuario>
   ) : RecyclerView.Adapter<CitaAdminAdapter.CitaViewHolder>() {

       /**
        * ViewHolder que representa las vistas de cada tarjeta de cita.
        *
        * @param itemView Vista correspondiente al ítem.
        */
       inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           val tvNombreCliente: TextView = itemView.findViewById(R.id.tvNombreCliente) // Nombre del cliente.
           val tvHoraCita: TextView = itemView.findViewById(R.id.tvHoraCita) // Hora de la cita.
           val tvDireccion: TextView = itemView.findViewById(R.id.tvDireccionCita) // Dirección de la cita.
           val tvTipoServicio: TextView = itemView.findViewById(R.id.tvTipoServicio) // Tipo de servicio.
       }

       /**
        * Crea un nuevo ViewHolder inflando el diseño del ítem.
        *
        * @param parent Vista padre donde se añadirá el ViewHolder.
        * @param viewType Tipo de vista (no utilizado en este caso).
        * @return Una nueva instancia de CitaViewHolder.
        */
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
           val vista = LayoutInflater.from(parent.context)
               .inflate(R.layout.item_cita_admin, parent, false)
           return CitaViewHolder(vista)
       }

       /**
        * Vincula los datos de una cita a las vistas del ViewHolder.
        *
        * @param holder ViewHolder que se actualizará con los datos de la cita.
        * @param position Posición del ítem en la lista.
        */
       override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
           val cita = citas[position]

           // Asigna el nombre del cliente según la prioridad: usuario registrado > nombre manual > desconocido.
           holder.tvNombreCliente.text = obtenerNombreCliente(cita)

           // Asigna los datos de la cita a las vistas correspondientes.
           holder.tvHoraCita.text = cita.hora
           holder.tvDireccion.text = cita.direccion
           holder.tvTipoServicio.text = cita.tipoServicio

           // Cambia el color de fondo según el tipo de servicio.
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

       /**
        * Devuelve el número total de ítems en la lista.
        *
        * @return Cantidad de citas en la lista.
        */
       override fun getItemCount(): Int = citas.size

       /**
        * Obtiene el nombre del cliente asociado a una cita.
        * Si hay un usuario registrado, se muestra su nombre.
        * Si no, se muestra el nombre manual (si existe).
        * Si no hay ninguno, se muestra "Cliente desconocido".
        *
        * @param cita Objeto de tipo Cita del cual se obtendrá el nombre del cliente.
        * @return Nombre del cliente asociado a la cita.
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