package com.example.tfg_app_makeup.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * Clase que gestiona la base de datos SQLite de la aplicación.
 * Se encarga de crear y actualizar las tablas necesarias.
 *
 * @param context Contexto de la aplicación.
 */
class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Método que se ejecuta al crear la base de datos por primera vez.
     * Crea las tablas necesarias para la aplicación.
     *
     * @param db Instancia de la base de datos SQLite.
     */
    override fun onCreate(db: SQLiteDatabase) {
        try {
            // Crear tabla de usuarios
            db.execSQL(CREATE_USUARIOS_TABLE)
            // Crear tabla de citas
            db.execSQL(CREATE_CITAS_TABLE)
            // Crear tabla de tareas
            db.execSQL(CREATE_TAREAS_TABLE)
            // Crear tabla de materiales
            db.execSQL(CREATE_MATERIALES_TABLE)

            Log.d("AppDatabase", "Tablas creadas correctamente.")
        } catch (e: Exception) {
            // Registrar error en caso de fallo al crear las tablas
            Log.e("AppDatabase", "Error al crear tablas: ${e.message}")
        }
    }

    /**
     * Método que se ejecuta al actualizar la versión de la base de datos.
     * Elimina las tablas existentes y las vuelve a crear.
     *
     * @param db Instancia de la base de datos SQLite.
     * @param oldVersion Versión anterior de la base de datos.
     * @param newVersion Nueva versión de la base de datos.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            // Eliminar tablas existentes
            db.execSQL("DROP TABLE IF EXISTS usuarios")
            db.execSQL("DROP TABLE IF EXISTS citas")
            db.execSQL("DROP TABLE IF EXISTS tareas")
            db.execSQL("DROP TABLE IF EXISTS materiales")

            // Volver a crear las tablas
            onCreate(db)
        } catch (e: Exception) {
            // Registrar error en caso de fallo al actualizar la base de datos
            Log.e("AppDatabase", "Error al actualizar la base de datos: ${e.message}")
        }
    }

    companion object {
        // Nombre del archivo de la base de datos
        private const val DATABASE_NAME = "tfg_app.db"
        // Versión de la base de datos
        private const val DATABASE_VERSION = 1

        // Sentencia SQL para crear la tabla de usuarios
        private const val CREATE_USUARIOS_TABLE = """
            CREATE TABLE usuarios (
                id TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                telefono TEXT NOT NULL,
                correo TEXT NOT NULL UNIQUE,
                clave TEXT NOT NULL,
                rol TEXT NOT NULL,
                imagenUrl TEXT
            )
        """

        // Sentencia SQL para crear la tabla de citas
        private const val CREATE_CITAS_TABLE = """
            CREATE TABLE citas (
                id TEXT PRIMARY KEY,
                tipoServicio TEXT NOT NULL,
                fecha TEXT NOT NULL,
                hora TEXT NOT NULL,
                direccion TEXT NOT NULL,
                estado TEXT NOT NULL,
                idUsuario TEXT NOT NULL,
                nombreClienteManual TEXT,
                telefonoClienteManual TEXT,
                FOREIGN KEY(idUsuario) REFERENCES usuarios(id)
            )
        """

        // Sentencia SQL para crear la tabla de tareas
        private const val CREATE_TAREAS_TABLE = """
            CREATE TABLE tareas (
                id TEXT PRIMARY KEY,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                prioridad TEXT NOT NULL,
                completada INTEGER NOT NULL CHECK (completada IN (0, 1))
            )
        """

        // Sentencia SQL para crear la tabla de materiales
        private const val CREATE_MATERIALES_TABLE = """
             CREATE TABLE materiales (
                id TEXT PRIMARY KEY,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                tipo TEXT NOT NULL,
                cantidad INTEGER NOT NULL,
                estado TEXT NOT NULL,
                imagenUrl TEXT
            )
        """
    }
}