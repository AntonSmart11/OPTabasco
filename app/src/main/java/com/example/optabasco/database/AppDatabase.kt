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

@Database(entities = [User::class, Application::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun applicationDao(): ApplicationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addCallback(roomCallback)
                .build()
                INSTANCE = instance
                instance
            }
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                //Llamar a la función que inserta el usuario administrador en un hilo separado
                ioThread {
                    kotlinx.coroutines.runBlocking {
                        INSTANCE?.let { database ->
                            database.userDao().insert(getAdminUser())
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
                nivel = 1
            )
        }

        //Función para ejecutar el código en un hilo separado
        val IO_EXECUTOR = Executors.newSingleThreadExecutor()

        fun ioThread(f: () -> Unit) {
            IO_EXECUTOR.execute(f)
        }
    }
}