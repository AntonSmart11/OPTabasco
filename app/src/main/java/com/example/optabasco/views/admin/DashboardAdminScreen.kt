package com.example.optabasco.views.admin

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.views.users.clearUserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun DashboardAdminScreen(navController: NavController) {
    // ScrollState para habilitar desplazamiento vertical en la columna
    val scrollState = rememberScrollState()

    // DrawerState para manejar el estado del menú lateral
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Contexto de la aplicación, necesario para acciones como la limpieza de sesión
    val contextDb = LocalContext.current

    // Configuración del menú lateral y su contenido
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(colorResource(R.color.pantone490))
                    .padding(26.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "OP Tabasco",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = colorResource(R.color.pantone468),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        // Llamada para limpiar sesión de usuario
                        clearUserSession(contextDb)

                        // Navegar a pantalla de login y eliminar el historial de navegación
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.pantone7420)
                    )
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        // Llama a la función que muestra el contenido principal del dashboard
        ContentDashboardAdmin(scope, drawerState, scrollState, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDashboardAdmin(scope: CoroutineScope, drawerState: DrawerState, scrollState: ScrollState, navController: NavController) {
    // Estructura principal con barra superior y contenido en el dashboard
    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.pantone490),
                titleContentColor = colorResource(R.color.pantone468)
            ),
            navigationIcon = {
                // Botón de menú que abre el drawer al hacer clic
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "Menú",
                        tint = colorResource(R.color.pantone468)
                    )
                }
            },
            actions = {
                // Imagen de perfil que navega a la pantalla de perfil al hacer clic
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "Perfil de usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate("profile")
                        }
                        .padding(2.dp)
                )
            }
        )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .verticalScroll(scrollState)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Muestra el encabezado con el título "Panel de Control"
                CustomSquareWithText()

                Spacer(Modifier.height(20.dp))

                // Pregunta al usuario
                Text(
                    text = "¿Qué hacer hoy?",
                    color = colorResource(R.color.pantone490),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Botón que redirige a la pantalla de solicitudes
                BoxButton(R.drawable.application, "Solicitudes", R.color.pantone465, navController, "applicationMenuAdmin")

                Spacer(modifier = Modifier.height(30.dp))

                // Botón que redirige a la pantalla de usuarios
                BoxButton(R.drawable.users, "Usuarios", R.color.pantone465, navController, "userMenuAdmin")
            }
        }
    )
}

// Función composable para mostrar un botón de caja con imagen y texto
@Composable
fun BoxButton(image: Int, title:String, color: Int, navController: NavController, destination: String) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(18.dp))
            .background(colorResource(color))
            .clickable {
                navController.navigate(destination)
            }
    ) {
        // Imagen dentro del botón
        Icon(
            painter = painterResource(id = image),
            tint = colorResource(R.color.pantone468),
            contentDescription = title,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        // Título en la parte inferior del botón
        Text(
            text = title,
            color = colorResource(R.color.pantone490),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
    }
}

// Función composable para el encabezado del panel de control
@Composable
fun CustomSquareWithText() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(colorResource(R.color.pantone490))
            .padding(bottom = 25.dp),
        contentAlignment = Alignment.Center
    ) {
        // Texto del encabezado
        Text(
            text = "Panel de Control",
            color = colorResource(R.color.pantone468),
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold)
    }
}