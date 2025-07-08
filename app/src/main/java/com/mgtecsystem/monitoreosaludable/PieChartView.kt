package com.mgtecsystem.monitoreosaludable

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var datos: List<Pair<String, Float>> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val colores = listOf(
        Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
        Color.CYAN, Color.YELLOW, Color.LTGRAY, Color.DKGRAY
    )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (datos.isEmpty()) {
            canvas.drawText("No hay datos", width / 2f, height / 2f, textPaint)
            return
        }

        val total = datos.fold(0f) { acc, pair -> acc + pair.second }
        val radius = min(width, height) * 0.4f
        val centerX = width / 2f
        val centerY = height / 2f
        var startAngle = 0f

        datos.forEachIndexed { i, (nombre, valor) ->
            val sweepAngle = (valor / total) * 360f
            paint.color = colores[i % colores.size]
            canvas.drawArc(
                centerX - radius, centerY - radius,
                centerX + radius, centerY + radius,
                startAngle, sweepAngle, true, paint
            )
            startAngle += sweepAngle
        }

        canvas.drawText("Calorías por persona", centerX, centerY + radius + 50f, textPaint)
    }
}
