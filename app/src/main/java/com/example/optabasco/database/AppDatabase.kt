package com.example.optabasco.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.optabasco.database.dao.ApplicationDao
import com.example.optabasco.database.dao.UserDao
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User
import java.util.concurrent.Executor
import java.util.concurrent.Executors

// Define la base de datos 'AppDatabase' con las entidades 'User' y 'Application'
@Database(entities = [User::class, Application::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Proporciona acceso a los DAOs (Data Access Objects) para la base de datos
    abstract fun userDao(): UserDao
    abstract fun applicationDao(): ApplicationDao

    companion object {
        // Instancia singleton de la base de datos, marcada como 'volatile' para asegurar visibilidad entre hilos
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Método para obtener una instancia única de la base de datos
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { // Bloquea la instancia para evitar duplicados
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Nombre de la base de datos
                )
                .addCallback(roomCallback) // Añade un callback para operaciones adicionales en la creación
                .build()
                INSTANCE = instance // Asigna la instancia creada a 'INSTANCE'
                instance
            }
        }

        // Callback que ejecuta código adicional al crear la base de datos
        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                //Llama a la función que inserta el usuario administrador en un hilo separado
                ioThread {
                    kotlinx.coroutines.runBlocking { // Ejecuta la inserción dentro de un contexto de coroutines
                        INSTANCE?.let { database ->
                            database.userDao().insert(getAdminUser()) // Inserta el usuario administrador
                        }
                    }
                }
            }
        }

        //Función para definir el usuario administrador
        private fun getAdminUser(): User {
            return User(
                nombre = "Admin",
                paterno = "",
                materno = "",
                telefono = "0000000000",
                correo = "admin@optabasco.com",
                curp = "ADMIN000000000000",
                contrasena = "admin123",
                nivel = 1,
                token = ""
            )
        }

        // Ejecuta código en un hilo separado usando un único hilo de ejecución
        val IO_EXECUTOR = Executors.newSingleThreadExecutor()

        // Función para ejecutar una tarea en el hilo de entrada/salida (I/O)
        fun ioThread(f: () -> Unit) {
            IO_EXECUTOR.execute(f)
        }
    }
}