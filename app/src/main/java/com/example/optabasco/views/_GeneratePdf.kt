package com.example.optabasco.views

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePdf(context: Context, uri: Uri) {
    // 1. Crea una instancia de PdfDocument
    val pdfDocument = PdfDocument()

    // 2. Crea una página en el PDF
    val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create() // Dimensiones de una hoja carta
    val page = pdfDocument.startPage(pageInfo)

    // 3. Dibuja en la página
    val canvas: Canvas = page.canvas
    val paint = Paint()
    paint.textSize = 16f
    paint.isFakeBoldText = true

    // Dibuja un texto en la posición (x, y)
    canvas.drawText("Mi primer document PDF en Jetpack Compose", 80f, 80f, paint)

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