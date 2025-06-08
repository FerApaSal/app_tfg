package com.example.tfg_app_makeup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.controllers.UsuarioController
import com.example.tfg_app_makeup.db.AppDatabase
import com.example.tfg_app_makeup.model.Usuario
import com.example.tfg_app_makeup.utils.EncryptUtil
import java.util.UUID

/**
 * Punto de entrada de la aplicación.
 * Redirige inmediatamente a la pantalla de login.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            AppDatabase(this).writableDatabase
            insertarAdminDemoSiNoExiste()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error al inicializar la base de datos: ${e.message}", e)
        }

        try {
            Log.d("MainActivity", "Iniciando redirección a LoginActivity")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        } catch (e: Exception) {
            Log.e("MainActivity", "Error al iniciar LoginActivity: ${e.message}", e)
        }
    }

    private fun insertarAdminDemoSiNoExiste() {
        val controller = UsuarioController(this)
        val correoAdmin = "admin@demo.com"

        if (!controller.existeCorreo(correoAdmin)) {
            val admin = Usuario(
                id = UUID.randomUUID().toString(),
                nombre = "Merche",
                apellido = "Makeup",
                telefono = "608150648",
                correo = correoAdmin,
                clave = EncryptUtil.encrypt("admin123"),
                imagenUrl = null,
                rol = "ADMIN"
            )
            controller.insertar(admin)
            Log.d("Seeder", "Administrador de prueba insertado correctamente.")
        } else {
            Log.d("Seeder", "Administrador de prueba ya existe. No se vuelve a insertar.")
        }
    }

}
