package com.example.optabasco.views.users

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.Application
import com.example.optabasco.views.CustomOutlinedSelectField
import com.example.optabasco.views.CustomOutlinedTextField
import com.example.optabasco.views.getUserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardUserScreen(navController: NavController) {
    // Estado guardado para scroll de pantalla
    val scrollState = rememberScrollState()

    // Estado del drawer (menú lateral)
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    // Ámbito de corutinas
    val scope = rememberCoroutineScope()

    // Contexto de la base de datos local
    val contextDb = LocalContext.current

    // ModalNavigationDrawer crea un menú lateral deslizable
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
                // Título del menú lateral
                Text(
                    text = "OP Tabasco",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = colorResource(R.color.pantone468),
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón para cerrar sesión
                Button(
                    onClick = {
                        // Limpiar sesión de usuario
                        clearUserSession(contextDb)

                        // Navegar a la pantalla de login y limpiar el historial
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
        // Aquí se carga el contenido principal de la pantalla
        ContentDashboardUser(scope, drawerState, scrollState, navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentDashboardUser(scope: CoroutineScope, drawerState: DrawerState, scrollState: ScrollState, navController: NavController) {
    val contextDb = LocalContext.current
    val applicationDao = AppDatabase.getDatabase(contextDb).applicationDao()
    val userDao = AppDatabase.getDatabase(contextDb).userDao()

    // Estado que guarda las solicitudes obtenidas de la base de datos
    val applications = remember { mutableStateOf<List<Application>>(emptyList()) }

    // Estado para mostrar el diálogo de agregar solicitud
    val showDialogAdd = remember { mutableStateOf(false) }

    // ID del usuario
    val userId = remember { mutableStateOf(0) }

    // Carga de datos del usuario y sus solicitudes cuando se monta la pantalla
    LaunchedEffect(Unit) {
        val userEmail = getUserSession(contextDb)

        if (userEmail != null) {
            val user = userDao.getUserByEmail(userEmail)

            if (user != null) {
                // Asignar el ID del usuario
                userId.value = user.id

                // Obtener solicitudes del usuario
                applications.value = applicationDao.getApplicationByUserId(user.id)
            }
        }
    }

    // Estructura de la pantalla que contiene TopBar y el contenido principal
    Scaffold(
        // Barra superior con el título y el ícono del menú
        topBar = { CenterAlignedTopAppBar(
            title = { Text("Solicitudes", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
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
                // Icono del perfil en la parte derecha de la barra superior
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = "Perfil de usuario",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            navController.navigate("profile") // Navegar a la pantalla de perfil
                        }
                        .padding(2.dp)
                )
            }
        ) },
        content = { paddingValues ->
            // Contenido principal donde se muestra la lista de solicitudes
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (applications.value.isEmpty()) {
                    // Si no hay solicitudes, mostrar un mensaje
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("No hay solicitudes creadas", color = colorResource(R.color.pantone490), fontWeight = FontWeight.Bold)
                } else {
                    // Si hay solicitudes, mostrar la lista
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(applications.value) { application ->
                            ApplicationItem(application, navController) // Mostrar cada solicitud
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            // Botón flotante para agregar una nueva solicitud
            FloatingActionButton(
                onClick = { showDialogAdd.value = true },
                containerColor = colorResource(R.color.pantone490)
            ) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = "Agregar",
                    tint = colorResource(R.color.pantone468)
                )
            }
        }
    )

    // Si se solicita agregar una nueva solicitud, mostrar el diálogo
    if (showDialogAdd.value) {
        AddApplicationDialog(
            onDismiss = { showDialogAdd.value = false },
            userId.value,
            applications
        )
    }
}

// Estructura de cada solicitud que se muestra en la página
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
                // Navegar a los detalles de la solicitud
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

        // Mostrar detalles de la solicitud (tipo y descripción)
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

            // Estado de la solicitud
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

// Composable para agregar una nueva solicitud
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddApplicationDialog(
    onDismiss: () -> Unit,
    userId: Int,
    applications: MutableState<List<Application>>
) {
    // Variables para los campos del cuadro de diálogo
    val titleField = remember { mutableStateOf("") }
    val streetField = remember { mutableStateOf("") }
    val ranchField = remember { mutableStateOf("") }
    val municipalityField = remember { mutableStateOf("") }
    val typeApplicationField = remember { mutableStateOf("") }
    val descriptionField = remember { mutableStateOf("") }

    // Se crea una coroutina para funciones asíncronas
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val contextDb = LocalContext.current
    val database = AppDatabase.getDatabase(contextDb)
    val applicationDao = database.applicationDao()

    val scrollState = rememberScrollState()
    var showInfo by remember { mutableStateOf(false) }

    // Variable con las opciones de tipo de solicitudes que hay
    val options = listOf("Constitución", "Mantenimiento", "Permisos", "Licitación", "Inspección")

    // Explicación de las solicitudes
    val infoText = "Tipo de Solicitudes\n\nConstitución: Para construcciones gubernamentales (escuelas, paradas, etc...)\n\nMantenimiento: Para obras ya hechas\n\nPermisos: Para construcciones o remodelación\n\nLicitación: Para participación de concursos públicos\n\nInspección: Para revisión de un proyecto en curso"

    // Cuadro de diálogo para agregar una nueva solicitud
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
                Column(
                    modifier = Modifier.padding(16.dp).verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Agregar nueva solicitud",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campos para agregar datos de una solicitud
                    CustomOutlinedTextField(
                        titleField,
                        "Título*"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        streetField,
                        "Calle"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        ranchField,
                        "Colonia o Ranchería*"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        municipalityField,
                        "Municipio*"
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomOutlinedSelectField(
                            selectedOption = typeApplicationField,
                            label = "Tipo de Solicitud*",
                            options = options,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(onClick = { showInfo = !showInfo }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Información",
                                tint = colorResource(R.color.pantone468)
                            )
                        }
                    }

                    // Si showInfo es verdadero se muestra información extra
                    if (showInfo) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorResource(R.color.pantone468),
                                contentColor = colorResource(R.color.pantone490)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = infoText,
                                color = colorResource(R.color.pantone490),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        descriptionField,
                        "Descripción*",
                        maxLines = 6,
                        modifier = Modifier.heightIn(min = 150.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Botón de cancelar
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

                        // Botón para agregar una nueva solicitud
                        TextButton(
                            onClick = {
                                // Valida los posibles errores
                                val validationError = validateFieldsCreateApplication(
                                    titleField.value,
                                    streetField.value,
                                    ranchField.value,
                                    municipalityField.value,
                                    typeApplicationField.value,
                                    descriptionField.value
                                )

                                if (validationError == null) {
                                    val currentDateString = getCurrentDate()

                                    coroutineScope.launch {
                                        // Llama a la función de inserción dentro de una corutina
                                        val application = Application(
                                            usuario_id = userId,
                                            titulo = titleField.value,
                                            calle = streetField.value,
                                            coloniaRancheria = ranchField.value,
                                            municipio = municipalityField.value,
                                            tipoSolicitud = typeApplicationField.value,
                                            descripcion = descriptionField.value,
                                            fecha = currentDateString,
                                            aprobada = "En espera",
                                            estadoSolicitud = "No confirmada"
                                        )

                                        // Inserta la solicitud
                                        insertApplication(context, application)

                                        // Recarga la lista de aplicaciones
                                        applications.value = applicationDao.getApplicationByUserId(userId)

                                        // Cierra el cuadro de diálogo
                                        onDismiss()
                                    }
                                } else {
                                    // Mostrar el mensaje de error
                                    Toast.makeText(context, validationError, Toast.LENGTH_LONG).show()
                                }
                            },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = colorResource(R.color.pantone465),
                                contentColor = colorResource(R.color.pantone465)
                            ),
                            modifier = Modifier.width(120.dp)
                        ) {
                            Text(
                                "Guardar",
                                color = colorResource(R.color.pantone490),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )
}

// Función para validar los campos del formulario
fun validateFieldsCreateApplication(
    title: String,
    street: String,
    ranch: String,
    municipality: String,
    typeApplication: String,
    description: String
): String? {
    // Validar que ningún campo esté vacío
    if (
        title.isBlank() ||
        ranch.isBlank() ||
        municipality.isBlank() ||
        typeApplication.isBlank() ||
        description.isBlank()
    ) {
        // Si algún campo está vacío retornar mensaje
        return "Los campos con * deben estar llenos"
    }

    // Si todo está bien retornar null (nada vacío)
    return null
}

// Función para insertar una solicitud
suspend fun insertApplication(context: Context, application: Application) {
    val database = AppDatabase.getDatabase(context)
    val applicationDao = database.applicationDao()

    // Llama al insert para insertar una solicitud
    applicationDao.insert(application)

    // Mostrar el mensaje
    Toast.makeText(context, "Creado correctamente", Toast.LENGTH_LONG).show()
}

// Función para obtener la fecha actual
fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Puedes ajustar el formato de la fecha
    val currentDate = Date()
    return dateFormat.format(currentDate)
}

// Función para eliminar los datos de sesión del usuario actual
fun clearUserSession(context: Context) {
    val sharedPref: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.clear() // Elimina todos los datos guardados de la sesión
    editor.apply()
}