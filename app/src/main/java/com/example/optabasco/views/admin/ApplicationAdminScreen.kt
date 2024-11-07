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
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.Application
import com.example.optabasco.views.CustomOutlinedSelectField
import com.example.optabasco.views.CustomSelectField
import com.example.optabasco.views.generatePdf
import com.example.optabasco.views.saveUserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationAdminScreen(navController: NavController, applicationId: Int) {
    val scrollState = rememberScrollState()

    val context = navController.context
    val contextDb = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            generatePdf(context, it)
        }
    }

    val applicationDao = AppDatabase.getDatabase(contextDb).applicationDao()
    val userDao = AppDatabase.getDatabase(contextDb).userDao()

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

    val name = remember { mutableStateOf("") }
    val lastPatern = remember { mutableStateOf("") }
    val lastMatern = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val number = remember { mutableStateOf("") }
    val curp = remember { mutableStateOf("") }
    val levelUser = remember { mutableStateOf(0) }

    val showDialogDelete = remember { mutableStateOf(false) }

    val approvedField = remember { mutableStateOf("") }
    val statusApplicationField = remember { mutableStateOf("") }

    val enabledStatusField = remember { mutableStateOf(false) }

    if (approvedField.value != "Aprobado") {
        enabledStatusField.value = false
        statusApplicationField.value = "No confirmada"
    } else {
        enabledStatusField.value = true

        if(statusApplicationField.value == "No confirmada") {
            statusApplicationField.value = "En proceso"
        }
    }

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
            date.value = app.fecha.substring(0, 10)
            dateFull.value = app.fecha
            approved.value = app.aprobada
            statusApplication.value = app.estadoSolicitud

            if (app.calle == "") {
                street.value = "Sin nombre"
            }

            approvedField.value = approved.value
            statusApplicationField.value = statusApplication.value
        }

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

    val optionsApproved = listOf("En espera","No aprobado", "Aprobado")
    val optionsStatus = listOf("No confirmada", "En proceso", "Sin éxito", "Concluida")

    Scaffold(
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

                            if (rowsAffected > 0) {
                                //Mostrar mensaje
                                Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_LONG).show()
                            } else {
                                //Mostrar mensaje
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