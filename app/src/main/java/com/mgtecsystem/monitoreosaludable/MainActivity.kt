package com.mgtecsystem.monitoreosaludable

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etPeso: EditText
    private lateinit var etEdad: EditText
    private lateinit var rgSexo: RadioGroup
    private lateinit var etPasos: EditText
    private lateinit var etFrecuencia: EditText
    private lateinit var btnCalcular: Button
    private lateinit var tvResumen: TextView

    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnListar: Button
    private lateinit var layoutBotonesCrud: LinearLayout

    private val listaMonitoreos = mutableListOf<String>()
    private var nombrePaciente: String = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nombrePaciente = intent.getStringExtra("nombrePaciente") ?: "Paciente"

        etPeso = findViewById(R.id.etPeso)
        etEdad = findViewById(R.id.etEdad)
        rgSexo = findViewById(R.id.rgSexo)
        etPasos = findViewById(R.id.etPasos)
        etFrecuencia = findViewById(R.id.etFrecuencia)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResumen = findViewById(R.id.tvResumen)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnEliminar = findViewById(R.id.btnEliminar)
        btnListar = findViewById(R.id.btnListar)
        layoutBotonesCrud = findViewById(R.id.layoutBotonesCrud)
        layoutBotonesCrud.visibility = View.VISIBLE

        btnCalcular.setOnClickListener {
            if (validarEntradas()) {
                val resumen = obtenerResumen()
                tvResumen.text = resumen
            }
        }

        btnGuardar.setOnClickListener {
            if (validarEntradas()) {
                val resumen = obtenerResumen()
                listaMonitoreos.add(resumen)
                Toast.makeText(this, "Monitoreo guardado", Toast.LENGTH_SHORT).show()
            }
        }

        btnEliminar.setOnClickListener {
            if (listaMonitoreos.isNotEmpty()) {
                listaMonitoreos.removeAt(listaMonitoreos.size - 1)
                Toast.makeText(this, "Último monitoreo eliminado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay registros para eliminar", Toast.LENGTH_SHORT).show()
            }
        }

        btnListar.setOnClickListener {
            tvResumen.text = if (listaMonitoreos.isEmpty()) {
                "No hay registros guardados."
            } else {
                listaMonitoreos.joinToString("\n\n")
            }
        }
    }

    private fun validarEntradas(): Boolean {
        if (etPeso.text.isBlank()) return mostrarError("Ingrese su peso")
        if (etEdad.text.isBlank()) return mostrarError("Ingrese su edad")
        if (rgSexo.checkedRadioButtonId == -1) return mostrarError("Seleccione su sexo")
        if (etPasos.text.isBlank()) return mostrarError("Ingrese los pasos")
        if (etFrecuencia.text.isBlank()) return mostrarError("Ingrese la frecuencia cardíaca")
        return true
    }

    private fun mostrarError(mensaje: String): Boolean {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
        return false
    }

    private fun obtenerSexo(): String {
        return when (rgSexo.checkedRadioButtonId) {
            R.id.rbMasculino -> "Masculino"
            R.id.rbFemenino -> "Femenino"
            else -> "Desconocido"
        }
    }

    private fun calcularCalorias(peso: Double, pasos: Int, sexo: String): Double {
        val coef = if (sexo == "Masculino") 0.05 else 0.04
        return pasos * coef
    }

    private fun obtenerResumen(): String {
        val peso = etPeso.text.toString().toDouble()
        val edad = etEdad.text.toString().toInt()
        val sexo = obtenerSexo()
        val pasos = etPasos.text.toString().toInt()
        val frecuencia = etFrecuencia.text.toString().toInt()
        val calorias = calcularCalorias(peso, pasos, sexo)

        return """
            Nombre: $nombrePaciente
            Peso: $peso kg
            Edad: $edad años
            Sexo: $sexo
            Pasos: $pasos
            Frecuencia cardíaca: $frecuencia bpm
            Calorías quemadas: ${"%.2f".format(calorias)} kcal
        """.trimIndent()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val screenHeight = resources.displayMetrics.heightPixels

            Toast.makeText(this, "Toque en x: $x, y: $y", Toast.LENGTH_SHORT).show()
            if (x < 200 && y > screenHeight - 200) {
                layoutBotonesCrud.visibility =
                    if (layoutBotonesCrud.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
        return super.onTouchEvent(event)
    }
}