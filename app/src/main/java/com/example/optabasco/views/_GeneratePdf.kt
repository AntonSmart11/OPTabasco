package com.example.optabasco.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.optabasco.R
import com.example.optabasco.database.models.Application
import com.example.optabasco.database.models.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePdf(context: Context, uri: Uri, application: Application, user: User) {
    val userName = "${user.nombre} ${user.paterno} ${user.materno}" ?: "No identificado"

    val idApplication = application.id ?: "N/A"
    val dateApplication = application.fecha ?: "N/A"
    val streetApplication = application.calle ?: "N/A"
    val ranchApplication = application.coloniaRancheria ?: "N/A"
    val municipalityApplication = application.municipio ?: "N/A"
    val typeApplication = application.tipoSolicitud ?: "N/A"
    val dataApplication = application.descripcion ?: "N/A"

    // 1. Crea una instancia de PdfDocument
    val pdfDocument = PdfDocument()

    // 2. Crea una página en el PDF
    val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create() // Dimensiones de una hoja carta
    val page = pdfDocument.startPage(pageInfo)

    // 3. Dibuja en la página
    val canvas: Canvas = page.canvas
    val paint = Paint()

    // Cargar la imagen y escalarla
    val logoBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_circular)
    val scaledBitmap: Bitmap = Bitmap.createScaledBitmap(logoBitmap, 100, 100, false)

    // Dibuja la imagen en la parte superior izquierda
    canvas.drawBitmap(scaledBitmap, 40f, 30f, paint)

    // Dibuja un texto en la posición (x, y)
    paint.textSize = 16f
    paint.isFakeBoldText = false

    // Dibuja los datos de la parte superior derecha
    paint.textSize = 16f
    paint.isFakeBoldText = true
    val textRight = "Folio: $idApplication\nFecha: $dateApplication"

    val lines = textRight.split("\n")
    val lineHeight = paint.fontSpacing
    var yPosition = 30f

    // Dibuja cada línea
    for (line in lines) {
        val textWidth = paint.measureText(line)
        canvas.drawText(line, pageInfo.pageWidth - textWidth - 40f, yPosition, paint)
        yPosition += lineHeight
    }

    // Texto principal
    val textToDraw = """
        Dirigido a $userName, se informa que la solicitud con el folio $idApplication, registrada el día $dateApplication, ha sido aceptada. La obra correspondiente se realizará conforme a los siguientes detalles:
        """.trimIndent()

    // Dibuja el texto dividido en líneas automáticas
    val maxWidth = pageInfo.pageWidth - 80f // Deja un margen de 40px a cada lado
    var yPositionText = 180f
    var yPositionDetails = 290f
    val lineSpacing = paint.fontSpacing

    // Divide el texto en líneas que se ajusten al ancho
    for (line in divideTextIntoLines(textToDraw, paint, maxWidth)) {
        canvas.drawText(line, 40f, yPositionText, paint)
        yPositionText += lineSpacing
    }

    // Agrega los detalles adicionales en formato de lista
    val detailsList = listOf(
        "Calle: $streetApplication",
        "Colonia o Ranchería: $ranchApplication",
        "Municipio: $municipalityApplication",
        "Tipo de Solicitud: $typeApplication",
        "Descripción: $dataApplication"
    )

    for (detail in detailsList) {
        // Divide cada línea en sublíneas según el ancho máximo permitido
        val dividedLines = divideTextIntoLines(detail, paint, maxWidth)

        for (line in dividedLines) {
            if (yPositionDetails + lineSpacing > pageInfo.pageHeight - 100f) {
                // Si el texto excede el espacio disponible, rompe el bucle o maneja más páginas
                canvas.drawText("Texto truncado...", 40f, yPositionDetails, paint)
                break
            }
            canvas.drawText(line, 40f, yPositionDetails, paint)
            yPositionDetails += lineSpacing
        }
    }

    // Agrega líneas para firma y sello en la parte inferior
    val lineYPosition = pageInfo.pageHeight - 100f // Altura para las líneas
    val textYPosition = lineYPosition + 20f // Posición del texto debajo de las líneas
    val lineStartX = 80f
    val lineEndX = pageInfo.pageWidth - 80f
    val middleX = pageInfo.pageWidth / 2

    // Dibuja la línea de "Firma"
    canvas.drawLine(lineStartX, lineYPosition, middleX - 20f, lineYPosition, paint)

    // Calcula la posición del texto "Firma" centrado
    val firmaText = "Firma"
    val firmaTextWidth = paint.measureText(firmaText)
    val firmaTextX = (lineStartX + middleX - 20f - firmaTextWidth) / 2
    canvas.drawText(firmaText, firmaTextX, textYPosition, paint)

    // Dibuja la línea de "Sello"
    canvas.drawLine(middleX + 20f, lineYPosition, lineEndX, lineYPosition, paint)

    // Calcula la posición del texto "Sello" centrado
    val selloText = "Sello"
    val selloTextWidth = paint.measureText(selloText)
    val selloTextX = (middleX + 20f + lineEndX - selloTextWidth) / 2
    canvas.drawText(selloText, selloTextX, textYPosition, paint)

    // Termina la página
    pdfDocument.finishPage(page)

    // 4. Guarda el documento en almacenamiento
    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
        }
        Toast.makeText(context, "PDF guardado", Toast.LENGTH_LONG).show()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        // 5. Cierra el documento PDF
        pdfDocument.close()
    }

}

// Función para dividir un texto largo en líneas más cortas según el ancho máximo
fun divideTextIntoLines(text: String, paint: Paint, maxWidth: Float): List<String> {
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var currentLine = ""

    for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        if (paint.measureText(testLine) <= maxWidth) {
            currentLine = testLine
        } else {
            lines.add(currentLine)
            currentLine = word
        }
    }

    if (currentLine.isNotEmpty()) {
        lines.add(currentLine)
    }

    return lines
}