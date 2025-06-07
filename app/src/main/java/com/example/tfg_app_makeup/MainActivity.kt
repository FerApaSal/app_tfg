package com.example.tfg_app_makeup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.auth.LoginActivity
import com.example.tfg_app_makeup.db.AppDatabase

/**
 * Punto de entrada de la aplicación.
 * Redirige inmediatamente a la pantalla de login.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            AppDatabase(this).writableDatabase
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
}
