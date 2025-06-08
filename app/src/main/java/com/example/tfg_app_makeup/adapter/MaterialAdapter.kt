package com.example.tfg_app_makeup.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.model.Material
import java.io.File

/**
 * Adaptador para mostrar los materiales en tarjetas del RecyclerView.
 */
class MaterialAdapter(
    private val context: Context,
    private val materiales: List<Material>,
    private val onEditar: (Material) -> Unit,
    private val onEliminar: (Material) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImagen: ImageView = view.findViewById(R.id.ivImagenMaterial)
        val tvNombre: TextView = view.findViewById(R.id.tvNombreMaterial)
        val tvTipo: TextView = view.findViewById(R.id.tvTipoMaterial)
        val tvCantidad: TextView = view.findViewById(R.id.tvCantidadMaterial)
        val tvEstado: TextView = view.findViewById(R.id.tvEstadoMaterial)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditarMaterial)
        val btnEliminar: ImageButton = view.findViewById(R.id.btnEliminarMaterial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_material, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = materiales[position]

        holder.tvNombre.text = material.nombre
        holder.tvTipo.text = "Tipo: ${material.tipo}"
        holder.tvCantidad.text = "Cantidad: ${material.cantidad}"
        holder.tvEstado.text = "Estado: ${material.estado}"

        if (!material.imagenUrl.isNullOrBlank()) {
            Glide.with(context)
                .load(File(material.imagenUrl!!))
                .placeholder(R.drawable.ic_user_placeholder)
                .error(R.drawable.ic_user_placeholder)
                .into(holder.ivImagen)
        } else {
            holder.ivImagen.setImageResource(R.drawable.ic_user_placeholder)
        }

        holder.btnEditar.setOnClickListener { onEditar(material) }

        holder.btnEliminar.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Eliminar material")
                .setMessage("¿Deseas eliminar este material?")
                .setPositiveButton("Sí") { _, _ -> onEliminar(material) }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }

    override fun getItemCount(): Int = materiales.size
}
