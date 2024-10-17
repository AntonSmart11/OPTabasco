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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optabasco.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun DashboardAdminScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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


            }
        }
    ) {
        ContentDashboardAdmin(scope, drawerState, scrollState, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDashboardAdmin(scope: CoroutineScope, drawerState: DrawerState, scrollState: ScrollState, navController: NavController) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.pantone490),
                titleContentColor = colorResource(R.color.pantone468)
            ),
            navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(
                        painter = painterResource(R.drawable.menu),
                        contentDescription = "Menú",
                        tint = colorResource(R.color.pantone468)
                    )
                }
            },
            actions = {
                // Imagen del usuario en el borde derecho del TopBar
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "Perfil de usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate("profileAdmin")
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
                CustomSquareWithText()

                Spacer(Modifier.height(20.dp))

                // Pregunta en el dashboard
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
                BoxButton(R.drawable.application, "Solicitudes", R.color.pantone465, navController, "UserMenuAdmin")

                Spacer(modifier = Modifier.height(30.dp))
                BoxButton(R.drawable.users, "Usuarios", R.color.pantone465, navController, "UserMenuAdmin")
            }
        }
    )
}

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
            color = colorResource(R.color.pantone468),
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
        Text(
            text = "Panel de Control",
            color = colorResource(R.color.pantone468),
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold)
    }
}