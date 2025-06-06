package com.example.tfg_app_makeup.helpers

/**
 * Funciones auxiliares específicas para la validación de datos del modelo Usuario.
 */
object UserHelper {

    /**
     * Valida que los campos básicos de registro no estén vacíos.
     *
     * @return true si todos los campos están completos.
     */
    fun camposRegistroValidos(
        nombre: String,
        apellidos: String,
        telefono: String,
        email: String,
        password: String
    ): Boolean {
        return nombre.isNotBlank()
                && apellidos.isNotBlank()
                && telefono.isNotBlank()
                && email.isNotBlank()
                && password.isNotBlank()
    }

    /**
     * Verifica que el teléfono tenga exactamente 9 dígitos.
     */
    fun telefonoValido(telefono: String): Boolean {
        return telefono.matches(Regex("^\\d{9}$"))
    }

    /**
     * Verifica que el email tenga un formato básico válido.
     */
    fun emailValido(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    /**
     * Valida que los campos de login no estén vacíos.
     */
    fun camposLoginValidos(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }
}
