package com.mgtecsystem.monitoreosaludable

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var datos: List<Pair<String, Double>> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val paintBarra = Paint()
    private val paintTexto = Paint().apply {
        color = Color.BLACK
        textSize = 36f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    private val paintEjes = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 3f
    }
    private val paintLineas = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }
    private val paintNumerosY = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.RIGHT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (datos.isEmpty()) {
            canvas.drawText("No hay datos para mostrar", width / 2f, height / 2f, paintTexto)
            return
        }

        val ancho = width.toFloat()
        val alto = height.toFloat()
        val margenIzquierdo = 120f
        val margenDerecho = 40f
        val margenSuperior = 80f
        val margenInferior = 150f
        val espacio = 100f // MÁS ESPACIO ENTRE BARRAS

        val areaGraficoAlto = alto - margenSuperior - margenInferior
        val areaGraficoAncho = ancho - margenIzquierdo - margenDerecho

        val maxValor = 100.0 // escala fija en porcentaje

        // Líneas horizontales y etiquetas eje Y (cada 20%)
        for (i in 0..5) {
            val y = margenSuperior + areaGraficoAlto * (1 - i / 5f)
            canvas.drawLine(margenIzquierdo, y, ancho - margenDerecho, y, paintLineas)
            canvas.drawText("${i * 20}%", margenIzquierdo - 10f, y + 10f, paintNumerosY)
        }

        // Ejes X e Y
        canvas.drawLine(margenIzquierdo, margenSuperior, margenIzquierdo, alto - margenInferior, paintEjes)
        canvas.drawLine(margenIzquierdo, alto - margenInferior, ancho - margenDerecho, alto - margenInferior, paintEjes)

        // Barras
        val anchoBarra = (areaGraficoAncho - espacio * (datos.size - 1)) / datos.size
        val colores = listOf(Color.BLUE, Color.RED, Color.GREEN)

        datos.forEachIndexed { i, (etiqueta, valor) ->
            paintBarra.color = colores[i % colores.size]

            val izquierda = margenIzquierdo + i * (anchoBarra + espacio)
            val altura = ((valor / maxValor) * areaGraficoAlto).toFloat()
            val arriba = alto - margenInferior - altura
            val derecha = izquierda + anchoBarra
            val abajo = alto - margenInferior

            // Barra
            canvas.drawRect(izquierda, arriba, derecha, abajo, paintBarra)

            // Etiqueta abajo
            canvas.drawText(etiqueta, izquierda + anchoBarra / 2, alto - margenInferior + 40f, paintTexto)
        }
    }
}
