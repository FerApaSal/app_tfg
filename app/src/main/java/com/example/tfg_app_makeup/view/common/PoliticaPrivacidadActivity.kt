package com.example.tfg_app_makeup.view.common

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R

/**
 * Pantalla de solo lectura que muestra la política de privacidad.
 * No permite interacción más allá de volver atrás.
 */
class PoliticaPrivacidadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_politica_privacidad)

        val btnVolver = findViewById<ImageButton>(R.id.btnVolverPolitica)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
