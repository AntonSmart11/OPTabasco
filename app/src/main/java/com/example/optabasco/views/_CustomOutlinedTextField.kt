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

@Composable
fun CustomOutlinedTextField(
    valueState: MutableState<String>,
    label: String,
    isPassword: Boolean = false,
    enabled: Boolean = true,
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = {
            Text(
                label,
                color = colorResource(R.color.pantone468)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colorResource(R.color.pantone468),
            unfocusedTextColor = colorResource(R.color.pantone468),
            focusedBorderColor = colorResource(R.color.pantone468),
            unfocusedBorderColor = colorResource(R.color.pantone468),
            cursorColor = colorResource(R.color.pantone468),
        ),
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            color = colorResource(R.color.pantone468),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        maxLines = 1,
        modifier = Modifier.fillMaxWidth()
    )
}