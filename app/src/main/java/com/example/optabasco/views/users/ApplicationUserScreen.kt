package com.example.optabasco.views.users

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User
import com.example.optabasco.views.generatePdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationUserScreen(navController: NavController, applicationId: Int) {
    // Estado para manejar el desplazamiento en la pantalla
    val scrollState = rememberScrollState()

    // Contexto para usar la base de datos y el contexto de la aplicación
    val context = navController.context
    val contextDb = LocalContext.current

    // Acceder al DAO de la base de datos para las solicitudes
    val userDao = AppDatabase.getDatabase(contextDb).userDao()
    val applicationDao = AppDatabase.getDatabase(contextDb).applicationDao()

    // Variables para almacenar la información de la solicitud
    val userId = remember { mutableStateOf(0) }
    val title = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val ranch = remember { mutableStateOf("") }
    val municipality = remember { mutableStateOf("") }
    val typeApplication = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val approved = remember { mutableStateOf("") }
    val statusApplication = remember { mutableStateOf("") }

    // Información del usuario asociado
    val name = remember { mutableStateOf("") }
    val lastPatern = remember { mutableStateOf("") }
    val lastMatern = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val curp = remember { mutableStateOf("") }
    val levelUser = remember { mutableStateOf(0) }

    // Estado para manejar la visibilidad del diálogo de eliminación
    val showDialogDelete = remember { mutableStateOf(false) }

    // Lanzar una corutina para cargar la solicitud cuando la pantalla se inicie
    LaunchedEffect(applicationId) {
        val applicationSelected = applicationId.let { applicationDao.getApplicationById(it) }

        // Si se encuentra la solicitud, se carga la información en las variables de estado
        applicationSelected?.let { app ->
            userId.value = app.usuario_id
            title.value = app.titulo
            street.value = app.calle
            ranch.value = app.coloniaRancheria
            municipality.value = app.municipio
            typeApplication.value = app.tipoSolicitud
            description.value = app.descripcion
            date.value = app.fecha.substring(0, 10)
            approved.value = app.aprobada
            statusApplication.value = app.estadoSolicitud

            // Si la calle está vacía, se muestra un texto predeterminado
            if (app.calle == "") {
                street.value = "Sin nombre"
            }

            // Obtiene el usuario asociado con la solicitud
            val userSelected = userDao.getUserById(userId.value)

            userSelected?.let { user ->
                name.value = user.nombre
                lastPatern.value = user.paterno
                lastMatern.value = user.materno
                email.value = user.correo
                number.value = user.telefono
                curp.value = user.curp
                levelUser.value = user.nivel
            }
        }
    }

    // Lanzador para crear un PDF a partir de la solicitud seleccionada
    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            generatePdf(context, it,
                Application(
                    applicationId,
                    userId.value,
                    title.value,
                    street.value,
                    ranch.value,
                    municipality.value,
                    typeApplication.value,
                    description.value,
                    date.value,
                    approved.value,
                    statusApplication.value
                ),
                User(
                    userId.value,
                    name.value,
                    lastPatern.value,
                    lastMatern.value,
                    email.value,
                    number.value,
                    curp.value,
                    "",
                    levelUser.value,
                    ""
                )
            )
        }
    }

    Scaffold(
        // Barra superior con un botón para regresar
        topBar = { CenterAlignedTopAppBar(
            title = { Text("") },
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
        )

        },
        // Contenido de la pantalla
        content = { paddingValues ->
            // Datos del formulario de las solcitudes
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .verticalScroll(scrollState)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = title.value,
                    color = colorResource(R.color.pantone490),
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(1.dp))

                Text(
                    text = date.value,
                    color = colorResource(R.color.pantone490),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(Modifier.height(20.dp))

                // Sección de dirección de la solicitud
                Text(
                    text = "Dirección",
                    color = colorResource(R.color.pantone490),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 10.dp)
                        .background(colorResource(R.color.pantone465))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Calle: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = street.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Colonia/Ranchería: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = ranch.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Municipio: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = municipality.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Sección de datos adicionales de la solicitud
                Text(
                    text = "Datos",
                    color = colorResource(R.color.pantone490),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 10.dp)
                        .background(colorResource(R.color.pantone465))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Tipo de solicitud: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = typeApplication.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Descripción: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = description.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }

                }

                Spacer(modifier = Modifier.height(30.dp))

                // Estado de aprobación y estado de la solicitud
                Text(
                    text = approved.value,
                    color = colorResource(R.color.pantone490),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = statusApplication.value,
                    color = colorResource(R.color.pantone490),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(25.dp))

                if (approved.value == "Aprobado") {
                    // Botón para generar PDF de la solicitud
                    Button(
                        onClick = {
                            pdfLauncher.launch("solicitud_${title.value}.pdf") // Lanza el Intent para seleccionar ubicación
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                            .padding(bottom = 32.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pantone465)
                        )
                    ) {
                        Text(
                            text = "Generar PDF",
                            color = colorResource(R.color.pantone490),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Botón para eliminar la solicitud
                Button(
                    onClick = {
                        showDialogDelete.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 90.dp)
                        .padding(bottom = 32.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.pantone1805)
                    )
                ) {
                    Text(
                        text = "Eliminar solicitud",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

    )

    // Mostrar el cuadro de confirmación de eliminación si el estado es verdadero
    if (showDialogDelete.value) {
        DeleteApplicationDialog(
            onDismiss = { showDialogDelete.value = false },
            applicationId,
            navController
        )
    }
}

// Alert Dialog Solicitud
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteApplicationDialog(
    onDismiss: () -> Unit,
    applicationId: Int,
    navController: NavController
) {

    // Se obtiene el contexto, la base de datos y el DAO de las solicitudes
    val contextDb = LocalContext.current
    val database = AppDatabase.getDatabase(contextDb)
    val applicationDao = database.applicationDao()

    // Diálogo de confirmación para eliminar la solicitud
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true
        ),

        // Modificar el contenido del diálogo con Surface para cambiar colores
        content = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colorResource(R.color.pantone490),
                modifier = Modifier.padding(16.dp),
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Eliminar solicitud",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "¿Estás seguro de que deseas eliminar esta solicitud?",
                        color = colorResource(R.color.pantone468),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón de cancelar la eliminación
                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = colorResource(R.color.pantone7420),
                                contentColor = colorResource(R.color.pantone7420)
                            ),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(
                                "Cancelar",
                                color = colorResource(R.color.pantone468),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón para confirmar la eliminación
                        TextButton(
                            onClick = {
                                // Llamar a la función de cambiar contraseña
                                CoroutineScope(Dispatchers.IO).launch {
                                    val application = applicationDao.getApplicationById(applicationId)

                                    if (application != null) {
                                        val deleted = applicationDao.deleteApplication(application)

                                        if (deleted > 0) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(contextDb, "Solicitud eliminada exitosamente", Toast.LENGTH_LONG).show()
                                                onDismiss()

                                                // Redirigir a menú
                                                navController.popBackStack()
                                            }

                                        } else {
                                            Toast.makeText(contextDb, "Hubo un error...", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = colorResource(R.color.pantone1805),
                                contentColor = colorResource(R.color.pantone1805)
                            ),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(
                                "Eliminar",
                                color = colorResource(R.color.pantone468),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )
}