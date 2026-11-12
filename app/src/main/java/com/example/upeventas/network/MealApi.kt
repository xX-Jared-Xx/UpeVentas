package com.example.upeventas.network


import com.example.upeventas.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    // retroft para conexion a internet
    // INDICAR METODO HTTP A USAR
    @GET("search.php")

    // Metodo para obtener la informacion con la url de la API
    fun obtenerPlatillos(
        // sustituir terminos
        @Query("s") busqueda : String,
        ) : retrofit2.Call<MealResponse>
}