package com.example.tfg_app_makeup.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AppDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL(CREATE_USUARIOS_TABLE)
            db.execSQL(CREATE_CITAS_TABLE)
            db.execSQL(CREATE_TAREAS_TABLE)
            db.execSQL(CREATE_MATERIALES_TABLE)

            Log.d("AppDatabase", "Tablas creadas correctamente.")
        } catch (e: Exception) {
            Log.e("AppDatabase", "Error al crear tablas: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS usuarios")
            db.execSQL("DROP TABLE IF EXISTS citas")
            db.execSQL("DROP TABLE IF EXISTS tareas")
            db.execSQL("DROP TABLE IF EXISTS materiales")

            onCreate(db)
        } catch (e: Exception) {
            Log.e("AppDatabase", "Error al actualizar la base de datos: ${e.message}")
        }
    }

    companion object {
        private const val DATABASE_NAME = "tfg_app.db"
        private const val DATABASE_VERSION = 1

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

        private const val CREATE_TAREAS_TABLE = """
            CREATE TABLE tareas (
                id TEXT PRIMARY KEY,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                prioridad TEXT NOT NULL,
                completada INTEGER NOT NULL CHECK (completada IN (0, 1))
            )
        """

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
