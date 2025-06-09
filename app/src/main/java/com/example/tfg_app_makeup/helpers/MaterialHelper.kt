package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tfg_app_makeup.controllers.MaterialController
import com.example.tfg_app_makeup.model.Material
import java.io.File

/**
 * Helper para validaciones y utilidades relacionadas con materiales.
 */
object MaterialHelper {

    /**
     * Verifica si hay campos vacíos entre nombre y cantidad.
     */
    fun hayCamposVacios(nombre: String, cantidad: String): Boolean {
        return nombre.isEmpty() || cantidad.isEmpty()
    }

    /**
     * Verifica si la cantidad es un número entero válido y mayor o igual a 0.
     */
    fun esCantidadValida(cantidad: String): Boolean {
        return try {
            val valor = cantidad.toInt()
            valor >= 0
        } catch (e: NumberFormatException) {
            false
        }
    }

    /**
     * Guarda una imagen seleccionada desde la galería en el almacenamiento interno de la aplicación.
     *
     * @param context El contexto de la aplicación.
     * @param uri La URI de la imagen seleccionada desde la galería.
     * @return La ruta absoluta del archivo guardado o `null` si ocurre un error.
     */

    fun guardarImagenDesdeGaleria(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)

            val extension = context.contentResolver.getType(uri)?.let {
                when {
                    it.contains("png") -> ".png"
                    it.contains("jpeg") || it.contains("jpg") -> ".jpg"
                    else -> ".img"
                }
            } ?: ".img"

            val timestamp = System.currentTimeMillis()
            val file = File(context.filesDir, "material_$timestamp$extension")

            file.outputStream().use { output ->
                inputStream?.copyTo(output)
            }

            file.absolutePath
        } catch (e: Exception) {
            Log.e("MaterialHelper", "Error al guardar imagen", e)
            null
        }
    }

    /**
     * Muestra un diálogo de confirmación para eliminar un material.
     * Si se confirma, lo elimina y ejecuta el callback de recarga.
     *
     * @param context Contexto desde donde se lanza el diálogo.
     * @param material Material que se desea eliminar.
     * @param controller Controlador que gestiona la operación.
     * @param onRecargar Callback que se ejecuta si se elimina correctamente.
     */
    fun confirmarEliminacion(
        context: Context,
        material: Material,
        controller: MaterialController,
        onRecargar: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Eliminar material")
            .setMessage("¿Estás seguro de que deseas eliminar este material?")
            .setPositiveButton("Sí") { _, _ ->
                if (controller.eliminar(material.id)) {
                    Toast.makeText(context, "Material eliminado", Toast.LENGTH_SHORT).show()
                    onRecargar()
                } else {
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}
