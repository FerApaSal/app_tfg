package com.example.tfg_app_makeup.controllers

import android.content.Context
import com.example.tfg_app_makeup.model.Material
import com.example.tfg_app_makeup.services.MaterialService

/**
 * Controlador de lógica de negocio para gestionar materiales.
 */
class MaterialController(context: Context) {

    private val materialService = MaterialService(context)

    fun insertar(material: Material): Boolean {
        return materialService.insertar(material)
    }

    fun actualizar(material: Material): Boolean {
        return materialService.actualizar(material)
    }

    fun eliminar(id: String): Boolean {
        return materialService.eliminar(id)
    }

    fun obtenerTodos(): List<Material> {
        return materialService.obtenerTodos()
    }

    fun obtenerPorId(id: String): Material? {
        return materialService.obtenerPorId(id)
    }
}
