package com.example.tfg_app_makeup.services

import android.content.Context
import android.util.Log
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Cita

/**
 * Servicio para gestionar las operaciones relacionadas con las citas en la base de datos.
 *
 * @param context Contexto de la aplicación, necesario para inicializar la base de datos.
 */
class CitaService(private val context: Context) {

    /**
     * Obtiene todas las citas almacenadas en la base de datos, ordenadas por fecha y hora.
     *
     * @return Lista de objetos de tipo Cita.
     */
    fun obtenerTodos(): List<Cita> {
        val db = AppDatabase(context).readableDatabase
        val lista = mutableListOf<Cita>()
        try {
            val cursor =
                db.query("citas", null, null, null, null, null, "fecha ASC, hora ASC")
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

    /**
     * Obtiene todas las citas asociadas a un usuario específico.
     *
     * @param idUsuario ID del usuario cuyas citas se desean obtener.
     * @return Lista de objetos de tipo Cita.
     */
    fun obtenerPorUsuario(idUsuario: String): List<Cita> {
        val db = AppDatabase(context).readableDatabase
        val lista = mutableListOf<Cita>()
        try {
            val cursor = db.query(
                "citas",
                null,
                "idUsuario = ?",
                arrayOf(idUsuario),
                null,
                null,
                "fecha ASC, hora ASC"
            )
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

    /**
     * Inserta una nueva cita en la base de datos.
     *
     * @param cita Objeto de tipo Cita que se desea insertar.
     * @return `true` si la cita fue insertada correctamente, `false` en caso contrario.
     */
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

    /**
     * Actualiza una cita existente en la base de datos.
     *
     * @param cita Objeto de tipo Cita que se desea actualizar.
     * @return `true` si la cita fue actualizada correctamente, `false` en caso contrario.
     */
    fun actualizar(cita: Cita): Boolean {
        val db = AppDatabase(context).writableDatabase
        return try {
            val filas =
                db.update("citas", cita.toContentValues(), "id = ?", arrayOf(cita.id))
            Log.d("CitaService", "Cita actualizada: ${cita.id}")
            filas > 0
        } catch (e: Exception) {
            Log.e("CitaService", "Error al actualizar cita: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    /**
     * Obtiene todas las citas con un estado específico.
     *
     * @param estado Estado de las citas que se desean obtener.
     * @return Lista de objetos de tipo Cita.
     */
    fun obtenerPorEstado(estado: String): List<Cita> {
        val citas = mutableListOf<Cita>()
        val db = AppDatabase(context).readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM citas WHERE estado = ?", arrayOf(estado))

        if (cursor.moveToFirst()) {
            do {
                citas.add(Cita.fromCursor(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return citas
    }
}