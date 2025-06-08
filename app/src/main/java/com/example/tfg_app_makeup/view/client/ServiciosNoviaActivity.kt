package com.example.tfg_app_makeup.view.client

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.helpers.AssetHelper

class ServiciosNoviaActivity : AppCompatActivity() {

    private lateinit var btnAtras : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_novia)

        btnAtras = findViewById(R.id.btnVolverServicios)

        val imageView = findViewById<ImageView>(R.id.ivServiciosNovia)
        val drawable = AssetHelper.cargarImagenDesdeAssets(this, "servicios_novia.jpg")
        if (drawable != null) {
            imageView.setImageDrawable(drawable)
        }

        btnAtras.setOnClickListener {
            finish()
        }
    }
}
