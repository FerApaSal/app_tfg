package com.example.tfg_app_makeup.services

import android.content.Context
import android.database.sqlite.SQLiteException
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Material

class MaterialService(context: Context) {

    private val dbHelper = AppDatabase(context)

    fun insertar(material: Material): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.insert("materiales", null, material.toContentValues()) > 0
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    fun actualizar(material: Material): Boolean {
        return try {
            val db = dbHelper.writableDatabase
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

    fun eliminar(id: String): Boolean {
        return try {
            val db = dbHelper.writableDatabase
            db.delete("materiales", "id = ?", arrayOf(id)) > 0
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }

    fun obtenerTodos(): List<Material> {
        val materiales = mutableListOf<Material>()
        try {
            val db = dbHelper.readableDatabase
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

    fun obtenerPorId(id: String): Material? {
        try {
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM materiales WHERE id = ?", arrayOf(id))
            if (cursor.moveToFirst()) {
                val material = Material.fromCursor(cursor)
                cursor.close()
                return material
            }
            cursor.close()
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        return null
    }
}
