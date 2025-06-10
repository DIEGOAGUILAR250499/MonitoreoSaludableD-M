package com.mgtecsystem.monitoreosaludable

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var etNombrePaciente: EditText
    private lateinit var btnEmpecemos: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        etNombrePaciente = findViewById(R.id.etNombrePaciente)
        btnEmpecemos = findViewById(R.id.btnEmpecemos)

        btnEmpecemos.setOnClickListener {
            val nombre = etNombrePaciente.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("nombrePaciente", nombre)
                startActivity(intent)
            }
        }
    }
}

