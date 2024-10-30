package com.example.optabasco.views

import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val context = navController.context
    val contextDb = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val userEmail = getUserSession(context)

    val userDao = AppDatabase.getDatabase(contextDb).userDao()

    val nameField = remember { mutableStateOf("") }
    val lastPaternField = remember { mutableStateOf("") }
    val lastMaternField = remember { mutableStateOf("") }
    val emailField = remember { mutableStateOf("") }
    val numberField = remember { mutableStateOf("") }
    val curpField = remember { mutableStateOf("") }
    val passwordUser = remember { mutableStateOf("") }
    val levelUser = remember { mutableStateOf(2) }
    val idUser = remember { mutableStateOf(0) }

    val showDialogPassword = remember { mutableStateOf(false) }
    val showDialogDelete = remember { mutableStateOf(false) }

    LaunchedEffect(userEmail) {
        val userLoggin = userEmail?.let { userDao.getUserByEmail(it) }

        userLoggin?.let { user ->
            nameField.value = user.nombre
            lastPaternField.value = user.paterno
            lastMaternField.value = user.materno
            emailField.value = user.correo
            numberField.value = user.telefono
            curpField.value = user.curp
            passwordUser.value = user.contrasena
            levelUser.value = user.nivel
            idUser.value = user.id
        }
    }

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
                Text(
                    text = "Perfil",
                    color = colorResource(R.color.pantone490),
                    fontSize = 45.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(20.dp))

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
                    label = "Correo electrónico (Bloqueado)",
                    enabled = false
                )
                Spacer(modifier = Modifier.height(20.dp))

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
                                    id = idUser.value,
                                    nombre = nameField.value,
                                    paterno = lastPaternField.value,
                                    materno = lastMaternField.value,
                                    correo = emailField.value,
                                    telefono = numberField.value,
                                    curp = curpField.value.uppercase(),
                                    contrasena = passwordUser.value,
                                    nivel = levelUser.value
                                )

                                updateUser(context, editUser)
                            } else {
                                // Mostrar el mensaje de error
                                Toast.makeText(context, validationError, Toast.LENGTH_LONG).show()
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
    )

    if (showDialogPassword.value) {
        ChangePasswordDialog(
            onDismiss = { showDialogPassword.value = false },
            idUser
        )
    }

    if (showDialogDelete.value) {
        DeleteAccountDialog(
            onDismiss = { showDialogDelete.value = false },
            idUser,
            navController
        )
    }
}

fun saveUserSession(context: Context, userEmail: String) {
    val sharedPref: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("userEmail", userEmail)
    editor.apply()
}

fun getUserSession(context: Context): String? {
    val sharedPref: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    return sharedPref.getString("userEmail", "")
}

fun validateFieldsCreateUser(
    name: String,
    lastPatern: String,
    lastMatern: String,
    email: String,
    number: String,
    curp: String,
): String? {
    //Validar que ningún campo esté vacío
    if (
        name.isBlank() ||
        lastPatern.isBlank() ||
        lastMatern.isBlank() ||
        email.isBlank() ||
        number.isBlank() ||
        curp.isBlank()
    ) {
        return "Todos los campos deben estar llenos"
    }

    if (number.length != 10) {
        return "El número de teléfono debe tener 10 dígitos"
    }

    if (curp.length != 18) {
        return "La CURP debe tener 18 caracteres"
    }

    return null
}

suspend fun updateUser(context: Context, user: User) {
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
        //Guarda los datos del usuario en una variable global
        saveUserSession(context, user.correo)

        //Mostrar mensaje
        Toast.makeText(context, "Actualizado correctamente", Toast.LENGTH_LONG).show()
    } else {
        //Mostrar mensaje
        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
    }
}

//Alert Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    userId: MutableState<Int>
) {
    val oldPasswordField = remember { mutableStateOf("") }
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cambiar contraseña",
                        color = colorResource(R.color.pantone468),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CustomOutlinedTextField(
                        oldPasswordField,
                        "Antigua contraseña",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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
                                color = colorResource(R.color.pantone468),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        TextButton(
                            onClick = {
                                // Llamar a la función de cambiar contraseña
                                CoroutineScope(Dispatchers.IO).launch {
                                    val result = AuthService(userDao = userDao).changePassword(
                                        userId.value,
                                        oldPasswordField.value,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountDialog(
    onDismiss: () -> Unit,
    userId: MutableState<Int>,
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
                                    val user = userDao.getUserById(userId.value)

                                    if (user != null) {
                                        database.applicationDao().deleteApplicationsByUserId(userId.value)
                                        val deleted = userDao.deleteUser(user)

                                        if (deleted > 0) {
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(contextDb, "Cuenta eliminada exitosamente", Toast.LENGTH_LONG).show()
                                                onDismiss()

                                                // Redirigir a login y limpiar el historial
                                                navController.navigate("login") {
                                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                                }
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