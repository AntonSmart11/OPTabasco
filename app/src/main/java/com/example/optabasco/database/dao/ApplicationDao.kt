package com.example.optabasco.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User

// Se define una interfaz para las operaciones de la base de datos relacionadas con la entidad 'Application'
@Dao
interface ApplicationDao {

    // Inserta un objeto 'Application' en la base de datos.
    // Si ya existe una entrada con el mismo ID, reemplaza la existente.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(application: Application)

    // Recupera todas las aplicaciones de la tabla 'solicitudes'
    @Query("SELECT * FROM solicitudes")
    suspend fun getAllApplications(): List<Application>

    // Recupera todas las aplicaciones, ordenadas de forma descendente por fecha
    @Query("SELECT * FROM solicitudes ORDER BY fecha DESC")
    suspend fun getAllApplicationsByDate(): List<Application>

    // Recupera una aplicación específica a partir de su ID
    @Query("SELECT * FROM solicitudes WHERE id = :id")
    suspend fun getApplicationById(id: Int): Application?

    // Recupera todas las aplicaciones asociadas a un usuario específico,
    // ordenadas de forma descendente por fecha
    @Query("SELECT * FROM solicitudes WHERE usuario_id = :userId ORDER BY fecha DESC")
    suspend fun getApplicationByUserId(userId: Int): List<Application>

    // Actualiza una aplicación existente en la base de datos.
    // Devuelve el número de filas afectadas.
    @Update
    suspend fun updateApplication(application: Application) : Int

    // Elimina una aplicación específica de la base de datos.
    // Devuelve el número de filas afectadas.
    @Delete
    suspend fun deleteApplication(application: Application) : Int

    // Elimina todas las aplicaciones asociadas a un usuario específico.
    // Devuelve el número de filas afectadas.
    @Query("DELETE FROM solicitudes WHERE usuario_id = :userId")
    suspend fun deleteApplicationsByUserId(userId: Int) : Int
}