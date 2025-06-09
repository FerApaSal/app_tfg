package com.example.tfg_app_makeup.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import com.example.tfg_app_makeup.helpers.NoviasHelper
import com.example.tfg_app_makeup.view.common.BaseDrawerActivity

/**
 * Pantalla de administradora para subir una imagen informativa para novias.
 * La imagen seleccionada se guarda en disco local y puede ser visualizada por clientes.
 */
class SeccionNoviasActivity : BaseDrawerActivity() {

    private lateinit var btnVolver: ImageButton
    private lateinit var btnSubirImagen: Button
    private lateinit var ivImagen: ImageView

    private val REQUEST_CODE_PICK_IMAGE = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_drawer)

        val contenido = layoutInflater.inflate(R.layout.activity_seccion_novias, findViewById(R.id.contenidoPrincipal))

        btnSubirImagen = contenido.findViewById(R.id.btnSubirImagenNovias)
        btnVolver = contenido.findViewById(R.id.btnVolverNovias)
        ivImagen = contenido.findViewById(R.id.ivImagenNovias)

        NoviasHelper.cargarImagenInformativa(this, ivImagen)

        btnSubirImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnVolver.setOnClickListener {
            finish()
        }

        configurarMenuHamburguesa()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val archivoGuardado = NoviasHelper.guardarImagenInformativa(this, data.data!!)
            if (archivoGuardado != null) {
                Glide.with(this)
                    .load(archivoGuardado)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(ivImagen)
                Toast.makeText(this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
