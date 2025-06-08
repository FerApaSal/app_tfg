package com.example.tfg_app_makeup.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Tarea

class TareaAdapter(
    private val context: Context,
    private val tareas: List<Tarea>,
    private val onEditar: (Tarea) -> Unit,
    private val onEliminar: (Tarea) -> Unit,
    private val onMarcarCompletada: (Tarea, Boolean) -> Unit
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    inner class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloTarea)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcionTarea)
        val tvPrioridad: TextView = itemView.findViewById(R.id.tvPrioridad)
        val cbCompletada: CheckBox = itemView.findViewById(R.id.cbCompletada)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditarTarea)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminarTarea)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]

        holder.tvTitulo.text = tarea.titulo
        holder.tvDescripcion.text = tarea.descripcion
        holder.cbCompletada.isChecked = tarea.completada
        holder.tvPrioridad.text = tarea.prioridad

        // Color según prioridad
        val color = when (tarea.prioridad.lowercase()) {
            "alta" -> R.color.naranjaPrioridad
            "media" -> R.color.amarilloPrioridad
            "baja" -> R.color.verdePrioridad
            "urgente" -> R.color.rojoPrioridad
            else -> R.color.black
        }
        holder.tvPrioridad.setTextColor(context.getColor(color))

        // Acciones
        holder.cbCompletada.setOnCheckedChangeListener(null)
        holder.cbCompletada.isChecked = tarea.completada
        holder.cbCompletada.setOnCheckedChangeListener { _, isChecked ->
            onMarcarCompletada(tarea, isChecked)
        }

        holder.btnEditar.setOnClickListener {
            onEditar(tarea)
        }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Sí") { _, _ ->
                    onEliminar(tarea)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun getItemCount(): Int = tareas.size
}
