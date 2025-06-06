package com.example.tfg_app_makeup.helpers

import android.content.Context
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.GenericDatabaseHelper

/**
 * Helper específico para gestionar la tabla de usuarios.
 */
class UserDatabaseHelper(context: Context) : GenericDatabaseHelper<Usuario>(
    context,
    dbName = "tfg_app.db",
    version = 1
) {
    override val tableName: String = "usuarios"

    override val createTableQuery: String = """
        CREATE TABLE IF NOT EXISTS $tableName (
            id TEXT PRIMARY KEY,
            nombre TEXT NOT NULL,
            apellidos TEXT NOT NULL,
            telefono TEXT NOT NULL,
            email TEXT NOT NULL UNIQUE,
            password TEXT NOT NULL,
            rol TEXT NOT NULL,
            imagenUrl TEXT
        )
    """.trimIndent()

    override fun fromCursor(cursor: android.database.Cursor): Usuario {
        return Usuario.fromCursor(cursor)
    }
}
