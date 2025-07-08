package com.mgtecsystem.monitoreosaludable

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class PrediccionActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPeso: EditText
    private lateinit var etEstatura: EditText
    private lateinit var etEdad: EditText
    private lateinit var etPasos: EditText
    private lateinit var etFrecuencia: EditText
    private lateinit var btnEvaluar: Button
    private lateinit var tvResultado: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediccion)

        etNombre = findViewById(R.id.etNombre)
        etPeso = findViewById(R.id.etPeso)
        etEstatura = findViewById(R.id.etEstatura)
        etEdad = findViewById(R.id.etEdad)
        etPasos = findViewById(R.id.etPasos)
        etFrecuencia = findViewById(R.id.etFrecuencia)
        btnEvaluar = findViewById(R.id.btnEvaluar)
        tvResultado = findViewById(R.id.tvResultado)
        btnVolver = findViewById(R.id.btnVolver) // Botón para volver a MainActivity

        // Autocompletar campos desde MainActivity si se pasaron
        etNombre.setText(intent.getStringExtra("nombre") ?: "")
        etPeso.setText(intent.getStringExtra("peso") ?: "")
        etEdad.setText(intent.getStringExtra("edad") ?: "")
        etPasos.setText(intent.getStringExtra("pasos") ?: "")
        etFrecuencia.setText(intent.getStringExtra("frecuencia") ?: "")

        btnEvaluar.setOnClickListener {
            evaluarCondicion()
        }

        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun evaluarCondicion() {
        val nombre = etNombre.text.toString().trim()
        val peso = etPeso.text.toString().toDoubleOrNull()
        val estatura = etEstatura.text.toString().toDoubleOrNull()
        val edad = etEdad.text.toString().toIntOrNull()
        val pasos = etPasos.text.toString().toIntOrNull()
        val frecuencia = etFrecuencia.text.toString().toIntOrNull()

        if (nombre.isEmpty() || peso == null || estatura == null || edad == null || pasos == null || frecuencia == null || estatura == 0.0) {
            Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            return
        }

        val imc = peso / (estatura * estatura)
        val df = DecimalFormat("#.##")
        val imcClasificacion = when {
            imc < 18.5 -> "Bajo peso"
            imc < 25 -> "Normal"
            imc < 30 -> "Sobrepeso"
            else -> "Obesidad"
        }

        val calorias = (peso * 0.5) + (pasos * 0.02) + (frecuencia * 0.1)
        val prediccion = when {
            imc >= 30 || calorias < 100 -> " Posible tendencia a obesidad o metabolismo lento."
            imc in 25.0..29.9 -> " Riesgo de sobrepeso si no mejora el ejercicio."
            pasos < 4000 -> " Nivel de pasos bajo: podrías aumentar de peso."
            frecuencia > 95 -> " Alta frecuencia: posible fatiga o estrés."
            else -> " Ritmo adecuado: condición física saludable si se mantiene."
        }

        tvResultado.text = """
             RESULTADO DE PREDICCIÓN

            Nombre: $nombre
            Edad: $edad años
            Peso: $peso kg
            Estatura: $estatura m
            IMC: ${df.format(imc)} → $imcClasificacion
            Pasos diarios: $pasos
            Frecuencia: $frecuencia bpm
            Calorías estimadas: ${df.format(calorias)}

            $prediccion
        """.trimIndent()
    }
}
