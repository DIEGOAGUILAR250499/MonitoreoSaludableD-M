package com.mgtecsystem.monitoreosaludable

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var etNombrePaciente: EditText
    private lateinit var btnGuardarUsuario: Button
    private lateinit var btnBorrarTodo: Button
    private lateinit var btnIrSiguiente: Button
    private lateinit var btnSeleccionar: Button
    private lateinit var btnOpcionesCrud: Button
    private lateinit var layoutBotonesCrud: LinearLayout
    private lateinit var tvNombreRegistrado: TextView
    private lateinit var layoutListaNombres: LinearLayout

    private var ultimoNombreRegistrado: String? = null
    private val listaNombres = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        etNombrePaciente = findViewById(R.id.etNombrePaciente)
        btnGuardarUsuario = findViewById(R.id.btnGuardar)
        btnBorrarTodo = findViewById(R.id.btnEliminar)
        btnIrSiguiente = findViewById(R.id.btnIrSiguientePagina)
        btnSeleccionar = findViewById(R.id.btnSeleccionar)
        btnOpcionesCrud = findViewById(R.id.btnOpcionesCrud)
        layoutBotonesCrud = findViewById(R.id.layoutBotonesCrud)
        tvNombreRegistrado = findViewById(R.id.tvNombreRegistrado)
        layoutListaNombres = findViewById(R.id.layoutListaNombres)

        btnOpcionesCrud.setOnClickListener {
            layoutBotonesCrud.visibility =
                if (layoutBotonesCrud.visibility == View.GONE) View.VISIBLE else View.GONE
        }

        btnGuardarUsuario.setOnClickListener {
            val nombre = etNombrePaciente.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
            } else {
                tvNombreRegistrado.text = "Nombre ingresado: $nombre"
                ultimoNombreRegistrado = nombre

                if (!listaNombres.contains(nombre)) {
                    listaNombres.add(nombre)

                    val nuevoNombre = TextView(this).apply {
                        text = nombre
                        textSize = 16f
                    }
                    layoutListaNombres.addView(nuevoNombre)
                }

                etNombrePaciente.text.clear()
            }
        }

        // Botón Eliminar todo
        btnBorrarTodo.setOnClickListener {
            etNombrePaciente.text.clear()
            tvNombreRegistrado.text = "Nombre ingresado: "
            layoutListaNombres.removeAllViews()
            listaNombres.clear()
            ultimoNombreRegistrado = null
            Toast.makeText(this, "Nombres borrados", Toast.LENGTH_SHORT).show()
        }

        // Botón Seleccionar nombre
        btnSeleccionar.setOnClickListener {
            if (listaNombres.isEmpty()) {
                Toast.makeText(this, "No hay nombres registrados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nombresArray = listaNombres.toTypedArray()

            AlertDialog.Builder(this)
                .setTitle("Selecciona un nombre")
                .setItems(nombresArray) { _, which ->
                    val nombreSeleccionado = nombresArray[which]
                    etNombrePaciente.setText(nombreSeleccionado)
                    tvNombreRegistrado.text = "Nombre ingresado: $nombreSeleccionado"
                    ultimoNombreRegistrado = nombreSeleccionado
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        // Botón Siguiente
        btnIrSiguiente.setOnClickListener {
            val nombreCampo = etNombrePaciente.text.toString().trim()
            val nombreFinal = if (nombreCampo.isNotEmpty()) {
                nombreCampo.also { ultimoNombreRegistrado = it }
            } else {
                ultimoNombreRegistrado
            }

            if (nombreFinal.isNullOrEmpty()) {
                Toast.makeText(this, "Registre su nombre antes de continuar", Toast.LENGTH_SHORT).show()
            } else {
                val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("nombrePaciente", nombreFinal)
                    putExtra("fechaRegistro", fecha)
                    putExtra("horaRegistro", hora)
                }
                startActivity(intent)
            }
        }
    }
}
