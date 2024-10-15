package com.example.optabasco.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.optabasco.R
import com.example.optabasco.database.AppDatabase
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val emailField = remember { mutableStateOf("") }
    val passwordField = remember { mutableStateOf("") }

    //Crear un scope de corutina
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        //topBar = { TopAppBar(title = { Text("Prueba")})},
        content = {
            Column(
                modifier = Modifier.fillMaxSize().background(colorResource(R.color.pantone468)).verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(50.dp))
                Image(painterResource(R.drawable.logo_circular), contentDescription = "Logo", Modifier.size(250.dp))
                Spacer(Modifier.height(15.dp))
                Text(text = "Iniciar Sesión", color = colorResource(R.color.pantone490), fontSize = 45.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(30.dp))

                Column(modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    CustomTextField(
                        valueState = emailField,
                        label = "Correo electrónico",
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    CustomTextField(
                        valueState = passwordField,
                        label = "Contraseña",
                        isPassword = true,
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            //Iniciar sesión
                            coroutineScope.launch {
                                val userDao = AppDatabase.getDatabase(context).userDao()
                                val user = userDao.getUserByEmail(emailField.value)

                                if (user != null && user.contrasena == passwordField.value) {
                                    //Mensaje
                                    Toast.makeText(context, "Iniciado correctamente", Toast.LENGTH_LONG).show()

                                    //Guarda los datos del usuario en una variable global
                                    saveUserSession(context, emailField.value)

                                    //Navegar al dashboard
                                    navController.navigate("dashboardAdmin")
                                } else {
                                    //Mensaje de error
                                    Toast.makeText(context, "CURP o contraseña incorrectos", Toast.LENGTH_LONG).show()
                                }
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
                            text = "Entrar",
                            color = colorResource(R.color.pantone490),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "¿No tienes cuenta?", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Registrate", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, modifier = Modifier.clickable {
                        navController.navigate("register")
                    })
                }
            }
        }
    )
}

fun saveUserSession(context: Context, userEmail: String) {
    val sharedPref: SharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putString("userEmail", userEmail)
    editor.apply()
}