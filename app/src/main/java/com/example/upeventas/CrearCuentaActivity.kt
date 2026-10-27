package com.example.upeventas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CrearCuentaActivity : AppCompatActivity() {

    // Aqui se declaran los elementos
    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegistrar: Button
    //Instancia database helper
    private lateinit var dbHelper: Databasehelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_cuenta)

        etNombre = findViewById(R.id.etNombre)
        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        dbHelper = Databasehelper(this)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    //Metodos por aqui
    private fun registrarUsuario(){

        val nombre  = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if(nombre.isEmpty() || correo.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Completa todos los campos.!",
                Toast.LENGTH_SHORT).show()
            return
        }
        
        // Variable que guarda el resultado del registro y llama a la db
        val registro = dbHelper.registrarUsuario(nombre, correo, password)
        if(registro){
            Toast.makeText(this, "Usuario creado con Exito!",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }else{
            Toast.makeText(this, "Hubo un problema, intentalo de nuevo!",
                Toast.LENGTH_SHORT).show()
        }
    }
}