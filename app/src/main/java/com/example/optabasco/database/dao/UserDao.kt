package com.example.optabasco.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.optabasco.database.models.User

// Define una interfaz para las operaciones de la base de datos relacionadas con la entidad 'User'
@Dao
interface UserDao {

    // Inserta un objeto 'User' en la base de datos.
    // Si ya existe un usuario con el mismo ID, reemplaza la entrada existente.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    // Recupera todos los usuarios de la tabla 'usuarios'
    @Query("SELECT * FROM usuarios")
    suspend fun getAllUsers(): List<User>

    // Recupera un usuario específico a partir de su CURP
    @Query("SELECT * FROM usuarios WHERE curp = :curp")
    suspend fun getUserByCurp(curp: String): User?

    // Recupera un usuario específico a partir de su correo electrónico
    @Query("SELECT * FROM usuarios WHERE correo = :email")
    suspend fun getUserByEmail(email: String): User?

    // Recupera un usuario específico a partir de su ID
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    // Actualiza un usuario existente en la base de datos.
    // Devuelve el número de filas afectadas.
    @Update
    suspend fun updateUser(user: User) : Int

    // Actualiza el token de un usuario específico identificado por su ID
    @Query("UPDATE usuarios SET token = :newToken WHERE correo = :email")
    suspend fun updateToken(email: String, newToken: String): Int

    // Elimina un usuario específico de la base de datos.
    // Devuelve el número de filas afectadas.
    @Delete
    suspend fun deleteUser(user: User) : Int
}