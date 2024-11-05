package com.example.optabasco.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.optabasco.R

// Composable que crea un campo de selección con un menú desplegable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedSelectField(
    selectedOption: MutableState<String>,
    label: String,
    options: List<String>,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(R.color.pantone468),
    textColor: Color = colorResource(R.color.pantone490)
) {
    // Estado para controlar si el menú desplegable está expandido o no
    var expanded by remember { mutableStateOf(false) }

    // Contenedor para el campo de texto con el menú desplegable
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded // Alterna entre expandir y colapsar el menú
        },
        modifier = modifier.fillMaxWidth() // Modificador para que ocupe todo el ancho disponible
    ) {
        // Campo de texto que muestra la opción seleccionada
        OutlinedTextField(
            value = selectedOption.value, // Muestra la opción seleccionada
            onValueChange = { selectedOption.value = it }, // Cambia el valor si es editable
            label = {
                Text(
                    label,
                    color = backgroundColor
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { expanded = true }, // Abre el menú al hacer clic
            enabled = enabled, // Habilita o deshabilita el campo
            readOnly = true, // El campo es solo de lectura
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = backgroundColor,
                unfocusedTextColor = backgroundColor,
                focusedBorderColor = backgroundColor,
                unfocusedBorderColor = backgroundColor,
                cursorColor = backgroundColor
            ),
            textStyle = TextStyle(
                color = colorResource(R.color.pantone468),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            shape = RoundedCornerShape(16.dp)
        )

        //Menú desplegable con las opciones
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded = false}, // Cierra el menú cuando se hace clic fuera
            modifier = Modifier.background(backgroundColor)
        ) {
            // Itera sobre las opciones y crea un item para cada una
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = textColor, fontWeight = FontWeight.Bold) },
                    onClick = {
                        selectedOption.value = option // Establece la opción seleccionada
                        expanded = false
                    }
                )
            }
        }
    }
}