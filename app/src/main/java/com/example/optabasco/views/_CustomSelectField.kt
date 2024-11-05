package com.example.optabasco.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.optabasco.R

// Composable de campo de selección personalizado (similar a un DropDown)
@Composable
fun CustomSelectField(
    label: String,
    selectOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    backgroundColor: Color = colorResource(R.color.pantone490),
    textColor: Color = colorResource(R.color.pantone468),
    enabled: Boolean = true
) {
    // Estado que controla si el menú desplegable está abierto o cerrado
    val expanded = remember { mutableStateOf(false) }

    Column {

        Text(
            text = label,
            color = colorResource(R.color.pantone490),
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Fila horizontal que contiene el texto seleccionado y el ícono de expansión
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (enabled) backgroundColor else Color.Gray, shape = MaterialTheme.shapes.medium)
                .padding(12.dp)
                .clickable(enabled = enabled) { expanded.value = !expanded.value },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Muestra el texto de la opción seleccionada
            Text(
                text = selectOption,
                color = textColor,
                fontSize = 16.sp
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.expand),
                contentDescription = "Expandir",
                tint = textColor
            )
        }

        // Menú desplegable que contiene las opciones
        DropdownMenu(
            expanded = expanded.value, // Establece si el menú está expandido
            onDismissRequest = {expanded.value = false}, // Cierra el menú cuando se hace clic fuera de él
            modifier = Modifier.background(backgroundColor)
        ) {
            // Itera sobre cada opción en la lista de opciones
            options.forEach { option ->
                // Crea un ítem para cada opción
                DropdownMenuItem(
                    text = { Text(option, color = textColor, fontWeight = FontWeight.Bold) },
                    onClick = {
                        onOptionSelected(option) // Llama a la función con la opción seleccionada
                        expanded.value = false // Cierra el menú después de seleccionar la opción
                })
            }
        }
    }
}