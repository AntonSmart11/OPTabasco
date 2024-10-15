package com.example.optabasco.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.optabasco.database.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM usuarios")
    suspend fun getAllUsers(): List<User>

    @Query("SELECT * FROM usuarios WHERE curp = :curp")
    suspend fun getUserByCurp(curp: String): User?

    @Query("SELECT * FROM usuarios WHERE correo = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    @Update
    suspend fun updateUser(user: User) : Int

    @Delete
    suspend fun deleteUser(user: User)
}