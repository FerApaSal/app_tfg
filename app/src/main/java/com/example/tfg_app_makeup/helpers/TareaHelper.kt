package com.example.tfg_app_makeup.helpers

import android.util.Log

object TareaHelper {

    /**
     * Valida que los campos de la tarea no estén vacíos.
     */
    fun validarCampos(titulo: String, descripcion: String, prioridad: String): Boolean {
        if (titulo.isBlank()) {
            Log.w("TareaHelper", "El título está vacío.")
            return false
        }

        if (descripcion.isBlank()) {
            Log.w("TareaHelper", "La descripción está vacía.")
            return false
        }

        if (prioridad.isBlank()) {
            Log.w("TareaHelper", "La prioridad no ha sido seleccionada.")
            return false
        }

        return true
    }

    /**
     * Convierte un valor booleano a entero (0/1) para almacenamiento en base de datos.
     */
    fun booleanAEntero(valor: Boolean): Int = if (valor) 1 else 0

    /**
     * Convierte un valor entero de la base de datos (0/1) a booleano.
     */
    fun enteroABoolean(valor: Int): Boolean = valor == 1

}
