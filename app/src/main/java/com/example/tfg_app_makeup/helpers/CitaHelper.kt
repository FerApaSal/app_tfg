package com.example.tfg_app_makeup.helpers

import android.util.Log
import android.widget.Toast
import com.example.tfg_app_makeup.model.Cita
import java.util.UUID

object CitaHelper {

    fun validar(cita: Cita): Boolean {
        if (cita.tipoServicio.isBlank()) {
            Log.w("CitaHelper", "Tipo de servicio vacío.")
            return false
        }
        if (!Regex("\\d{2}/\\d{2}/\\d{4}").matches(cita.fecha)) {
            Log.w("CitaHelper", "Fecha inválida: ${cita.fecha}")
            return false
        }
        if (!Regex("\\d{2}:\\d{2}").matches(cita.hora)) {
            Log.w("CitaHelper", "Hora inválida: ${cita.hora}")
            return false
        }
        if (cita.direccion.isBlank()) {
            Log.w("CitaHelper", "Dirección vacía.")
            return false
        }
        if (cita.idUsuario.isBlank()) {
            Log.w("CitaHelper", "ID de usuario no especificado.")
            return false
        }
        return true
    }
}
