package com.example.optabasco

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.optabasco.ui.theme.OPTabascoTheme
import com.example.optabasco.views.LoginScreen
import com.example.optabasco.views.RegisterScreen
import com.example.optabasco.views.admin.DashboardAdminScreen
import com.example.optabasco.views.admin.ProfileAdminScreen
import com.example.optabasco.views.admin.UserAdminScreen
import com.example.optabasco.views.admin.UserMenuAdminScreen

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
        composable("dashboardAdmin") { DashboardAdminScreen(navController) }
        composable("profileAdmin") { ProfileAdminScreen(navController) }
        composable("UserMenuAdmin") { UserMenuAdminScreen(navController) }
        composable("UserAdmin/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
            UserAdminScreen(navController, userId = userId)
        }
    }
}