package com.example.upeventas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.upeventas.model.MealResponse
import com.example.upeventas.model.Platillo
import com.example.upeventas.network.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProduVendedorActivity : AppCompatActivity() {

    private lateinit var tvResultado: TextView
    private lateinit var tvResultado2: TextView
    private lateinit var tvResultado3: TextView
    private lateinit var contenedorUno : LinearLayout
    private lateinit var contenedorDos : LinearLayout
    private lateinit var ContenedorTres : LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_produ_vendedor)

        bottomNavigation = findViewById(R.id.bottomNavigation)
        tvResultado = findViewById(R.id.tvResultado)
        tvResultado2 = findViewById(R.id.tvResultado2)
        tvResultado3 = findViewById(R.id.tvResultado3)
        contenedorUno = findViewById(R.id.contenedoruno)
        contenedorDos = findViewById(R.id.contenedorSeg)
        ContenedorTres = findViewById(R.id.contenedorTres)

        menuNavegacion()

        // AGREGAR METODO LLAMADO
        obtenerPlatillo("pasta", contenedorUno)
        obtenerPlatillo("taco", contenedorDos)
        obtenerPlatillo("Japanese", ContenedorTres)
    }


    // METODO PARA OBTENER INFORMACION
    private fun obtenerPlatillo(genero : String, contenedor : LinearLayout) {
        // Hacer el llamado a la construccion de la URL
        RetrofitClient.api.obtenerPlatillos(
            busqueda = genero
        ).enqueue(object : Callback<MealResponse?> {
            override fun onResponse(
                call: Call<MealResponse?>,
                response: Response<MealResponse?>
            ) {
                // variable para guardar
                val platillos = response.body()?.meals

                // validar si la variable tiene informacon
                if (!platillos.isNullOrEmpty()) {
                    tvResultado.visibility = View.GONE
                    tvResultado2.visibility = View.GONE
                    tvResultado3.visibility = View.GONE
                    mostrarPlatillos(platillos, contenedor)
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
    private fun mostrarPlatillos(platillos : List<Platillo>, contenedor: LinearLayout){
        contenedor.removeAllViews()

        // ciclo para recorrer los jultiples peticiones de una api
        for(platillo in platillos){
            val vistaPlatillo = layoutInflater.inflate(
                R.layout.item_comida,
                contenedor,
                false
            )

            val imgPortada = vistaPlatillo.findViewById<ImageView>(R.id.imgComida)
            val tvNombrePlato = vistaPlatillo.findViewById<TextView>(R.id.tvNombrePlato)
            val tvNombrePlatillo = vistaPlatillo.findViewById<TextView>(R.id.tvNombrePlatillo)

            // Asignando los nombres obtenidos del api
            tvNombrePlato.text = platillo.strMeal ?: "Plato sin nombre"
            tvNombrePlatillo.text = platillo.strCategory ?: "Platillo sin  categoria"

            //Mostrar la imagen en la tarjeta
            Glide.with(this@ProduVendedorActivity)
                .load(platillo.obtenerImagnGrnde())
                .into(imgPortada)

            vistaPlatillo.setOnClickListener {

                // necesitamos obtemer el ID
                val idPlato = platillo.idMeal

                if (idPlato != null){
                    val intent = Intent(this@ProduVendedorActivity, DetailsActivity::class.java)

                    //enviar datos
                    intent.putExtra("ID_PLATILLO", idPlato)
                    intent.putExtra("NOMBRE_PLATILLO", platillo.strMeal)
                    intent.putExtra("CATEGORIA_PLATILLO", platillo.strCategory)
                    intent.putExtra("INSTRUCCIONES_PLATILLO", platillo.strInstructions)
                    intent.putExtra("IMAGEN_PLATILLO", platillo.obtenerImagnGrnde())

                    startActivity(intent)


                }
            }
            
            contenedor.addView(vistaPlatillo)
        }
    }

    //Metodo para el funcionamiento del menu
    private fun menuNavegacion(){
        //Verificar la opcion seleccionada del menu
        bottomNavigation.selectedItemId = R.id.nav_inicio

        bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_inicio -> {
                    true
                }
                R.id.nav_fav -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}