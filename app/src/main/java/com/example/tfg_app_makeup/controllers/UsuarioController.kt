package com.example.tfg_app_makeup.controllers

    import android.content.Context
    import android.util.Log
    import android.widget.Toast
    import com.example.tfg_app_makeup.db.AppDatabase
    import com.example.tfg_app_makeup.helpers.UsuarioHelper
    import com.example.tfg_app_makeup.model.Usuario
    import com.example.tfg_app_makeup.services.UsuarioService

    /**
     * Controlador de lógica de negocio para gestionar usuarios.
     * Proporciona métodos para insertar, actualizar, autenticar y obtener usuarios.
     *
     * @param context Contexto de la aplicación, necesario para inicializar el servicio y la base de datos.
     */
    class UsuarioController(private val context: Context) {

        private val service = UsuarioService(context)
        private val database = AppDatabase(context)

        /**
         * Inserta un nuevo usuario en la base de datos si es válido.
         *
         * @param usuario Objeto de tipo Usuario que se desea insertar.
         * @return `true` si el usuario fue insertado correctamente, `false` en caso contrario.
         */
        fun insertar(usuario: Usuario): Boolean {
            return if (UsuarioHelper.validar(usuario)) {
                val result = service.insertar(usuario)
                Log.d("UsuarioController", "Insert resultado: $result")
                result
            } else {
                Log.w("UsuarioController", "Usuario no válido, inserción cancelada.")
                false
            }
        }

        /**
         * Actualiza un usuario existente en la base de datos si es válido.
         *
         * @param usuario Objeto de tipo Usuario que se desea actualizar.
         * @return `true` si el usuario fue actualizado correctamente, `false` en caso contrario.
         */
        fun actualizar(usuario: Usuario): Boolean {
            return if (UsuarioHelper.validar(usuario)) {
                val result = service.actualizar(usuario)
                Log.d("UsuarioController", "Update resultado: $result")
                result
            } else {
                Log.w("UsuarioController", "Usuario no válido, actualización cancelada.")
                false
            }
        }

        /**
         * Autentica un usuario mediante su correo y contraseña.
         *
         * @param correo Correo del usuario.
         * @param password Contraseña del usuario.
         * @return Objeto de tipo Usuario si las credenciales son correctas, `null` en caso contrario.
         */
        fun login(correo: String, password: String): Usuario? {
            val usuario = service.obtenerPorCorreo(correo)

            return if (correo.isBlank() || password.isBlank()) {
                Log.w("UsuarioController", "Correo o contraseña vacíos.")
                Toast.makeText(context, "Correo y contraseña no pueden estar vacíos.", Toast.LENGTH_SHORT).show()
                null
            } else if (usuario != null && usuario.clave == password) {
                Log.d("UsuarioController", "Login exitoso para: $correo")
                usuario
            } else {
                Log.w("UsuarioController", "Login fallido para: $correo")
                Toast.makeText(context, "Credenciales incorrectas.", Toast.LENGTH_SHORT).show()
                null
            }
        }

        /**
         * Verifica si un correo ya está registrado en la base de datos.
         *
         * @param correo Correo a verificar.
         * @return `true` si el correo existe, `false` en caso contrario.
         */
        fun existeCorreo(correo: String): Boolean {
            return service.obtenerPorCorreo(correo) != null
        }

        /**
         * Obtiene una lista de usuarios que tienen un rol específico.
         *
         * @param rolBuscado Rol de los usuarios que se desean obtener.
         * @return Lista de usuarios con el rol especificado.
         */
        fun obtenerUsuariosPorRol(rolBuscado: String): List<Usuario> {
            val lista = mutableListOf<Usuario>()
            val db = database.readableDatabase
            val cursor = db.query(
                "usuarios", null,
                "rol = ?", arrayOf(rolBuscado),
                null, null, "nombre ASC"
            )

            cursor.use {
                while (it.moveToNext()) {
                    lista.add(Usuario.fromCursor(it))
                }
            }

            return lista
        }

        /**
         * Obtiene todos los usuarios almacenados en la base de datos.
         *
         * @return Lista de objetos de tipo Usuario.
         */
        fun obtenerTodos(): List<Usuario> {
            return service.obtenerTodos()
        }

        /**
         * Devuelve un mapa de usuarios con rol CLIENTE, indexados por su ID.
         *
         * @return Mapa de usuarios con rol CLIENTE.
         */
        fun obtenerUsuariosMapeados(): Map<String, Usuario> {
            return obtenerUsuariosPorRol("CLIENTE").associateBy { it.id }
        }
    }