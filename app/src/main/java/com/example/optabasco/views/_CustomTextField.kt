package com.example.optabasco.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.optabasco.R

// Composable de campo personalizado
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    valueState: MutableState<String>,
    label: String,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Determina si se debe mostrar como una contraseña (máscara de texto) o no
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    // Componente TextField para ingreso de texto
    TextField(
        value = valueState.value, // El valor actual del campo de texto
        onValueChange = { valueState.value = it }, // Actualiza el valor cuando el usuario lo cambia
        label = { Text(label, color = colorResource(R.color.pantone468))},
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.pantone490)),
        colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.pantone490),
                unfocusedContainerColor = colorResource(R.color.pantone490),
                cursorColor = colorResource(R.color.pantone468),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Gray,
                disabledIndicatorColor = Color.Gray,
        ),
        visualTransformation = visualTransformation, // Transforma la visualización del texto si es una contraseña
        textStyle = TextStyle(
            color = colorResource(R.color.pantone468),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        enabled = enabled, // Controla si el campo es interactivo
        shape = RoundedCornerShape(16.dp),
        maxLines = 1
    )
}
