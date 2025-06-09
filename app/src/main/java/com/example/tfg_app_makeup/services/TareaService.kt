package com.example.tfg_app_makeup.services

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Tarea

/**
 * Servicio para gestionar las operaciones relacionadas con las tareas en la base de datos.
 *
 * @param context Contexto de la aplicación, necesario para inicializar la base de datos.
 */
class TareaService(context: Context) {

    private val db: SQLiteDatabase = AppDatabase(context).writableDatabase

    /**
     * Inserta una nueva tarea en la base de datos.
     *
     * @param tarea Objeto de tipo Tarea que se desea insertar.
     * @return `true` si la tarea fue insertada correctamente, `false` en caso contrario.
     */
    fun insertar(tarea: Tarea): Boolean {
        return try {
            db.insert("tareas", null, tarea.toContentValues()) > 0
        } catch (e: Exception) {
            Log.e("TareaService", "Error al insertar tarea: ${e.message}")
            false
        }
    }

    /**
     * Actualiza una tarea existente en la base de datos.
     *
     * @param tarea Objeto de tipo Tarea que se desea actualizar.
     * @return `true` si la tarea fue actualizada correctamente, `false` en caso contrario.
     */
    fun actualizar(tarea: Tarea): Boolean {
        return try {
            db.update("tareas", tarea.toContentValues(), "id = ?", arrayOf(tarea.id)) > 0
        } catch (e: Exception) {
            Log.e("TareaService", "Error al actualizar tarea: ${e.message}")
            false
        }
    }

    /**
     * Elimina una tarea de la base de datos.
     *
     * @param id Identificador único de la tarea que se desea eliminar.
     * @return `true` si la tarea fue eliminada correctamente, `false` en caso contrario.
     */
    fun eliminar(id: String): Boolean {
        return try {
            db.delete("tareas", "id = ?", arrayOf(id)) > 0
        } catch (e: Exception) {
            Log.e("TareaService", "Error al eliminar tarea: ${e.message}")
            false
        }
    }

    /**
     * Obtiene todas las tareas almacenadas en la base de datos.
     *
     * @return Lista de objetos de tipo Tarea.
     */
    fun obtenerTodas(): List<Tarea> {
        val lista = mutableListOf<Tarea>()
        return try {
            val cursor = db.rawQuery("SELECT * FROM tareas", null)
            if (cursor.moveToFirst()) {
                do {
                    lista.add(Tarea.fromCursor(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
            lista
        } catch (e: Exception) {
            Log.e("TareaService", "Error al obtener tareas: ${e.message}")
            emptyList()
        }
    }
}