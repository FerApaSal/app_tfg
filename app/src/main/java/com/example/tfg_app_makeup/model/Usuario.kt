package com.example.tfg_app_makeup.model

  import android.content.ContentValues
  import android.database.Cursor

  /**
   * Modelo de datos para representar un usuario.
   * Incluye información básica como nombre, apellido, teléfono, correo, clave, rol e imagen.
   *
   * @property id Identificador único del usuario.
   * @property nombre Nombre del usuario.
   * @property apellido Apellido del usuario.
   * @property telefono Teléfono del usuario.
   * @property correo Correo electrónico del usuario.
   * @property clave Contraseña del usuario.
   * @property rol Rol del usuario (ej. "ADMIN", "CLIENTE").
   * @property imagenUrl URL de la imagen asociada al usuario (opcional).
   */
  data class Usuario(
      val id: String,
      var nombre: String,
      var apellido: String,
      var telefono: String,
      var correo: String,
      var clave: String,
      var rol: String,
      var imagenUrl: String? = null,
  ) {
      /**
       * Convierte el objeto `Usuario` a un objeto `ContentValues` para operaciones con SQLite.
       * @return Objeto `ContentValues` con los datos del usuario.
       */
      fun toContentValues(): ContentValues {
          return ContentValues().apply {
              put("id", id)
              put("nombre", nombre)
              put("apellido", apellido)
              put("telefono", telefono)
              put("correo", correo)
              put("clave", clave)
              put("rol", rol)
              put("imagenUrl", imagenUrl)
          }
      }

      companion object {
          /**
           * Crea un objeto `Usuario` a partir de un cursor de SQLite.
           * @param cursor Cursor que contiene los datos del usuario.
           * @return Objeto `Usuario` con los datos extraídos del cursor.
           */
          fun fromCursor(cursor: Cursor): Usuario {
              return Usuario(
                  id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                  nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                  apellido = cursor.getString(cursor.getColumnIndexOrThrow("apellido")),
                  telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                  correo = cursor.getString(cursor.getColumnIndexOrThrow("correo")),
                  clave = cursor.getString(cursor.getColumnIndexOrThrow("clave")),
                  rol = cursor.getString(cursor.getColumnIndexOrThrow("rol")),
                  imagenUrl = cursor.getString(cursor.getColumnIndexOrThrow("imagenUrl")),
              )
          }
      }
  }