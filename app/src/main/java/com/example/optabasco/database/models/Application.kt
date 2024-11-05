package com.example.optabasco.database.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Define una entidad de base de datos llamada 'solicitudes', que representa una solicitud realizada por un usuario, con una llave foránea
@Entity(
    tableName = "solicitudes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Application(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario_id: Int,
    val titulo: String,
    val calle: String,
    val coloniaRancheria: String,
    val municipio: String,
    val tipoSolicitud: String,
    val descripcion: String,
    val fecha: String,
    val aprobada: String,
    val estadoSolicitud: String
)