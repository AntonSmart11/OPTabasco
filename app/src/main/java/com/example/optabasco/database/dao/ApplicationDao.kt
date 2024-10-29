package com.example.optabasco.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User

@Dao
interface ApplicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: Application)

    @Query("SELECT * FROM solicitudes")
    suspend fun getAllApplications(): List<Application>

    @Query("SELECT * FROM solicitudes WHERE id = :id")
    suspend fun getApplicationById(id: Int): Application?

    @Query("SELECT * FROM solicitudes WHERE usuario_id = :userId ORDER BY fecha DESC")
    suspend fun getApplicationByUserId(userId: Int): List<Application>

    @Update
    suspend fun updateApplication(application: Application) : Int

    @Delete
    suspend fun deleteApplication(application: Application) : Int
}