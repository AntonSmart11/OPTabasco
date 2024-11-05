package com.example.optabasco.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define una entidad de base de datos llamada 'usuarios'
@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val paterno: String,
    val materno: String,
    val correo: String,
    val telefono: String,
    val curp: String,
    var contrasena: String,
    val nivel: Int,
)