package com.example.optabasco.views

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import com.example.optabasco.database.dao.UserDao
import com.example.optabasco.database.models.User
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val nameField = remember { mutableStateOf("") }
    val lastPaternField = remember { mutableStateOf("") }
    val lastMaternField = remember { mutableStateOf("") }
    val emailField = remember { mutableStateOf("") }
    val numberField = remember { mutableStateOf("") }
    val curpField = remember { mutableStateOf("") }
    val passwordField = remember { mutableStateOf("") }
    val repeatPasswordField = remember { mutableStateOf("") }

    //Crear un scope de corutina
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text("OP Tabasco", color = colorResource(R.color.pantone468), fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = colorResource(R.color.pantone490),
                titleContentColor = colorResource(R.color.pantone468)
            ),
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
                        valueState = emailField,
                        label = "Correo electrónico"
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    CustomTextField(
                        valueState = numberField,
                        label = "Teléfono"
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

                    Button(
                        onClick = {
                            val validationError = validateFields(
                                name = nameField.value,
                                lastPatern = lastPaternField.value,
                                lastMatern = lastMaternField.value,
                                email = emailField.value,
                                number = numberField.value,
                                curp = curpField.value,
                                password = passwordField.value,
                                repeatPassword = repeatPasswordField.value
                            )

                            if (validationError == null) {
                                coroutineScope.launch {
                                    //Llamar a la función de inserción dentro de una corutina
                                    val user = User(
                                        nombre = nameField.value,
                                        paterno = lastPaternField.value,
                                        materno = lastMaternField.value,
                                        correo = emailField.value,
                                        telefono = numberField.value,
                                        curp = curpField.value,
                                        contrasena = passwordField.value,
                                        nivel = 2
                                    )
                                    insertUser(context, user, navController)
                                }
                            } else {
                                // Mostrar el mensaje de error
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

fun validateFields(
    name: String,
    lastPatern: String,
    lastMatern: String,
    email: String,
    number: String,
    curp: String,
    password: String,
    repeatPassword: String
): String? {
    //Validar que ningún campo esté vacío
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

suspend fun insertUser(context: Context, user: User, navController: NavController) {
    val database = AppDatabase.getDatabase(context)
    val userDao = database.userDao()

    //Inserta el usuario a la base de datos
    userDao.insert(user)

    // Mostrar el mensaje
    Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_LONG).show()

    //Redirección
    navController.navigate("login")
}