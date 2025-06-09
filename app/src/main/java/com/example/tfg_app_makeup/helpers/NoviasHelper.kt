package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import java.io.File
import java.io.IOException

object NoviasHelper {

    private const val NOMBRE_ARCHIVO_NOVIAS = "novias.jpg"

    /**
     * Guarda la imagen seleccionada desde la galería como archivo local.
     * Devuelve el archivo guardado o null si hay error.
     */
    fun guardarImagenInformativa(context: Context, uri: Uri): File? {
        return try {
            val archivoDestino = File(context.filesDir, NOMBRE_ARCHIVO_NOVIAS)
            val inputStream = context.contentResolver.openInputStream(uri)
            archivoDestino.outputStream().use { output ->
                inputStream?.copyTo(output)
            }
            archivoDestino
        } catch (e: Exception) {
            Log.e("NoviasHelper", "Error al guardar imagen", e)
            null
        }
    }

    /**
     * Carga la imagen informativa si existe. Si no, muestra un placeholder.
     */
    fun cargarImagenInformativa(context: Context, imageView: ImageView) {
        val archivo = File(context.filesDir, NOMBRE_ARCHIVO_NOVIAS)
        if (archivo.exists()) {
            Glide.with(context)
                .load(archivo)
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_user_placeholder)
        }
    }

    /**
     * Carga una imagen desde la carpeta de assets del proyecto.
     *
     * @param context El contexto de la aplicación.
     * @param nombreArchivo El nombre del archivo de imagen dentro de la carpeta de assets.
     * @return Un objeto `Drawable` que representa la imagen cargada, o `null` si ocurre un error.
     */
    fun cargarImagenDesdeAssets(context: Context, nombreArchivo: String): Drawable? {
        return try {
            val inputStream = context.assets.open(nombreArchivo)
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("NoviasHelper", "No se pudo cargar la imagen desde assets: $nombreArchivo", e)
            null
        }
    }
}