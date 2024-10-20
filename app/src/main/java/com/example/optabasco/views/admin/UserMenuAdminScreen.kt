package com.example.optabasco.views.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserMenuAdminScreen(navController: NavController) {
    val contextDb = LocalContext.current
    val userDao = AppDatabase.getDatabase(contextDb).userDao()

    val users = remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(Unit) {
        users.value = userDao.getAllUsers()
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("Usuarios", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)},
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.pantone490),
                titleContentColor = colorResource(R.color.pantone468)
            ),
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Flecha atrás",
                        tint = colorResource(R.color.pantone468)
                    )
                }
            }
        ) },
        content = {paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (users.value.isEmpty()) {
                    // Muestra un mensaje si no hay usuarios
                    Text("No hay usuarios disponibles", color = colorResource(R.color.pantone490), fontWeight = FontWeight.Bold)
                } else {
                    // Mostrar la lista de usuarios usando LazyColumn
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(users.value) { user ->
                            UserItem(user, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserItem(user: User, navController: NavController) {
    var userType = "Usuario"

    if (user.nivel == 1) {
        userType = "Admin"
    }

    val userId = user.id

    Spacer(modifier = Modifier.height(10.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(4.dp))
            .background(colorResource(R.color.pantone465))
            .padding(16.dp)
            .clickable {
                navController.navigate("userAdmin/$userId")
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Usuario: ${user.nombre} ${user.paterno} ${user.materno}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.pantone490),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Correo: ${user.correo}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.pantone490),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "CURP: ${user.curp}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.pantone490),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = userType,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = colorResource(R.color.pantone490)
            )
        }

    }
}