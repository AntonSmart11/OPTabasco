package com.example.optabasco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.optabasco.views.LoginScreen
import com.example.optabasco.views.RegisterScreen
import com.example.optabasco.views.admin.DashboardAdminScreen
import com.example.optabasco.views.ProfileScreen
import com.example.optabasco.views.admin.ApplicationAdminScreen
import com.example.optabasco.views.admin.ApplicationMenuAdminScreen
import com.example.optabasco.views.admin.UserAdminScreen
import com.example.optabasco.views.admin.UserMenuAdminScreen
import com.example.optabasco.views.users.ApplicationUserScreen
import com.example.optabasco.views.users.DashboardUserScreen

// Actividad principal de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el diseño de borde a borde en la interfaz
        setContent {
            MyApp() // Llama al contenido principal de la aplicación
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController() // Controlador de navegación para gestionar rutas en la aplicación

    // Configuración de NavHost para definir las rutas y las pantallas de la aplicación
    NavHost(navController = navController, startDestination = "login") {
        // Pantallas de acceso y registro
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile") { ProfileScreen(navController) }

        // Pantallas para el administrador
        composable("dashboardAdmin") { DashboardAdminScreen(navController) }
        composable("userMenuAdmin") { UserMenuAdminScreen(navController) }
        composable("userAdmin/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
            UserAdminScreen(navController, userId = userId)
        }
        composable("applicationMenuAdmin") { ApplicationMenuAdminScreen(navController) }
        composable("applicationAdmin/{applicationId}") { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getString("applicationId")?.toInt() ?: 0
            ApplicationAdminScreen(navController, applicationId = applicationId)
        }

        // Pantallas para el usuario general
        composable("dashboardUser") { DashboardUserScreen(navController) }
        composable("applicationUser/{applicationId}") { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getString("applicationId")?.toInt() ?: 0
            ApplicationUserScreen(navController, applicationId = applicationId)
        }
    }
}