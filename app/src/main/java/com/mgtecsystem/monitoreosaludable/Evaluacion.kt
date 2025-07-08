package com.mgtecsystem.monitoreosaludable

import java.io.Serializable

data class Evaluacion(
    val nombre: String,
    val peso: Double,
    val edad: Int,
    val pasos: Int,
    val frecuencia: Int,
    val calorias: Double,
    val fechaHora: String
) : java.io.Serializable
