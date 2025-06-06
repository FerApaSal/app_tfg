package com.example.tfg_app_makeup.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Utilidad para solicitar permisos en tiempo de ejecución de forma centralizada.
 */
object PermissionHelper {

    const val REQUEST_GALLERY_PERMISSION = 101

    /**
     * Verifica si el permiso de acceso a la galería ya ha sido concedido.
     */
    fun tienePermisoGaleria(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Solicita el permiso correspondiente según la versión de Android.
     */
    fun solicitarPermisoGaleria(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_GALLERY_PERMISSION
            )
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_GALLERY_PERMISSION
            )
        }
    }

    /**
     * Muestra un mensaje si se deniega el permiso.
     */
    fun manejarPermisoDenegado(activity: Activity) {
        Toast.makeText(activity, "No se concedió acceso a la galería", Toast.LENGTH_SHORT).show()
    }
}
