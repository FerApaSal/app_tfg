package com.example.tfg_app_makeup.utils

import android.content.ContentValues
import android.database.Cursor

/**
 * Interfaz base que debe implementar cualquier entidad que quiera
 * aprovechar el sistema CRUD genérico con SQLite.
 */
interface CrudEntity {
    /**
     * Convierte la entidad en un objeto ContentValues para operaciones de inserción o actualización.
     */
    fun toContentValues(): ContentValues

    /**
     * Rellena los datos de la entidad a partir de un Cursor obtenido de una consulta SQL.
     * Normalmente implementado como companion object o método de fábrica.
     */
    companion object {
        fun fromCursor(cursor: Cursor): CrudEntity {
            throw NotImplementedError("fromCursor must be overridden in implementing class")
        }
    }
}