package com.example.tfg_app_makeup.view.client

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.helpers.NoviasHelper

/**
 * Pantalla que muestra la imagen informativa de servicios nupciales.
 * Esta imagen es subida por la administradora y visualizada en modo lectura por los clientes.
 */
class ServiciosNoviaActivity : AppCompatActivity() {

    private lateinit var btnAtras: ImageButton
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicios_novia)

        inicializarComponentes()
        cargarImagenInformativa()

        btnAtras.setOnClickListener {
            finish()
        }
    }

    /**
     * Enlaza los elementos visuales del layout con las variables de clase.
     */
    private fun inicializarComponentes() {
        btnAtras = findViewById(R.id.btnVolverServicios)
        imageView = findViewById(R.id.ivServiciosNovia)
    }

    /**
     * Carga la imagen estática de servicios para novias desde la carpeta de assets.
     */
    private fun cargarImagenInformativa() {
        val drawable = NoviasHelper.cargarImagenDesdeAssets(this, "servicios_novia.jpg")
        drawable?.let {
            imageView.setImageDrawable(it)
        }
    }
}