package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.IOException

object AssetHelper {

    fun cargarImagenDesdeAssets(context: Context, nombreArchivo: String): Drawable? {
        return try {
            val inputStream = context.assets.open(nombreArchivo)
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            Log.e("AssetHelper", "No se pudo cargar la imagen desde assets: $nombreArchivo", e)
            null
        }
    }
}