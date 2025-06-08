package com.example.tfg_app_makeup.services

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Cita

class CitaService(private val context: Context) {

    fun obtenerTodos(): List<Cita> {
        val db = AppDatabase(context).readableDatabase
        val lista = mutableListOf<Cita>()
        try {
            val cursor = db.query("citas", null, null, null, null, null, "fecha ASC, hora ASC")
            while (cursor.moveToNext()) {
                lista.add(Cita.fromCursor(cursor))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CitaService", "Error al obtener citas: ${e.message}")
        } finally {
            db.close()
        }
        return lista
    }

    fun obtenerPorId(id: String): Cita? {
        val db = AppDatabase(context).readableDatabase
        var cita: Cita? = null
        try {
            val cursor = db.query("citas", null, "id = ?", arrayOf(id), null, null, null)
            if (cursor.moveToFirst()) {
                cita = Cita.fromCursor(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CitaService", "Error al buscar cita por ID: ${e.message}")
        } finally {
            db.close()
        }
        return cita
    }

    fun obtenerPorUsuario(idUsuario: String): List<Cita> {
        val db = AppDatabase(context).readableDatabase
        val lista = mutableListOf<Cita>()
        try {
            val cursor = db.query("citas", null, "idUsuario = ?", arrayOf(idUsuario), null, null, "fecha ASC, hora ASC")
            while (cursor.moveToNext()) {
                lista.add(Cita.fromCursor(cursor))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CitaService", "Error al buscar citas por usuario: ${e.message}")
        } finally {
            db.close()
        }
        return lista
    }

    fun insertar(cita: Cita): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val result = db.insert("citas", null, cita.toContentValues())
            Log.d("CitaService", "Cita insertada: ${cita.id}")
            result != -1L
        } catch (e: Exception) {
            Log.e("CitaService", "Error al insertar cita: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun actualizar(cita: Cita): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val filas = db.update("citas", cita.toContentValues(), "id = ?", arrayOf(cita.id))
            Log.d("CitaService", "Cita actualizada: ${cita.id}")
            filas > 0
        } catch (e: Exception) {
            Log.e("CitaService", "Error al actualizar cita: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun eliminarPorId(id: String): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val filas = db.delete("citas", "id = ?", arrayOf(id))
            Log.d("CitaService", "Cita eliminada: $id")
            filas > 0
        } catch (e: Exception) {
            Log.e("CitaService", "Error al eliminar cita: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun obtenerPorEstado(estado: String): List<Cita> {
        val citas = mutableListOf<Cita>()
        val db = AppDatabase(context).readableDatabase
        val cursor = db.rawQuery("SELECT * FROM citas WHERE estado = ?", arrayOf(estado))

        if (cursor.moveToFirst()) {
            do {
                citas.add(Cita.fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return citas
    }


    /**
     * Obtiene todas las citas de una fecha específica con un estado determinado.
     * @param fecha Fecha en formato "dd/MM/yyyy"
     * @param estado Estado de la cita (por ejemplo: "ACEPTADA")
     */
    fun obtenerPorFechaYEstado(fecha: String, estado: String): List<Cita> {
        val citas = mutableListOf<Cita>()
        val db = AppDatabase(context).readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM citas WHERE fecha = ? AND estado = ? ORDER BY hora ASC",
            arrayOf(fecha, estado)
        )
        if (cursor.moveToFirst()) {
            do {
                val cita = Cita.fromCursor(cursor)
                citas.add(cita)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return citas
    }
}
