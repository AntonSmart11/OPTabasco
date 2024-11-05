package com.example.optabasco.database

import com.example.optabasco.database.dao.UserDao
import com.example.optabasco.database.models.User

// Clase de servicio de autenticación que gestiona el cambio de contraseñas de los usuarios
class AuthService(private val userDao: UserDao) {

    // Método para cambiar la contraseña de un usuario, verificando su contraseña actual
    suspend fun changePassword(userId: Int, oldPassword: String, newPassword: String, confirmPassword: String): String? {
        val user = userDao.getUserById(userId) // Obtiene el usuario por su ID

        // Verifica que la contraseña anterior sea correcta y que la nueva coincida con la confirmación
        return if (user != null && user.contrasena == oldPassword) {
            if (newPassword == confirmPassword) {
                user.contrasena = newPassword // Actualiza la contraseña
                userDao.updateUser(user) // Guarda los cambios en la base de datos

                null // Indica que la operación fue exitosa
            } else {
                // Retorna mensaje si las contraseñas nuevas no coinciden
                "Las contraseñas nuevas no coinciden"
            }
        } else {
            // Retorna mensaje si la contraseña antigua es incorrecta
            "La contraseña antigua es incorrecta"
        }
    }

    // Método para que un administrador cambie la contraseña de un usuario sin verificar la contraseña actual
    suspend fun changePasswordAdmin(userId: Int, newPassword: String, confirmPassword: String): String? {
        val user = userDao.getUserById(userId)

        // Verifica si el usuario existe y que la nueva contraseña coincida con la confirmación
        return if (user != null) {
            if (newPassword == confirmPassword) {
                user.contrasena = newPassword // Actualiza la contraseña
                userDao.updateUser(user) // Guarda los cambios en la base de datos

                null // Indica que la operación fue exitosa
            } else {
                // Retorna mensaje si las contraseñas nuevas no coinciden
                "Las contraseñas nuevas no coinciden"
            }
        } else {
            // Retorna mensaje si el usuario no se encuentra en la base de datos
            "Usuario no encontrado"
        }
    }
}