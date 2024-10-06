package com.example.optabasco.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    val userField = remember { mutableStateOf("") }
    val passwordField = remember { mutableStateOf("") }

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
                    TextField(
                        value = userField.value,
                        onValueChange = { userField.value = it },
                        label = { Text("Usuario", color = colorResource(R.color.pantone468)) },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(colorResource(R.color.pantone490)),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.pantone490), // Color de fondo cuando está enfocado
                            unfocusedContainerColor = colorResource(R.color.pantone490), // Color de fondo cuando no está enfocado
                            focusedIndicatorColor = Color.Transparent, // Color de la línea inferior cuando está enfocado
                            unfocusedIndicatorColor = Color.Transparent // Color de la línea inferior cuando no está enfocado
                        ),
                        textStyle = TextStyle(
                            color = colorResource(R.color.pantone468), // Color del texto
                            fontSize = 18.sp, // Tamaño de la fuente
                            fontWeight = FontWeight.Bold, // Peso del texto
                            textAlign = TextAlign.Center
                        ),
                        shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        value = passwordField.value,
                        onValueChange = { passwordField.value = it },
                        label = { Text("Contraseña", color = colorResource(R.color.pantone468)) },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(colorResource(R.color.pantone490)),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(R.color.pantone490), // Color de fondo cuando está enfocado
                            unfocusedContainerColor = colorResource(R.color.pantone490), // Color de fondo cuando no está enfocado
                            focusedIndicatorColor = Color.Transparent, // Color de la línea inferior cuando está enfocado
                            unfocusedIndicatorColor = Color.Transparent // Color de la línea inferior cuando no está enfocado
                        ),
                        textStyle = TextStyle(
                            color = colorResource(R.color.pantone468), // Color del texto
                            fontSize = 18.sp, // Tamaño de la fuente
                            fontWeight = FontWeight.Bold, // Peso del texto
                            textAlign = TextAlign.Center
                        ),
                        shape = RoundedCornerShape(16.dp), // Esquinas redondeadas
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                            .height(50.dp), // Altura del botón
                        shape = RoundedCornerShape(30.dp), // Esquinas redondeadas
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.pantone465) // Color de fondo del botón
                        )
                    ) {
                        Text(
                            text = "Entrar",
                            color = colorResource(R.color.pantone490),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(50.dp))

                    Text(text = "¿No tienes cuenta?", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.Normal, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Registrate", color = colorResource(R.color.pantone465), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                }
            }
        }
    )
}