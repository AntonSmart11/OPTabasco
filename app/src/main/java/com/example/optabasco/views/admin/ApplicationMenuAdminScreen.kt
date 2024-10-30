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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.Application

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationMenuAdminScreen(navController: NavController) {
    val contextDb = LocalContext.current
    val applicationDao = AppDatabase.getDatabase(contextDb).applicationDao()

    val applications = remember { mutableStateOf<List<Application>>(emptyList()) }
    val searchQuery = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        applications.value = applicationDao.getAllApplicationsByDate()
    }

    val filteredApplications = applications.value.filter {
        it.titulo.contains(searchQuery.value, ignoreCase = true) ||
        it.calle.contains(searchQuery.value, ignoreCase = true) ||
        it.coloniaRancheria.contains(searchQuery.value, ignoreCase = true) ||
        it.municipio.contains(searchQuery.value, ignoreCase = true) ||
        it.tipoSolicitud.contains(searchQuery.value, ignoreCase = true) ||
        it.fecha.contains(searchQuery.value, ignoreCase = true) ||
        it.estadoSolicitud.contains(searchQuery.value, ignoreCase = true) ||
        it.aprobada.contains(searchQuery.value, ignoreCase = true)
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("Usuarios", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
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
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = {
                        Text(
                            "Buscar solicitud",
                            color = colorResource(R.color.pantone490)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.pantone490),
                        unfocusedTextColor = colorResource(R.color.pantone490),
                        focusedBorderColor = colorResource(R.color.pantone490),
                        unfocusedBorderColor = colorResource(R.color.pantone490),
                        cursorColor = colorResource(R.color.pantone490),
                    ),
                    textStyle = TextStyle(
                        color = colorResource(R.color.pantone490),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(16.dp),
                    maxLines = 1,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                )

                if (filteredApplications.isEmpty()){
                    // Muestra un mensaje si no hay solicitudes
                    Text("No hay solicitudes disponibles", color = colorResource(R.color.pantone490), fontWeight = FontWeight.Bold)
                } else {
                    // Mostrar la lista de solicitudes usando LazyColumn
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredApplications) { app ->
                            ApplicationItem(app, navController)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ApplicationItem(application: Application, navController: NavController) {

    val applicationId = application.id

    Spacer(modifier = Modifier.height(10.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(4.dp))
            .background(colorResource(R.color.pantone465))
            .padding(16.dp)
            .clickable {
                navController.navigate("applicationUser/$applicationId")
            }
    ) {
        Text(
            text = application.titulo,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = colorResource(R.color.pantone490),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tipo: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.pantone490),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = application.tipoSolicitud,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.pantone490),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Descripción: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.pantone490),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = application.descripcion,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = colorResource(R.color.pantone490),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = application.aprobada,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.pantone490)
                )
                Text(
                    text = application.estadoSolicitud,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.pantone490)
                )
            }
        }

    }
}