package com.example.optabasco.database

import com.example.optabasco.database.dao.UserDao
import com.example.optabasco.database.models.User

class AuthService(private val userDao: UserDao) {

    //Método para cambiar la contraseña del usuario
    suspend fun changePassword(userId: Int, oldPassword: String, newPassword: String, confirmPassword: String): String? {
        val user = userDao.getUserById(userId)

        //Verifica que la contraseña anterior sea correcta y que la nueva contraseña coincida con la confirmación
        return if (user != null && user.contrasena == oldPassword) {
            if (newPassword == confirmPassword) {
                user.contrasena = newPassword
                userDao.updateUser(user)

                null //Todo salió bien
            } else {
                // Las contraseñas no coinciden
                "Las contraseñas nuevas no coinciden"
            }
        } else {
            // Contraseña anterior incorrecta
            "La contraseña antigua es incorrecta"
        }
    }

    //Método para cambiar la contraseña del usuario por parte del administrador
    suspend fun changePasswordAdmin(userId: Int, newPassword: String, confirmPassword: String): String? {
        val user = userDao.getUserById(userId)

        //Verifica que la contraseña anterior sea correcta y que la nueva contraseña coincida con la confirmación
        return if (user != null) {
            if (newPassword == confirmPassword) {
                user.contrasena = newPassword
                userDao.updateUser(user)

                null //Todo salió bien
            } else {
                // Las contraseñas no coinciden
                "Las contraseñas nuevas no coinciden"
            }
        } else {
            // El usuario no se encuentra
            "Usuario no encontrado"
        }
    }
}