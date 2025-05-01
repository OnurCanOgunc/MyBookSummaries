package com.decode.mybooksummaries.presentation.detail.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun createPdfFromText(
    context: Context,
    title: String,
    summary: String,
    fileName: String = "book_summary"
): File {
    val pdf = PdfDocument()
    val width = 595
    val height = 842
    val margin = 40f

    val bodyPaint = Paint().apply { color = Color.BLACK; textSize = 14f }
    val titlePaint = Paint().apply { color = Color.BLACK; textSize = 18f; isFakeBoldText = true }
    val lineHeight = bodyPaint.textSize + 6f
    val maxWidth = width - margin * 2

    fun splitLines(text: String, paint: Paint) = buildList {
        val words = text.split(" ")
        var line = ""
        for (word in words) {
            val test = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(test) <= maxWidth) line = test
            else { add(line); line = word }
        }
        if (line.isNotEmpty()) add(line)
    }

    val lines = splitLines(summary, bodyPaint)
    var pageNum = 1
    var y = margin + titlePaint.textSize + 10f
    var page = pdf.startPage(PdfDocument.PageInfo.Builder(width, height, pageNum).create())
    var canvas = page.canvas

    canvas.drawText(title, margin, y, titlePaint)
    y += 30f

    for (line in lines) {
        if (y + lineHeight > height - margin) {
            pdf.finishPage(page)
            pageNum++
            y = margin + titlePaint.textSize + 30f
            page = pdf.startPage(PdfDocument.PageInfo.Builder(width, height, pageNum).create())
            canvas = page.canvas
            canvas.drawText(title, margin, margin + titlePaint.textSize, titlePaint)
        }
        canvas.drawText(line, margin, y, bodyPaint)
        y += lineHeight
    }

    pdf.finishPage(page)
    return File(context.cacheDir, "$fileName.pdf").apply {
        pdf.writeTo(FileOutputStream(this))
        pdf.close()
    }
}

fun sharePdf(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "PDF olarak paylaş"))
}
