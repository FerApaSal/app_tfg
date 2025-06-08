package com.example.tfg_app_makeup.controllers

import android.content.Context
import com.example.tfg_app_makeup.model.Tarea
import com.example.tfg_app_makeup.services.TareaService

class TareaController(context: Context) {

    private val service = TareaService(context)

    fun insertar(tarea: Tarea): Boolean {
        return service.insertar(tarea)
    }

    fun actualizar(tarea: Tarea): Boolean {
        return service.actualizar(tarea)
    }

    fun eliminar(id: String): Boolean {
        return service.eliminar(id)
    }

    fun obtenerTodas(): List<Tarea> {
        return service.obtenerTodas()
    }

    fun obtenerPorId(id: String): Tarea? {
        return service.obtenerPorId(id)
    }
}
