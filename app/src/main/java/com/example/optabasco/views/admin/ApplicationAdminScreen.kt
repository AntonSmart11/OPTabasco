package com.example.optabasco.views.admin

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User
import com.example.optabasco.firebase.MyFirebaseAuth
import com.example.optabasco.views.CustomOutlinedSelectField
import com.example.optabasco.views.CustomSelectField
import com.example.optabasco.views.generatePdf
import com.example.optabasco.views.saveUserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

// Pantalla principal de la aplicación administrativa para gestionar solicitudes
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationAdminScreen(navController: NavController, applicationId: Int) {
    // Estado de scroll para manejar el desplazamiento de la pantalla
    val scrollState = rememberScrollState()

    // Contexto necesario para acceder a recursos de la aplicación
    val context = navController.context
    val contextDb = LocalContext.current

    // Define un alcance para lanzar corutinas (necesario para operaciones de base de datos)
    val coroutineScope = rememberCoroutineScope()

    // Obtiene instancias de los DAOs para acceder a las tablas de la base de datos
    val applicationDao = AppDatabase.getDatabase(contextDb).applicationDao()
    val userDao = AppDatabase.getDatabase(contextDb).userDao()

    // Variables de estado para almacenar datos de la solicitud y del usuario asociado
    val userId = remember { mutableStateOf(0) }
    val title = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val ranch = remember { mutableStateOf("") }
    val municipality = remember { mutableStateOf("") }
    val typeApplication = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val dateFull = remember { mutableStateOf("") }
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
    val tokenUser = remember { mutableStateOf("") }

    // Manejadores para el diálogo de confirmación de eliminación
    val showDialogDelete = remember { mutableStateOf(false) }

    // Campos para mostrar y modificar el estado de aprobación y confirmación de la solicitud
    val approvedField = remember { mutableStateOf("") }
    val statusApplicationField = remember { mutableStateOf("") }

    val enabledStatusField = remember { mutableStateOf(false) }

    // Lógica para habilitar o deshabilitar el estado según el valor de "aprobado"
    if (approvedField.value != "Aprobado") {
        enabledStatusField.value = false
        statusApplicationField.value = "No confirmada"
    } else {
        enabledStatusField.value = true

        if(statusApplicationField.value == "No confirmada") {
            statusApplicationField.value = "En proceso"
        }
    }

    // Carga de datos al iniciar la pantalla usando el applicationId
    LaunchedEffect(applicationId) {
        val applicationSelected = applicationId.let { applicationDao.getApplicationById(it) }

        applicationSelected?.let { app ->
            userId.value = app.usuario_id
            title.value = app.titulo
            street.value = app.calle
            ranch.value = app.coloniaRancheria
            municipality.value = app.municipio
            typeApplication.value = app.tipoSolicitud
            description.value = app.descripcion
            date.value = app.fecha.substring(0, 10) // Formato corto de la fecha
            dateFull.value = app.fecha
            approved.value = app.aprobada
            statusApplication.value = app.estadoSolicitud

            // Valor por defecto para la calle si no se proporciona
            if (app.calle == "") {
                street.value = "Sin nombre"
            }

            // Valores iniciales para los campos aprobados y estado de solicitud
            approvedField.value = approved.value
            statusApplicationField.value = statusApplication.value
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
            tokenUser.value = user.token
        }
    }

    // Opciones de selección para la aprobación y el estado de la solicitud
    val optionsApproved = listOf("En espera","No aprobado", "Aprobado")
    val optionsStatus = listOf("No confirmada", "En proceso", "Sin éxito", "Concluida")

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

    // Interfaz de usuario (UI) principal usando Scaffold
    Scaffold(
        // Barra superior con título y botón de navegación
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
        // Contenido principal de la pantalla
        content = {paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .verticalScroll(scrollState)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Información de la solicitud y el usuario (organizada en secciones)
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

                Text(
                    text = "Usuario",
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
                            text = "Nombre del usuario: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "${name.value} ${lastPatern.value} ${lastMatern.value}",
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
                            text = "Correo electrónico: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = email.value,
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
                            text = "Número telefónico: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = number.value,
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
                            text = "CURP: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = curp.value,
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    }

                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Solicitud",
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
                            text = "Título: ",
                            color = colorResource(R.color.pantone490),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = title.value,
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

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Seguimiento",
                    color = colorResource(R.color.pantone490),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomSelectField(
                        label = "Confirma si se aprueba la solicitud",
                        selectOption = approvedField.value,
                        options = optionsApproved,
                        onOptionSelected = { approvedField.value = it }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    CustomSelectField(
                        label = "Estado de la solicitud",
                        selectOption = statusApplicationField.value,
                        options = optionsStatus,
                        onOptionSelected = { statusApplicationField.value = it },
                        enabled = enabledStatusField.value
                    )
                }

                Button(
                    onClick = {
                        // Lógica para actualizar el estado de la solicitud y de aprobación
                        if (approvedField.value != "Aprobado") {
                            enabledStatusField.value = false
                            statusApplicationField.value = "No confirmada"
                        } else {
                            enabledStatusField.value = true

                            if(statusApplicationField.value == "No confirmada") {
                                statusApplicationField.value = "En proceso"
                            }
                        }

                        val editApplication = Application(
                            applicationId,
                            userId.value,
                            title.value,
                            street.value,
                            ranch.value,
                            municipality.value,
                            typeApplication.value,
                            description.value,
                            dateFull.value,
                            approvedField.value,
                            statusApplicationField.value
                        )

                        coroutineScope.launch {
                            val rowsAffected = applicationDao.updateApplication(editApplication)

                            approved.value = approvedField.value

                            if (rowsAffected > 0) {
                                // Mensaje de éxito en la actualización
                                Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_LONG).show()

                                if (!tokenUser.value.isNullOrEmpty()) {
                                    var titleNotification = ""
                                    var messageNotification = ""

                                    if (approvedField.value == "Aprobado") {
                                        titleNotification = "Aprobada"
                                    } else {
                                        titleNotification = "No Aprobada"
                                        messageNotification = "no"
                                    }

                                    sendTokenNotification(
                                        token = tokenUser.value,
                                        title = "Solicitud $titleNotification",
                                        message = "${name.value} ${lastPatern.value} ${lastMatern.value}, su solicitud '${title.value}' $messageNotification ha sido aprobada",
                                        context = contextDb
                                    )
                                }
                            } else {
                                // Mensaje de error en la actualización
                                Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
                            }
                        }


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
                        text = "Guardar",
                        color = colorResource(R.color.pantone490),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

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

                // Botón para eliminar la solicitud, muestra un diálogo de confirmación
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
                        text = "Eliminar",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )

    // Diálogo de confirmación para eliminar la solicitud
    if (showDialogDelete.value) {
        DeleteApplicationDialog(
            onDismiss = { showDialogDelete.value = false },
            applicationId,
            navController
        )
    }
}

// Función composable para mostrar el cuadro de eliminación de una solicitud
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteApplicationDialog(
    onDismiss: () -> Unit,
    applciationId: Int,
    navController: NavController
) {
    // Obtener el contexto de la base de datos y el DAO de usuario
    val contextDb = LocalContext.current
    val database = AppDatabase.getDatabase(contextDb)
    val applicationDao = database.applicationDao()

    // Mostrar el cuadro
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

                    // Mensaje de confirmación
                    Text(
                        text = "¿Estás seguro de que deseas eliminar esta solicitud?",
                        color = colorResource(R.color.pantone468),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Botones de acción
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón para cancelar
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

                        // Botón para eliminar cuenta
                        TextButton(
                            onClick = {
                                // Llamada a la función de eliminación de cuenta en un hilo de fondo
                                CoroutineScope(Dispatchers.IO).launch {
                                    val application = applicationDao.getApplicationById(applciationId)

                                    if (application != null) {
                                        val deleted = applicationDao.deleteApplication(application)

                                        if (deleted > 0) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(contextDb, "Solicitud eliminada exitosamente", Toast.LENGTH_LONG).show()
                                                onDismiss()

                                                // Redirigir al menu anterior
                                                navController.popBackStack()
                                            }

                                        } else {
                                            // Mensaje de validación por si hubo un error
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

fun sendTokenNotification(
    token: String,
    title: String,
    message: String,
    context: Context
) {
    // Lanza un coroutine para obtener el token en segundo plano
    GlobalScope.launch(Dispatchers.Main) {
        val accessToken = withContext(Dispatchers.IO) {
            // Obtén el access token en segundo plano
            MyFirebaseAuth.getAccessToken(context)
        }

        if (accessToken != null) {
            // Si el access token es válido, procede con el envío de la notificación
            sendNotificationToDevice(token, accessToken, title, message, context)
        } else {
            // Si no se obtuvo un access token, muestra un mensaje de error
            Toast.makeText(context, "Error al obtener el token", Toast.LENGTH_LONG).show()
        }
    }
}

private fun sendNotificationToDevice(
    token: String,
    auth: String,
    title: String,
    message: String,
    context: Context
) {
    val url = "https://fcm.googleapis.com/v1/projects/optabasco-865a5/messages:send"
    val requestQueue = Volley.newRequestQueue(context)

    val jsonBody = JSONObject().apply {
        put("message", JSONObject().apply {
            put("token", token)
            put("notification", JSONObject().apply {
                put("title", title)
                put("body", message)
            })
        })
    }

    val request = object : JsonObjectRequest(
        Method.POST, url, jsonBody,
        Response.Listener { response ->
            Toast.makeText(context, "Notificación enviada exitosamente", Toast.LENGTH_LONG).show()
        },
        Response.ErrorListener { error ->
            Toast.makeText(context, "Error al enviar notificación: ${error.message}", Toast.LENGTH_LONG).show()
        }
    ) {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            val accessToken = auth

            headers["Authorization"] = "Bearer $accessToken"
            headers["Content-Type"] = "application/json"
            return headers
        }
    }

    requestQueue.add(request)
}