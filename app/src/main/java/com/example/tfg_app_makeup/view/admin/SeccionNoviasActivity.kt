package com.example.tfg_app_makeup.view.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tfg_app_makeup.R
import java.io.File

/**
 * Pantalla de administradora para subir una imagen informativa para novias.
 * La imagen seleccionada se guarda en disco local y puede ser visualizada por clientes.
 */
class SeccionNoviasActivity : AppCompatActivity() {

    private lateinit var btnVolver: ImageButton
    private lateinit var btnSubirImagen: Button
    private lateinit var ivImagen: ImageView

    private val REQUEST_CODE_PICK_IMAGE = 201
    private val NOMBRE_ARCHIVO_NOVIAS = "novias.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seccion_novias)

        btnVolver = findViewById(R.id.btnVolverNovias)
        btnSubirImagen = findViewById(R.id.btnSubirImagenNovias)
        ivImagen = findViewById(R.id.ivImagenNovias)

        cargarImagenSiExiste()

        btnSubirImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    /**
     * Carga la imagen previamente guardada si existe en almacenamiento local.
     */
    private fun cargarImagenSiExiste() {
        val archivo = File(filesDir, NOMBRE_ARCHIVO_NOVIAS)
        if (archivo.exists()) {
            Glide.with(this)
                .load(archivo)
                .placeholder(R.drawable.ic_user_placeholder)
                .error(R.drawable.ic_user_placeholder)
                .into(ivImagen)
        } else {
            ivImagen.setImageResource(R.drawable.ic_user_placeholder)
        }
    }

    /**
     * Maneja el resultado de selección de imagen desde la galería.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data?.data != null) {
            val uri = data.data
            try {
                val inputStream = contentResolver.openInputStream(uri!!)
                val archivoDestino = File(filesDir, NOMBRE_ARCHIVO_NOVIAS)
                archivoDestino.outputStream().use { output ->
                    inputStream?.copyTo(output)
                }

                Glide.with(this)
                    .load(archivoDestino)
                    .placeholder(R.drawable.ic_user_placeholder)
                    .into(ivImagen)

                Toast.makeText(this, "Imagen actualizada correctamente", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("SeccionNoviasActivity", "Error al guardar imagen", e)
                Toast.makeText(this, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
