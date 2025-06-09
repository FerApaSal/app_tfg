package com.example.tfg_app_makeup.helpers

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

object TareaHelper {

    /**
     * Valida que los campos título, descripción y prioridad no estén vacíos.
     */
    fun validarCampos(
        context: Context,
        titulo: String,
        descripcion: String,
        prioridadSeleccionada: String
    ): Boolean {
        return if (titulo.isBlank() || descripcion.isBlank() || prioridadSeleccionada == "Selecciona prioridad") {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            false
        } else true
    }


    /**
     * Muestra un diálogo de confirmación reutilizable para eliminar una tarea.
     */
    fun mostrarDialogoEliminar(
        context: Context,
        onConfirmar: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Sí") { _, _ -> onConfirmar() }
            .setNegativeButton("No", null)
            .show()
    }
}
