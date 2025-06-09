package com.example.tfg_app_makeup.services

import android.content.Context
import android.database.sqlite.SQLiteException
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Material

/**
 * Servicio para gestionar las operaciones relacionadas con los materiales en la base de datos.
 *
 * @param context Contexto de la aplicación, necesario para inicializar la base de datos.
 */
class MaterialService(context: Context) {

    private val database = AppDatabase(context)

    /**
     * Inserta un nuevo material en la base de datos.
     *
     * @param material Objeto de tipo Material que se desea insertar.
     * @return `true` si el material fue insertado correctamente, `false` en caso contrario.
     */
    fun insertar(material: Material): Boolean {
        return try {
            val db = database.writableDatabase
            db.insert("materiales", null, material.toContentValues()) > 0
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Actualiza un material existente en la base de datos.
     *
     * @param material Objeto de tipo Material que se desea actualizar.
     * @return `true` si el material fue actualizado correctamente, `false` en caso contrario.
     */
    fun actualizar(material: Material): Boolean {
        return try {
            val db = database.writableDatabase
            db.update(
                "materiales",
                material.toContentValues(),
                "id = ?",
                arrayOf(material.id)
            ) > 0
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Elimina un material de la base de datos.
     *
     * @param id Identificador único del material que se desea eliminar.
     * @return `true` si el material fue eliminado correctamente, `false` en caso contrario.
     */
    fun eliminar(id: String): Boolean {
        return try {
            val db = database.writableDatabase
            db.delete("materiales", "id = ?", arrayOf(id)) > 0
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Obtiene todos los materiales almacenados en la base de datos.
     *
     * @return Lista de objetos de tipo Material.
     */
    fun obtenerTodos(): List<Material> {
        val materiales = mutableListOf<Material>()
        try {
            val db = database.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM materiales", null)
            if (cursor.moveToFirst()) {
                do {
                    materiales.add(Material.fromCursor(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        return materiales
    }
}