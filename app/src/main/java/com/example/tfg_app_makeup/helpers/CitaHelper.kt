package com.example.tfg_app_makeup.helpers

import android.content.Context
import android.util.Log
import android.widget.ImageView
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

object CitaHelper {

    /**
     * Valida los campos obligatorios de una cita.
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
     * Valida si una fecha escrita en formato dd/MM/yyyy es válida.
     * Muestra un Toast si no lo es.
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
     * Aplica un DotSpan en color rosaRoto.
     */
    fun decorarFechasConCitas(context: Context, calendarView: MaterialCalendarView, citas: List<Cita>) {
        try {
            calendarView.removeDecorators()
            calendarView.invalidateDecorators()

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
     * Valida si la fecha recibida desde un intent es válida.
     * Si la fecha es nula o está vacía, muestra un Toast y devuelve false.
     *
     * @param context Contexto de la actividad que llama.
     * @param fecha Cadena con la fecha en formato dd/MM/yyyy.
     * @return true si la fecha es válida, false si es nula o vacía.
     */
    fun validarFechaDesdeIntent(context: Context, fecha: String?): Boolean {
        return if (fecha.isNullOrBlank()) {
            Toast.makeText(context, "Fecha no válida", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    /**
     * Muestra un diálogo de confirmación para rechazar una cita.
     * Si se confirma, actualiza su estado a RECHAZADA y ejecuta el callback.
     *
     * @param context Contexto desde donde se lanza el diálogo.
     * @param cita Objeto Cita a modificar.
     * @param citaController Controlador para actualizar la cita.
     * @param onRecargar Callback a ejecutar si la operación es exitosa.
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
     * Valida que todos los campos requeridos del formulario estén completos.
     * Muestra un Toast si alguno está vacío.
     *
     * @param context Contexto donde se mostrará el mensaje.
     * @param campos Lista variable de cadenas a comprobar.
     * @return true si alguno está vacío, false si todos tienen contenido.
     */
    fun hayCamposVacios(context: Context, vararg campos: String): Boolean {
        return if (campos.any { it.isBlank() }) {
            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            true
        } else false
    }

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
