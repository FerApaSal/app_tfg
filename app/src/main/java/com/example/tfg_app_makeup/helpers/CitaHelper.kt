package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.controllers.CitaController
import com.example.tfg_app_makeup.model.Cita
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan

/**
 * Helper para gestionar operaciones relacionadas con las citas.
 */
object CitaHelper {

    /**
     * Valida los campos obligatorios de una cita.
     * @param cita Objeto de tipo Cita a validar.
     * @return true si todos los campos son válidos, false en caso contrario.
     */
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

    /**
     * Valida si una fecha tiene el formato dd/MM/yyyy.
     * @param context Contexto para mostrar mensajes.
     * @param fecha Fecha a validar.
     * @return true si el formato es válido, false en caso contrario.
     */
    fun validarFormatoFecha(context: Context, fecha: String): Boolean {
        val formatoEsperado = Regex("""\d{2}/\d{2}/\d{4}""")
        return if (!formatoEsperado.matches(fecha)) {
            Toast.makeText(context, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    /**
     * Decora un calendario con puntos en las fechas con citas aceptadas.
     * @param context Contexto para obtener colores.
     * @param calendarView Vista del calendario a decorar.
     * @param citas Lista de citas para decorar las fechas.
     */
    fun decorarFechasConCitas(context: Context, calendarView: MaterialCalendarView, citas: List<Cita>) {
        try {
            // Eliminar decoraciones previas
            calendarView.removeDecorators()
            calendarView.invalidateDecorators()

            // Convertir fechas de las citas a objetos CalendarDay
            val fechasDecoradas = citas.mapNotNull { cita ->
                try {
                    val partes = cita.fecha.split("/")
                    CalendarDay.from(
                        partes[2].toInt(),
                        partes[1].toInt() - 1,
                        partes[0].toInt()
                    )
                } catch (e: Exception) {
                    null
                }
            }.toSet()

            // Agregar decorador con puntos en las fechas
            calendarView.addDecorator(object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay): Boolean {
                    return fechasDecoradas.contains(day)
                }

                override fun decorate(view: DayViewFacade) {
                    view.addSpan(DotSpan(8f, context.getColor(R.color.rosaRoto)))
                }
            })

        } catch (e: Exception) {
            Log.e("CitaHelper", "Error al decorar el calendario", e)
        }
    }

    /**
     * Muestra un diálogo de confirmación para rechazar una cita.
     * @param context Contexto para mostrar el diálogo.
     * @param cita Objeto Cita a modificar.
     * @param citaController Controlador para actualizar la cita.
     * @param onRecargar Callback a ejecutar tras rechazar la cita.
     */
    fun confirmarRechazoCita(
        context: Context,
        cita: Cita,
        citaController: CitaController,
        onRecargar: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Rechazar cita")
            .setMessage("¿Estás segura de que deseas rechazar esta cita?")
            .setPositiveButton("Sí") { _, _ ->
                cita.estado = "RECHAZADA"
                if (citaController.actualizar(cita)) {
                    Toast.makeText(context, "Cita rechazada", Toast.LENGTH_SHORT).show()
                    onRecargar()
                } else {
                    Toast.makeText(context, "Error al rechazar la cita", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Verifica si hay campos vacíos en un formulario.
     * @param context Contexto para mostrar mensajes.
     * @param campos Lista de campos a validar.
     * @return true si hay campos vacíos, false en caso contrario.
     */
    fun hayCamposVacios(context: Context, vararg campos: String): Boolean {
        return if (campos.any { it.isBlank() }) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            true
        } else false
    }

    /**
     * Convierte una hora en formato HH:mm a un entero (HHmm).
     * @param hora Hora en formato HH:mm.
     * @return Hora como entero o null si ocurre un error.
     */
    fun convertirHoraAHoraEntera(hora: String): Int? {
        return try {
            val partes = hora.split(":")
            val horas = partes[0].toInt()
            val minutos = partes[1].toInt()
            horas * 100 + minutos
        } catch (e: Exception) {
            Log.e("CitaHelper", "Error al convertir hora: $hora", e)
            null
        }
    }
}