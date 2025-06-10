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

  /**
   * Adaptador para mostrar una lista de citas pendientes en un RecyclerView.
   *
   * @param citas Lista de citas pendientes.
   * @param mapaUsuarios Mapa que relaciona los IDs de usuarios con sus datos (id -> Usuario).
   * @param onAceptar Callback que se ejecuta al aceptar una cita.
   * @param onRechazar Callback que se ejecuta al rechazar una cita.
   */
  class CitaPendienteAdapter(
      private val citas: List<Cita>,
      private val mapaUsuarios: Map<String, Usuario>,
      private val onAceptar: (Cita) -> Unit,
      private val onRechazar: (Cita) -> Unit
  ) : RecyclerView.Adapter<CitaPendienteAdapter.ViewHolder>() {

      /**
       * ViewHolder que representa las vistas de cada ítem de cita pendiente.
       *
       * @param view Vista correspondiente al ítem.
       */
      inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
          val tvNombreCliente: TextView = view.findViewById(R.id.tvNombreCliente) // Nombre del cliente.
          val tvNumeroTelefono: TextView = view.findViewById(R.id.tvNumeroTelefono) // Número de teléfono del cliente.
          val tvFechaHora: TextView = view.findViewById(R.id.tvFechaHora) // Fecha y hora de la cita.
          val tvTipoServicio: TextView = view.findViewById(R.id.tvTipoServicio) // Tipo de servicio solicitado.
          val btnAceptar: Button = view.findViewById(R.id.btnAceptarCita) // Botón para aceptar la cita.
          val btnRechazar: Button = view.findViewById(R.id.btnRechazarCita) // Botón para rechazar la cita.
      }

      /**
       * Crea un nuevo ViewHolder inflando el diseño del ítem.
       *
       * @param parent Vista padre donde se añadirá el ViewHolder.
       * @param viewType Tipo de vista (no utilizado en este caso).
       * @return Una nueva instancia de ViewHolder.
       */
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
          val vista = LayoutInflater.from(parent.context)
              .inflate(R.layout.item_cita_pendiente, parent, false)
          return ViewHolder(vista)
      }

      /**
       * Devuelve el número total de ítems en la lista.
       *
       * @return Cantidad de citas pendientes en la lista.
       */
      override fun getItemCount(): Int = citas.size

      /**
       * Vincula los datos de una cita pendiente a las vistas del ViewHolder.
       *
       * @param holder ViewHolder que se actualizará con los datos de la cita.
       * @param position Posición del ítem en la lista.
       */
      override fun onBindViewHolder(holder: ViewHolder, position: Int) {
          val cita = citas[position]
          val usuario = mapaUsuarios[cita.idUsuario]

          // Asigna los datos del cliente y la cita a las vistas correspondientes.
          holder.tvNombreCliente.text = "${usuario?.nombre} ${usuario?.apellido}"
          holder.tvNumeroTelefono.text = usuario?.telefono ?: "Sin número"
          holder.tvFechaHora.text = "${cita.fecha} - ${cita.hora}"
          holder.tvTipoServicio.text = cita.tipoServicio

          // Configura los listeners para los botones de aceptar y rechazar.
          holder.btnAceptar.setOnClickListener { onAceptar(cita) }
          holder.btnRechazar.setOnClickListener { onRechazar(cita) }
      }
  }