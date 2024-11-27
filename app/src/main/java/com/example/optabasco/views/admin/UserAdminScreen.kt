package com.example.optabasco.views.admin

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.optabasco.database.AuthService
import com.example.optabasco.database.models.User
import com.example.optabasco.views.CustomOutlinedTextField
import com.example.optabasco.views.CustomTextField
import com.example.optabasco.views.getUserSession
import com.example.optabasco.views.validateFieldsCreateUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAdminScreen(navController: NavController, userId: Int) {
    // Recordar el estado del desplazamiento vertical de la pantalla
    val scrollState = rememberScrollState()

    // Obtener el contexto de la navegación y de la base de datos
    val context = navController.context
    val contextDb = LocalContext.current

    // Recordar el alcance de las corrutinas para lanzar tareas en segundo plano
    val coroutineScope = rememberCoroutineScope()

    // Obtener una instancia de UserDao para acceder a la base de datos de usuarios
    val userDao = AppDatabase.getDatabase(contextDb).userDao()

    // Variables de estado para almacenar los datos del usuario y actualizar la interfaz
    val nameField = remember { mutableStateOf("") }
    val lastPaternField = remember { mutableStateOf("") }
    val lastMaternField = remember { mutableStateOf("") }
    val emailField = remember { mutableStateOf("") }
    val numberField = remember { mutableStateOf("") }
    val curpField = remember { mutableStateOf("") }
    val levelUser = remember { mutableStateOf(2) }
    val passwordUser = remember { mutableStateOf("") }
    val tokenUser = remember { mutableStateOf("") }

    // Variables de estado para controlar la visibilidad de los diálogos
    val showDialogPassword = remember { mutableStateOf(false) }
    val showDialogDelete = remember { mutableStateOf(false) }

    // Cargar los datos del usuario cuando se pasa el userId
    LaunchedEffect(userId) {
        val userSelected = userId.let { userDao.getUserById(it) }

        // Actualizar los campos con la información del usuario seleccionado
        userSelected?.let { user ->
            // Asignar los valores del usuario a los campos de texto
            nameField.value = user.nombre
            lastPaternField.value = user.paterno
            lastMaternField.value = user.materno
            emailField.value = user.correo
            numberField.value = user.telefono
            curpField.value = user.curp
            levelUser.value = user.nivel
            passwordUser.value = user.contrasena
            tokenUser.value = user.token
        }
    }

    // Definir el título de la pantalla según el nivel de usuario
    val title = if (levelUser.value == 1) {
        "Admin"
    } else {
        "Usuario"
    }

    // Estructura de la pantalla principal con Scaffold para el AppBar y contenido
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
            },
        ) },
        content = { paddingValues ->
            // Contenedor principal de la pantalla
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.pantone468))
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                // Mostrar el título de la pantalla
                Text(
                    text = title,
                    color = colorResource(R.color.pantone490),
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(20.dp))

                // Campos de entrada personalizados para cada atributo del usuario
                CustomTextField(
                    valueState = nameField,
                    label = "Nombre(s)"
                )
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    valueState = lastPaternField,
                    label = "Apellido paterno"
                )
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    valueState = lastMaternField,
                    label = "Apellido materno"
                )
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    valueState = numberField,
                    label = "Teléfono"
                )
                Spacer(modifier = Modifier.height(20.dp))

                CustomTextField(
                    valueState = curpField,
                    label = "CURP",
                )
                Spacer(modifier = Modifier.height(30.dp))

                CustomTextField(
                    valueState = emailField,
                    label = "Correo electrónico"
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Botón para guardar cambios en el usuario
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val validationError = validateFieldsCreateUser(
                                name = nameField.value,
                                lastPatern = lastPaternField.value,
                                lastMatern = lastMaternField.value,
                                email = emailField.value,
                                number = numberField.value,
                                curp = curpField.value,
                            )

                            if (validationError == null) {
                                val editUser = User(
                                    id = userId,
                                    nombre = nameField.value,
                                    paterno = lastPaternField.value,
                                    materno = lastMaternField.value,
                                    correo = emailField.value,
                                    telefono = numberField.value,
                                    curp = curpField.value.uppercase(),
                                    nivel = levelUser.value,
                                    contrasena = passwordUser.value,
                                    token = tokenUser.value
                                )

                                updateUserWithoutSaveEmail(context, editUser)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 70.dp)
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

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para abrir el diálogo de cambio de contraseña
                Button(
                    onClick = {
                        showDialogPassword.value = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 70.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.pantone465)
                    )
                ) {
                    Text(
                        text = "Cambiar contraseña",
                        color = colorResource(R.color.pantone490),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                val userSession = getUserSession(context)

                // Mostrar botón de eliminación si el usuario no es el mismo que inició sesión
                if (emailField.value != userSession) {
                    if (levelUser.value == 2) {
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                showDialogDelete.value = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 70.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.pantone7420)
                            )
                        ) {
                            Text(
                                text = "Eliminar cuenta",
                                color = colorResource(R.color.pantone468),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )

    // Mostrar el diálogo de cambio de contraseña si está habilitado
    if (showDialogPassword.value) {
        ChangePasswordDialogAdmin(
            onDismiss = { showDialogPassword.value = false },
            userId
        )
    }

    // Mostrar el diálogo de confirmación de eliminación si está habilitado
    if (showDialogDelete.value) {
        DeleteAccountDialog(
            onDismiss = { showDialogDelete.value = false },
            userId,
            navController
        )
    }
}

//Alert Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialogAdmin(
    onDismiss: () -> Unit,
    userId: Int
) {
    val newPasswordField = remember { mutableStateOf("") }
    val confirmPasswordField = remember { mutableStateOf("") }

    val contextDb = LocalContext.current
    val database = AppDatabase.getDatabase(contextDb)
    val userDao = database.userDao()

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
                        text = "Cambiar contraseña",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CustomOutlinedTextField(
                        newPasswordField,
                        "Nueva contraseña",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomOutlinedTextField(
                        confirmPasswordField,
                        "Repetir nueva contraseña",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                color = colorResource(R.color.pantone490),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Botón para aceptar el cambio de contraseña
                        TextButton(
                            onClick = {
                                // Llamar a la función de cambiar contraseña
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = AuthService(userDao = userDao).changePasswordAdmin(
                                        userId,
                                        newPasswordField.value,
                                        confirmPasswordField.value
                                    )

                                    withContext(Dispatchers.Main) {
                                        if (result == null) {
                                            Toast.makeText(contextDb, "Contraseña actualizada correctamente", Toast.LENGTH_LONG).show()
                                            onDismiss()
                                        } else {
                                            Toast.makeText(contextDb, result, Toast.LENGTH_LONG).show()
                                        }
                                    }
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
        },
    )
}


// Alert Dialog Eliminar Cuenta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    userId: Int,
    navController: NavController
) {

    val contextDb = LocalContext.current
    val database = AppDatabase.getDatabase(contextDb)
    val userDao = database.userDao()

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
                        text = "Eliminar cuenta",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "¿Estás seguro de que deseas eliminar esta cuenta? Se eliminarán las solicitudes y ya no podrás volver a inicar sesión.",
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

                        TextButton(
                            onClick = {
                                // Llamar a la función de cambiar contraseña
                                CoroutineScope(Dispatchers.IO).launch {
                                    val user = userDao.getUserById(userId)

                                    if (user != null) {
                                        val deleted = userDao.deleteUser(user)

                                        if (deleted > 0) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(contextDb, "Cuenta eliminada exitosamente", Toast.LENGTH_LONG).show()
                                                onDismiss()

                                                // Redirigir a menu
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

suspend fun updateUserWithoutSaveEmail(context: Context, user: User) {
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    // Verificar si existe un usuario con el mismo correo, excluyendo al usuario actual
    val existingUserByEmail = userDao.getUserByEmail(user.correo)
    if (existingUserByEmail != null && existingUserByEmail.id != user.id) {
        // El correo está en uso por otro usuario
        Toast.makeText(context, "El correo ya está en uso por otro usuario", Toast.LENGTH_LONG).show()
        return
    }

    // Verificar si existe un usuario con la misma CURP, excluyendo al usuario actual
    val existingUserByCurp = userDao.getUserByCurp(user.curp)
    if (existingUserByCurp != null && existingUserByCurp.id != user.id) {
        // La CURP está en uso por otro usuario
        Toast.makeText(context, "La CURP ya está en uso por otro usuario", Toast.LENGTH_LONG).show()
        return
    }

    // Si no hay duplicados, actualiza el usuario en la base de datos
    val rowsAffected = userDao.updateUser(user)

    if (rowsAffected > 0) {

        //Mostrar mensaje
        Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_LONG).show()
    } else {
        //Mostrar mensaje
        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
    }
}