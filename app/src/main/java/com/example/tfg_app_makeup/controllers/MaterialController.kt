package com.example.tfg_app_makeup.controllers

    import android.content.Context
    import com.example.tfg_app_makeup.model.Material
    import com.example.tfg_app_makeup.services.MaterialService

    /**
     * Controlador de lógica de negocio para gestionar materiales.
     * Proporciona métodos para insertar, actualizar, eliminar y obtener materiales.
     *
     * @param context Contexto de la aplicación, necesario para inicializar el servicio de materiales.
     */
    class MaterialController(context: Context) {

        private val service = MaterialService(context)

        /**
         * Inserta un nuevo material en la base de datos.
         *
         * @param material Objeto de tipo Material que se desea insertar.
         * @return `true` si el material fue insertado correctamente, `false` en caso contrario.
         */
        fun insertar(material: Material): Boolean {
            return service.insertar(material)
        }

        /**
         * Actualiza un material existente en la base de datos.
         *
         * @param material Objeto de tipo Material que se desea actualizar.
         * @return `true` si el material fue actualizado correctamente, `false` en caso contrario.
         */
        fun actualizar(material: Material): Boolean {
            return service.actualizar(material)
        }

        /**
         * Elimina un material de la base de datos.
         *
         * @param id Identificador único del material que se desea eliminar.
         * @return `true` si el material fue eliminado correctamente, `false` en caso contrario.
         */
        fun eliminar(id: String): Boolean {
            return service.eliminar(id)
        }

        /**
         * Obtiene todos los materiales almacenados en la base de datos.
         *
         * @return Lista de objetos de tipo Material.
         */
        fun obtenerTodos(): List<Material> {
            return service.obtenerTodos()
        }
    }