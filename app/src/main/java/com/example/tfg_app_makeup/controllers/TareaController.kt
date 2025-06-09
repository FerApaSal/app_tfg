package com.example.tfg_app_makeup.controllers

    import android.content.Context
    import com.example.tfg_app_makeup.model.Tarea
    import com.example.tfg_app_makeup.services.TareaService

    /**
     * Controlador de lógica de negocio para gestionar tareas.
     * Proporciona métodos para insertar, actualizar, eliminar y obtener tareas.
     *
     * @param context Contexto de la aplicación, necesario para inicializar el servicio de tareas.
     */
    class TareaController(context: Context) {

        private val service = TareaService(context)

        /**
         * Inserta una nueva tarea en la base de datos.
         *
         * @param tarea Objeto de tipo Tarea que se desea insertar.
         * @return `true` si la tarea fue insertada correctamente, `false` en caso contrario.
         */
        fun insertar(tarea: Tarea): Boolean {
            return service.insertar(tarea)
        }

        /**
         * Actualiza una tarea existente en la base de datos.
         *
         * @param tarea Objeto de tipo Tarea que se desea actualizar.
         * @return `true` si la tarea fue actualizada correctamente, `false` en caso contrario.
         */
        fun actualizar(tarea: Tarea): Boolean {
            return service.actualizar(tarea)
        }

        /**
         * Elimina una tarea de la base de datos.
         *
         * @param id Identificador único de la tarea que se desea eliminar.
         * @return `true` si la tarea fue eliminada correctamente, `false` en caso contrario.
         */
        fun eliminar(id: String): Boolean {
            return service.eliminar(id)
        }

        /**
         * Obtiene todas las tareas almacenadas en la base de datos.
         *
         * @return Lista de objetos de tipo Tarea.
         */
        fun obtenerTodas(): List<Tarea> {
            return service.obtenerTodas()
        }
    }