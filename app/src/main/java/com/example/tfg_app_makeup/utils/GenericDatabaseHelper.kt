package com.example.tfg_app_makeup.utils

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Clase base genérica para gestionar operaciones CRUD en SQLite.
 * Las entidades deben implementar CrudEntity para ser compatibles.
 *
 * @param context Contexto de la aplicación.
 * @param dbName Nombre de la base de datos.
 * @param version Versión de la base de datos (usar 1 como inicial).
 */
abstract class GenericDatabaseHelper<T : CrudEntity>(
    context: Context,
    dbName: String,
    version: Int = 1
) : SQLiteOpenHelper(context, dbName, null, version) {

    /**
     * Nombre de la tabla que gestiona esta instancia.
     */
    abstract val tableName: String

    /**
     * SQL de creación de la tabla. Se ejecuta en onCreate().
     */
    abstract val createTableQuery: String

    /**
     * Conversión desde Cursor a objeto de tipo T.
     */
    abstract fun fromCursor(cursor: Cursor): T

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(createTableQuery)
            Log.d("DBHelper", "Tabla '$tableName' creada correctamente.")
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al crear la tabla '$tableName': ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS $tableName")
            onCreate(db)
            Log.d("DBHelper", "Tabla '$tableName' actualizada de versión $oldVersion a $newVersion.")
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al actualizar la tabla '$tableName': ${e.message}")
        }
    }

    /**
     * Inserta un nuevo registro en la base de datos.
     *
     * @param item Objeto a insertar.
     * @return true si fue exitoso, false si hubo error.
     */
    fun insert(item: T): Boolean {
        return try {
            writableDatabase.insertOrThrow(tableName, null, item.toContentValues())
            Log.d("DBHelper", "Insertado en '$tableName': $item")
            true
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al insertar en '$tableName': ${e.message}")
            false
        }
    }

    /**
     * Elimina un registro por ID.
     *
     * @param id ID del elemento a eliminar.
     * @return true si se eliminó correctamente.
     */
    fun deleteById(id: String): Boolean {
        return try {
            val result = writableDatabase.delete(tableName, "id = ?", arrayOf(id))
            Log.d("DBHelper", "Eliminado de '$tableName' ID=$id (filas afectadas: $result)")
            result > 0
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al eliminar en '$tableName': ${e.message}")
            false
        }
    }

    /**
     * Devuelve todos los elementos de la tabla.
     */
    fun getAll(): List<T> {
        val list = mutableListOf<T>()
        val query = "SELECT * FROM $tableName"
        val db = readableDatabase

        try {
            val cursor = db.rawQuery(query, null)
            if (cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al consultar todos en '$tableName': ${e.message}")
        }

        return list
    }

    /**
     * Busca un elemento por su email.
     *
     * @param email Email del usuario.
     * @return El objeto si existe, null si no.
     */
    fun getByEmail(email: String): T? {
        val query = "SELECT * FROM $tableName WHERE email = ?"
        val db = readableDatabase
        var result: T? = null

        try {
            val cursor = db.rawQuery(query, arrayOf(email))
            if (cursor.moveToFirst()) {
                result = fromCursor(cursor)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("DBHelper", "Error al buscar por email en '$tableName': ${e.message}")
        }

        return result
    }
}
