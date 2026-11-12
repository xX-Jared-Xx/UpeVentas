package com.example.upeventas


import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    private lateinit var imgPortadaDetalle : ImageView
    private lateinit var btnFavorito : ImageButton
    private lateinit var  tvNombre :  TextView
    private lateinit var  tvCategoria : TextView
    private lateinit var  tvDetalle : TextView

    private lateinit var db : Databasehelper
    private  var platilloFavorito = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        imgPortadaDetalle = findViewById(R.id.imgPortadaDetalle)
        btnFavorito = findViewById(R.id.btnFavorito)
        tvNombre = findViewById(R.id.tvNombre)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvDetalle = findViewById(R.id.tvDetalle)
        db = Databasehelper(this)

        mostrarInformacionPlatillo()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    // metodo para mostrar la informacion
    private fun mostrarInformacionPlatillo(){

        val idPlato = intent.getLongExtra("ID_PLATILLO", 1)
        val nombrePlato = intent.getStringExtra("NOMBRE_PLATILLO") ?: "Platillo desconocido"
        val categoria = intent.getStringExtra("CATEGORIA_PLATILLO") ?: "Sin Categoria"
        val instrucciones = intent.getStringExtra("INSTRUCCIONES_PLATILLO") ?: "Sin instrucciones"
        val imagen = intent.getStringExtra("IMAGEN_PLATILLO") ?: "https://thumb.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/330px-No-Image-Placeholder.svg.png"

        tvNombre.text = nombrePlato
        tvCategoria.text = categoria
        tvDetalle.text = "$instrucciones"

        Glide.with(this)
            .load(imagen)
            .into(imgPortadaDetalle)

        //Vertoficar que el platillo exista como favorito
        if(idPlato != -1L){
            platilloFavorito = db.existePlatilloFavorito(idPlato)
            btnFavorito.setBackgroundResource(R.drawable.megusta2)
        }

        btnFavorito.setOnClickListener {

            //platilloFavorito = !platilloFavorito

            if (platilloFavorito){
                val eliminado = db.eliminarPlatilloFavorito(idPlato)
                if(eliminado) {
                    btnFavorito.setBackgroundResource(R.drawable.megusta)
                    platilloFavorito = !platilloFavorito
                    Toast.makeText(this, "$nombrePlato Eliminado de Favoritos", Toast.LENGTH_SHORT).show()
                }
            }else{
                //btnFavorito.setBackgroundResource(R.drawable.megusta1)
                //Toast.makeText(this, "$nombrePlato Eliminado de Favoritos", Toast.LENGTH_SHORT).show()

                val guardado = db.guardarPlatilloFavoritos(
                    platilloId=idPlato,
                    nombrePlatillo = nombrePlato,
                    categ = categoria,
                    instruc = instrucciones,
                    img = imagen
                )
                //val guardado = true;

                btnFavorito.setBackgroundResource(R.drawable.megusta2)

                //Evaluar si la inserccion fue correcta
                if(guardado){
                    platilloFavorito = !platilloFavorito
                    Toast.makeText(this, "$nombrePlato Agregado a Favoritos", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Ya se encuentra en Favoritos", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

}