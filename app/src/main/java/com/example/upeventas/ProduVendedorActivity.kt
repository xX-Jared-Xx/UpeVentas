package com.example.upeventas

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.upeventas.model.MealResponse
import com.example.upeventas.model.Platillo
import com.example.upeventas.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProduVendedorActivity : AppCompatActivity() {

    private lateinit var tvResultado: TextView
    private lateinit var contenedorUno : LinearLayout
    private lateinit var contenedorDos : LinearLayout
    private lateinit var ContenedorTres : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produ_vendedor)

        tvResultado = findViewById(R.id.tvResultado)
        contenedorUno = findViewById(R.id.contenedoruno)
        contenedorDos = findViewById(R.id.contenedorSeg)
        ContenedorTres = findViewById(R.id.contenedorTres)

        // AGREGAR METODO LLAMADO DE ALBUM
        obtenerPlatillo("pasta", contenedorUno)
        obtenerPlatillo("taco", contenedorDos)
        obtenerPlatillo("Japanese", ContenedorTres)

    }


    // METODO PARA OBTENER INFORMACION DEL ALBUM
    private fun obtenerPlatillo(genero : String, contenedor : LinearLayout) {
        // Hacer el llamado a la construccion de la URL
        RetrofitClient.api.obtenerPlatillos(
            busqueda = genero
        ).enqueue(object : Callback<MealResponse?> {
            override fun onResponse(
                call: Call<MealResponse?>,
                response: Response<MealResponse?>
            ) {
                // variable para guardar albumes
                val platillos = response.body()?.meals

                // validar si la variable albumes tiene informacon
                if (!platillos.isNullOrEmpty()) {
                    tvResultado.visibility = View.GONE
                    mostrarAlbumes(platillos, contenedor)
                } else {
                    Toast.makeText(
                        this@ProduVendedorActivity,
                        "No hay respuesta del Api",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // metodo si api falla
            override fun onFailure(call: Call<MealResponse?>, t: Throwable) {
                Toast.makeText(
                    this@ProduVendedorActivity,
                    "ERROR EN EL API: ${t.message}", Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //METODO PARA MOSTRAR EL LISTADO DE DATOS DE UNA API
    private fun mostrarAlbumes(albumes : List<Platillo>, contenedor: LinearLayout){
        contenedor.removeAllViews()

        // ciclo para recorrer los jultiples peticiones de una api
        for(album in albumes){
            val vistaAlbum = layoutInflater.inflate(
                R.layout.item_comida,
                contenedor,
                false
            )

            val imgPortada = vistaAlbum.findViewById<ImageView>(R.id.imgComida)
            val tvNombrePlato = vistaAlbum.findViewById<TextView>(R.id.tvNombrePlato)
            val tvNombrePlatillo = vistaAlbum.findViewById<TextView>(R.id.tvNombrePlatillo)

            // Asignando los nombres obtenidos del api
            tvNombrePlato.text = album.strMeal ?: "Plato sin nombre"
            tvNombrePlatillo.text = album.strCategory ?: "Platillo sin  categoria"

            //Mostrar la imagen en la tarjeta
            Glide.with(this@ProduVendedorActivity)
                .load(album.obtenerImagnGrnde())
                .into(imgPortada)

            // Asignar la vista del contenedor a las tarjetas de albumes
            contenedor.addView(vistaAlbum)
        }
    }
}