package com.mgtecsystem.monitoreosaludable

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GraficoManualActivity : AppCompatActivity() {

    private lateinit var layoutDatosEvaluaciones: LinearLayout
    private lateinit var btnEliminarTodo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grafico_manual)

            layoutDatosEvaluaciones = findViewById(R.id.layoutDatosEvaluaciones)
        btnEliminarTodo = findViewById(R.id.btnEliminarTodo)

        // Título superior
        val titulo = TextView(this).apply {
            text = "GRÁFICO DE EVALUACIÓN"
            textSize = 24f
            setTextColor(Color.parseColor("#4A148C"))
            setPadding(0, 0, 0, 32)
        }
        layoutDatosEvaluaciones.addView(titulo)

        // Recibir datos
        val evaluaciones = intent.getSerializableExtra("evaluaciones") as? ArrayList<Evaluacion>

        if (!evaluaciones.isNullOrEmpty()) {
            evaluaciones.forEach { evaluacion ->

                // Mostrar nombre del paciente
                val nombrePaciente = TextView(this).apply {
                    text = "Nombre: ${evaluacion.nombre}"
                    textSize = 20f
                    setTextColor(Color.DKGRAY)
                    setPadding(0, 0, 0, 8)
                }
                layoutDatosEvaluaciones.addView(nombrePaciente)

                // Mostrar datos en columna
                val datosTexto = TextView(this).apply {
                    text = """
                        Peso: ${evaluacion.peso} kg
                        Pasos: ${evaluacion.pasos}
                        Frecuencia: ${evaluacion.frecuencia} bpm
                    """.trimIndent()
                    textSize = 20f
                    setTextColor(Color.BLACK)
                    setPadding(0, 0, 0, 16)
                }
                layoutDatosEvaluaciones.addView(datosTexto)

                // Datos a graficar
                val datosPaciente = listOf(
                    "Peso" to evaluacion.peso.toDouble(),
                    "Pasos" to evaluacion.pasos.toDouble(),
                    "Frecuencia" to evaluacion.frecuencia.toDouble()
                )

                // Crear gráfico personalizado
                val grafico = CustomBarChartView(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        700
                    ).apply {
                        setMargins(20, 40, 20, 60)
                    }
                    setBackgroundColor(Color.parseColor("#EEEEEE"))
                    datos = datosPaciente
                }

                layoutDatosEvaluaciones.addView(grafico)
            }
        } else {
            layoutDatosEvaluaciones.addView(TextView(this).apply {
                text = "No hay datos disponibles."
                textSize = 18f
                setTextColor(Color.RED)
            })
        }

        // Botón eliminar
        btnEliminarTodo.setOnClickListener {
            layoutDatosEvaluaciones.removeAllViews()
        }
    }
}
