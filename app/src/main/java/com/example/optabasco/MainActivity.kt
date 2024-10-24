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
import com.example.optabasco.views.admin.UserAdminScreen
import com.example.optabasco.views.admin.UserMenuAdminScreen
import com.example.optabasco.views.users.ApplicationUserScreen
import com.example.optabasco.views.users.DashboardUserScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("profile") { ProfileScreen(navController) }

        composable("dashboardAdmin") { DashboardAdminScreen(navController) }
        composable("userMenuAdmin") { UserMenuAdminScreen(navController) }
        composable("userAdmin/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
            UserAdminScreen(navController, userId = userId)
        }

        composable("dashboardUser") { DashboardUserScreen(navController) }
        composable("applicationUser/{applicationId}") { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getString("applicationId")?.toInt() ?: 0
            ApplicationUserScreen(navController, applicationId = applicationId)
        }
    }
}