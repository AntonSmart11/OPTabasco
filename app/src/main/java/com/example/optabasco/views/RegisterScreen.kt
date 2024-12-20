package com.example.optabasco.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.models.User
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    // Variable para almacenar el estado de desplazamiento de la pantalla
    val scrollState = rememberScrollState()

    // Variables para almacenar los valores de los campos del formulario de registro
    val nameField = remember { mutableStateOf("") }
    val lastPaternField = remember { mutableStateOf("") }
    val lastMaternField = remember { mutableStateOf("") }
    val emailField = remember { mutableStateOf("") }
    val numberField = remember { mutableStateOf("") }
    val curpField = remember { mutableStateOf("") }
    val passwordField = remember { mutableStateOf("") }
    val repeatPasswordField = remember { mutableStateOf("") }

    // Crear un alcance de corrutina para operaciones asincrónicas
    val coroutineScope = rememberCoroutineScope()

    // Obtiene el contexto de la actividad actual
    val context = LocalContext.current

    Scaffold(
        // Define la barra superior de la pantalla de registro
        topBar = { CenterAlignedTopAppBar(
            title = { Text("OP Tabasco", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.pantone490),
                titleContentColor = colorResource(R.color.pantone468)
            ),
            // Botón de navegación para regresar a la pantalla anterior
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = "Atrás",
                        tint = colorResource(R.color.pantone468)
                    )
                }
            }
        )},
        // Contenido principal de la pantalla
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
                Spacer(Modifier.height(20.dp))
                Text(text = "Registrate", color = colorResource(R.color.pantone490), fontSize = 45.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(30.dp))

                // Campos de texto personalizados para capturar datos de usuario
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
                        valueState = emailField,
                        label = "Correo electrónico"
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    CustomTextField(
                        valueState = curpField,
                        label = "CURP"
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    CustomTextField(
                        valueState = passwordField,
                        label = "Contraseña",
                        isPassword = true,
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    CustomTextField(
                        valueState = repeatPasswordField,
                        label = "Repita Contraseña",
                        isPassword = true,
                    )
                    Spacer(modifier = Modifier.height(30.dp))

                    // Botón de registro
                    Button(
                        onClick = {
                            // Validación de los campos antes de crear el usuario
                            val validationError = validateFieldsCreateUser(
                                name = nameField.value,
                                lastPatern = lastPaternField.value,
                                lastMatern = lastMaternField.value,
                                email = emailField.value,
                                number = numberField.value,
                                curp = curpField.value,
                                password = passwordField.value,
                                repeatPassword = repeatPasswordField.value
                            )

                            // Si no hay errores, insertar el usuario en la base de datos
                            if (validationError == null) {
                                coroutineScope.launch {
                                    //Llamar a la función de inserción dentro de una corutina
                                    val user = User(
                                        nombre = nameField.value,
                                        paterno = lastPaternField.value,
                                        materno = lastMaternField.value,
                                        correo = emailField.value,
                                        telefono = numberField.value,
                                        curp = curpField.value.uppercase(),
                                        contrasena = passwordField.value,
                                        nivel = 2,
                                        token = ""
                                    )
                                    insertUser(context, user, navController)
                                }
                            } else {
                                // Mostrar error de validación al usuario
                                Toast.makeText(context, validationError, Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pantone465)
                        )
                    ) {
                        Text(
                            text = "Registrarse",
                            color = colorResource(R.color.pantone490),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Texto con opciones para iniciar sesión si ya se tiene cuenta
                    Text(text = "¿Tienes cuenta?", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Inicia sesión", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                }
            }
        }
    )
}

// Función para validar los campos del formulario de registro de usuario
fun validateFieldsCreateUser(
    name: String,
    lastPatern: String,
    lastMatern: String,
    email: String,
    number: String,
    curp: String,
    password: String,
    repeatPassword: String
): String? {
    // Validar que ningún campo esté vacío
    if (
        name.isBlank() ||
        lastPatern.isBlank() ||
        lastMatern.isBlank() ||
        email.isBlank() ||
        number.isBlank() ||
        curp.isBlank() ||
        password.isBlank() ||
        repeatPassword.isBlank()
    ) {
        return "Todos los campos deben estar llenos"
    }

    if (password != repeatPassword) {
        return "Las contraseñas no coinciden"
    }

    if (number.length != 10) {
        return "El número de teléfono debe tener 10 dígitos"
    }

    if (curp.length != 18) {
        return "La CURP debe tener 18 caracteres"
    }

    return null
}

// Función para insertar el usuario en la base de datos
suspend fun insertUser(context: Context, user: User, navController: NavController) {
    // Obtener instancia de la base de datos
    val database = AppDatabase.getDatabase(context)

    // Acceso al DAO de usuario
    val userDao = database.userDao()

    // Verificar si ya existe un usuario con el mismo CURP o correo
    val existingUserByCurp = userDao.getUserByCurp(user.curp)
    val existingUserByEmail = userDao.getUserByEmail(user.correo)

    when {
        existingUserByCurp != null -> {
            // Mostrar mensaje de error si el CURP ya existe
            Toast.makeText(context, "Ya existe un usuario con esa CURP", Toast.LENGTH_LONG).show()
        }
        existingUserByEmail != null -> {
            // Mostrar mensaje de error si el correo ya existe
            Toast.makeText(context, "Ya existe un usuario con ese correo electrónico", Toast.LENGTH_LONG).show()
        }
        else -> {
            // Inserta el usuario en la base de datos y muestra un mensaje de éxito
            userDao.insert(user)

            // Mostrar el mensaje
            Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_LONG).show()

            // Redirigir a la pantalla
            navController.navigate("login")
        }
    }
}