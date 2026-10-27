package com.example.upeventas.model

data class MealResponse(
    // Confirmacion de la respesta exitosa o no
    // saber si da error o que nos da
    val meals: List<Platillo>?
)