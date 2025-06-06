package com.mgtecsystem.monitoreosaludable

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val etNombre = findViewById<EditText>(R.id.etNombrePaciente)
        val btnEmpezar = findViewById<Button>(R.id.btnEmpecemos)

        btnEmpezar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("nombrePaciente", nombre)
                startActivity(intent)
            }
        }
    }
}