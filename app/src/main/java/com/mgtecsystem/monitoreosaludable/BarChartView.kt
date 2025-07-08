package com.mgtecsystem.monitoreosaludable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var datos: List<Pair<String, Double>> = emptyList()
        set(value) {
            field = value
            invalidate() // Redibuja la vista cuando cambian los datos
        }

    private val paintBarra = Paint().apply {
        color = Color.BLUE
    }

    private val paintTexto = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (datos.isEmpty()) return

        val ancho = width.toFloat()
        val alto = height.toFloat()
        val espacio = 50f
        val margenInferior = 150f

        val anchoBarra = (ancho - espacio * (datos.size + 1)) / datos.size
        val maxValor = datos.maxOf { it.second }

        datos.forEachIndexed { i, (nombre, valor) ->
            val izquierda = espacio + i * (anchoBarra + espacio)
            val alturaBarra = if (maxValor != 0.0) {
                ((valor / maxValor) * (alto - margenInferior)).toFloat()
            } else {
                0f
            }
            val arriba = alto - alturaBarra
            val derecha = izquierda + anchoBarra
            val abajo = alto

            canvas.drawRect(izquierda, arriba, derecha, abajo, paintBarra)
            canvas.drawText(nombre, izquierda + anchoBarra / 2, alto - 50f, paintTexto)
        }
    }
}