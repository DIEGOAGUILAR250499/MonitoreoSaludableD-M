package com.mgtecsystem.monitoreosaludable

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etNombrePaciente: EditText
    private lateinit var etPeso: EditText
    private lateinit var etEdad: EditText
    private lateinit var etPasos: EditText
    private lateinit var etFrecuencia: EditText
    private lateinit var tvResumen: TextView

    private val evaluaciones = arrayListOf<Evaluacion>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referencias de UI
        etNombrePaciente = findViewById(R.id.etNombrePaciente)
        etPeso = findViewById(R.id.etPeso)
        etEdad = findViewById(R.id.etEdad)
        etPasos = findViewById(R.id.etPasos)
        etFrecuencia = findViewById(R.id.etFrecuencia)
        tvResumen = findViewById(R.id.tvResumen)

        val btnAcciones = findViewById<Button>(R.id.btnAcciones)
        val btnVerGrafico = findViewById<Button>(R.id.btnVerGrafico)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)

        // ✅ Recibir nombre del paciente desde WelcomeActivity
        val nombrePaciente = intent.getStringExtra("nombrePaciente")
        if (!nombrePaciente.isNullOrEmpty()) {
            etNombrePaciente.setText(nombrePaciente)
        }

        btnCalcular.setOnClickListener { calcular() }

        btnAcciones.setOnClickListener {
            val popup = PopupMenu(this, btnAcciones)
            popup.menuInflater.inflate(R.menu.menu_crud, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_guardar -> guardarDatos()
                    R.id.menu_eliminar -> eliminarTodo()
                }
                true
            }
            popup.show()
        }

        btnVerGrafico.setOnClickListener {
            val intent = Intent(this, GraficoManualActivity::class.java)
            intent.putExtra("evaluaciones", ArrayList(evaluaciones))
            startActivity(intent)
        }
    }

    private fun calcular() {
        val nombre = etNombrePaciente.text.toString().trim()
        val peso = etPeso.text.toString().toDoubleOrNull()
        val edad = etEdad.text.toString().toIntOrNull()
        var pasos = etPasos.text.toString().toIntOrNull()
        var frecuencia = etFrecuencia.text.toString().toIntOrNull()

        if (nombre.isEmpty() || peso == null || edad == null) {
            Toast.makeText(this, "Ingresa nombre, peso y edad", Toast.LENGTH_SHORT).show()
            return
        }

        if (pasos == null) {
            pasos = (3000..8000).random()
            etPasos.setText(pasos.toString())
        }

        if (frecuencia == null) {
            frecuencia = (60..100).random()
            etFrecuencia.setText(frecuencia.toString())
        }

        val calorias = (peso * 0.5) + (pasos * 0.02) + (frecuencia * 0.1)
        val resumen = """
            Nombre: $nombre
            Peso: $peso kg
            Edad: $edad años
            Pasos: $pasos
            Frecuencia: $frecuencia bpm
            Calorías estimadas: %.2f
        """.trimIndent().format(calorias)

        tvResumen.text = resumen
    }

    private fun guardarDatos() {
        val nombre = etNombrePaciente.text.toString().trim()
        val peso = etPeso.text.toString().toDoubleOrNull()
        val edad = etEdad.text.toString().toIntOrNull()
        val pasos = etPasos.text.toString().toIntOrNull()
        val frecuencia = etFrecuencia.text.toString().toIntOrNull()

        if (nombre.isEmpty() || peso == null || edad == null || pasos == null || frecuencia == null) {
            Toast.makeText(this, "Completa todos los campos correctamente para guardar", Toast.LENGTH_SHORT).show()
            return
        }

        val calorias = (peso * 0.5) + (pasos * 0.02) + (frecuencia * 0.1)
        val fechaHora = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val evaluacion = Evaluacion(nombre, peso, edad, pasos, frecuencia, calorias, fechaHora)
        evaluaciones.add(evaluacion)

        tvResumen.text = """
            Guardado:
            Nombre: $nombre
            Peso: %.2f kg
            Edad: $edad años
            Pasos: $pasos
            Frecuencia: $frecuencia bpm
            Calorías: %.2f
            Fecha: $fechaHora
        """.trimIndent().format(peso, calorias)

        Toast.makeText(this, "Evaluación guardada", Toast.LENGTH_SHORT).show()
    }

    private fun eliminarTodo() {
        evaluaciones.clear()
        Toast.makeText(this, "Todos los datos fueron eliminados", Toast.LENGTH_SHORT).show()
        tvResumen.text = ""
        etNombrePaciente.text.clear()
        etPeso.text.clear()
        etEdad.text.clear()
        etPasos.text.clear()
        etFrecuencia.text.clear()
    }
}
