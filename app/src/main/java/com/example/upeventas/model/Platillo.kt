package com.example.upeventas.model
class Platillo(

    // SI NO DE INTERROGACION ES PAR QUE NO CRASHEE
    val idMeal: String?,
    val strMeal: String?,
    val strCategory: String?,
    val strArea: String?, // Se llama strArea para que coincida exactamente con la API
    val strMealThumb: String?
) {

    // METODO PARA OBTENER LA IMAGEN MAS GRANDE
    fun obtenerImagnGrnde() : String{
        // en caso de que no encuntre la url va  apasar ua cadena vacia
        return strMealThumb?.replace("100x100bb.jpg", "600x600bb.jpg") ?: ""
    }
}