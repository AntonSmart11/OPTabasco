package com.example.optabasco.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.optabasco.R

// Composable para un campo de texto de entrada con estilo y transformación visual para contraseñas
@Composable
fun CustomOutlinedTextField(
    valueState: MutableState<String>,
    label: String,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    // Define la transformación visual para contraseñas (ocultar texto)
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    // Crea un campo de texto con borde y etiquetas personalizadas
    OutlinedTextField(
        value = valueState.value, // Valor del campo de texto, se obtiene del estado
        onValueChange = { valueState.value = it },  // Cambia el valor cuando el texto cambia
        label = {
            Text(
                label,
                color = colorResource(R.color.pantone468),
                modifier = Modifier
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.pantone468),
            unfocusedTextColor = colorResource(R.color.pantone468),
            focusedBorderColor = colorResource(R.color.pantone468),
            unfocusedBorderColor = colorResource(R.color.pantone468),
            cursorColor = colorResource(R.color.pantone468),
        ),
        visualTransformation = visualTransformation, // Define si el texto es transformado (como una contraseña)
        textStyle = TextStyle(
            color = colorResource(R.color.pantone468),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        enabled = enabled, // Define si el campo es interactuable
        shape = RoundedCornerShape(16.dp),
        maxLines = maxLines,
        modifier = modifier.fillMaxWidth()
    )
}