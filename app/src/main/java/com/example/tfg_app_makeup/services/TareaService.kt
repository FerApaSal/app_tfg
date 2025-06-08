package com.example.tfg_app_makeup.services

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Tarea

class TareaService(context: Context) {

    private val db: SQLiteDatabase = AppDatabase(context).writableDatabase

    fun insertar(tarea: Tarea): Boolean {
        return db.insert("tareas", null, tarea.toContentValues()) > 0
    }

    fun actualizar(tarea: Tarea): Boolean {
        return db.update("tareas", tarea.toContentValues(), "id = ?", arrayOf(tarea.id)) > 0
    }

    fun eliminar(id: String): Boolean {
        return db.delete("tareas", "id = ?", arrayOf(id)) > 0
    }

    fun obtenerTodas(): List<Tarea> {
        val lista = mutableListOf<Tarea>()
        val cursor = db.rawQuery("SELECT * FROM tareas", null)

        if (cursor.moveToFirst()) {
            do {
                lista.add(Tarea.fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return lista
    }

    fun obtenerPorId(id: String): Tarea? {
        val cursor = db.rawQuery("SELECT * FROM tareas WHERE id = ?", arrayOf(id))

        val tarea = if (cursor.moveToFirst()) {
            Tarea.fromCursor(cursor)
        } else null

        cursor.close()
        return tarea
    }
}
