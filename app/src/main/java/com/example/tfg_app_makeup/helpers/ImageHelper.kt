package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

/**
 * Utilidad para copiar imágenes seleccionadas desde la galería a almacenamiento interno.
 */
object ImageHelper {

    /**
     * Copia la imagen desde su URI al directorio interno de archivos.
     *
     * @return Ruta absoluta del archivo copiado, o null si falló.
     */
    fun copiarImagenInterna(context: Context, uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val nombreArchivo = "profile_${UUID.randomUUID()}.jpg"
            val archivoDestino = File(context.filesDir, nombreArchivo)

            val outputStream = FileOutputStream(archivoDestino)
            inputStream?.copyTo(outputStream)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            Log.d("ImageHelper", "Imagen copiada a: ${archivoDestino.absolutePath}")
            archivoDestino.absolutePath
        } catch (e: Exception) {
            Log.e("ImageHelper", "Error al copiar imagen: ${e.message}", e)
            null
        }
    }

    /**
     * Fuerza la indexación de una imagen para que aparezca en la galería del sistema.
     */
    fun indexarImagen(context: Context, archivo: File) {
        try {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(archivo.absolutePath),
                arrayOf("image/jpeg"),
                null
            )
            Log.d("ImageHelper", "Imagen indexada en MediaStore: ${archivo.name}")
        } catch (e: Exception) {
            Log.e("ImageHelper", "Error al indexar imagen: ${e.message}", e)
        }
    }
}
