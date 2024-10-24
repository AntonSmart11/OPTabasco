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
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption.value,
            onValueChange = { selectedOption.value = it },
            label = {
                Text(
                    label,
                    color = colorResource(R.color.pantone468)
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .clickable { expanded = true },
            enabled = enabled,
            readOnly = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colorResource(R.color.pantone468),
                unfocusedTextColor = colorResource(R.color.pantone468),
                focusedBorderColor = colorResource(R.color.pantone468),
                unfocusedBorderColor = colorResource(R.color.pantone468),
                cursorColor = colorResource(R.color.pantone468)
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
            onDismissRequest = {expanded = false},
            modifier = Modifier.background(backgroundColor)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = textColor, fontWeight = FontWeight.Bold) },
                    onClick = {
                        selectedOption.value = option
                        expanded = false
                    }
                )
            }
        }
    }
}