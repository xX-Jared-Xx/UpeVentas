package com.example.upeventas

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.upeventas.model.MealResponse
import com.example.upeventas.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProduVendedorActivity : AppCompatActivity() {

    private lateinit var tvResultado: TextView
    private lateinit var imgAlbum: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produ_vendedor)

        tvResultado = findViewById(R.id.tvResultado)
        imgAlbum = findViewById(R.id.imgAlbum)

        // AGREGAR METODO LLAMADO DE ALBUM
        obtenerAlbum(
            regionFiltro = "Italian",
            categoriaFiltro = "Vegetarian",
            busqueda = "Arrabiata"
        )
    }

    // METODO PARA OBTENER INFORMACION DEL ALBUM
    private fun obtenerAlbum(regionFiltro: String, categoriaFiltro: String, busqueda: String) {
        // Hacer el llamdo a la construccion de la URL
        RetrofitClient.api.obtenerPlatillos(busqueda).enqueue(object : Callback<MealResponse?> {
            override fun onResponse(
                call: Call<MealResponse?>,
                response: Response<MealResponse?>
            ) {
                // variable para guardar albumes
                val platillos = response.body()?.meals

                // validar si la variable albumes tiene informacon
                if (!platillos.isNullOrEmpty()) {

                    // Filtrado múltiple en Kotlin evaluando categoría y región (strArea)
                    val resultadosFiltrados = platillos.filter { platillo ->
                        val coincideCategoria = categoriaFiltro.isEmpty() ||
                                platillo.strCategory.equals(categoriaFiltro, ignoreCase = true)

                        val coincideRegion = regionFiltro.isEmpty() ||
                                platillo.strArea.equals(regionFiltro, ignoreCase = true)

                        coincideCategoria && coincideRegion
                    }

                    if (resultadosFiltrados.isNotEmpty()) {
                        // VARIABLE PARA ACCEDER AL PRIMER ELEMENTO DE LOS ALBUMES ENONTRADOS
                        val platillo = resultadosFiltrados[0]
                        tvResultado.text =
                            "Nombre: ${platillo.strMeal} \n\n " +
                                    "Categoría: ${platillo.strCategory} \n\n " +
                                    "Región: ${platillo.strArea}"

                        // MOSTRAR LA IMAGEN DE ALBUM
                        Glide.with(this@ProduVendedorActivity)
                            .load(platillo.obtenerImagnGrnde())
                            .into(imgAlbum)
                    } else {
                        Toast.makeText(
                            this@ProduVendedorActivity,
                            "NO HAY COINCIDENCIAS CON LOS FILTROS",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ProduVendedorActivity,
                        "NO HAY RESPUESTA DE LA API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // metodo si api falla
            override fun onFailure(call: Call<MealResponse?>, t: Throwable) {
                Toast.makeText(
                    this@ProduVendedorActivity,
                    "ERROR EN EL API: ${t.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }
}