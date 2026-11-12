package com.example.upeventas

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)
    }

    /*private fun menuNavegacion(){
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
    }*/
}