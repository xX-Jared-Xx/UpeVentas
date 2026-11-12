package com.example.upeventas

import android.content.Intent
import androidx.credentials.CredentialManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.CreateCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var etCorreo : EditText
    private lateinit var  etPassword: EditText
    private lateinit var  btnIngresar: Button
    private lateinit var  dbHelper: Databasehelper

    //Crear objetos para el funcionamiento con Google
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager : CredentialManager
    private lateinit var btnGoogle : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        etCorreo = findViewById(R.id.etMatricula)
        etPassword = findViewById(R.id.etPass)
        btnIngresar = findViewById(R.id.btnLogin)
        dbHelper = Databasehelper(this)

        btnGoogle = findViewById(R.id.btnGoogle)
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(this);

        // Evento pra iniciar sesion
        btnIngresar.setOnClickListener {
            // llamara al metodo iniciar sesion
            iniciarSesion()

        }

        btnGoogle.setOnClickListener {
            iniciarSesionGoogle()
        }

        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, CrearCuentaActivity::class.java)
            startActivity(intent)
        }
    }

    // Metdo para iniciar sesion
    // metodo privado
    private fun iniciarSesion(){
        val correo = etCorreo.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        if(correo.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        val resultado = dbHelper.iniciarSesion(correo, pass)

        // validar si el iniio de sesion fue correcto
        if(resultado){
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ProduVendedorActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            // resultado es falso
            Toast.makeText(this, "Credenciales Icorrectas", Toast.LENGTH_SHORT).show()
        }


    }

    // 3. MÉTODO PARA AUTENTICAR EN FIREBSAE
    private fun autenticarFireBase(idToken: String){

        val credencialFirebase = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credencialFirebase).addOnCompleteListener(this) {

                inicio ->
                //validar si el inicio de seion es correcto
                if(inicio.isSuccessful){

                    //crear Variable para guardar al usuario actual
                    val usuario = auth.currentUser
                    Toast.makeText(this, "Bienvenido ${usuario?.displayName}", Toast.LENGTH_SHORT).show()

                    //Enviar al Usuario a HomeActivity
                    val intent = Intent(this, ProduVendedorActivity::class.java)
                    startActivity(intent)
                    finish()

                }else{
                    Toast.makeText(this, "(x) Error al inciar con FireBase", Toast.LENGTH_SHORT).show()
                }

        }

    }

    private fun procesarCredencial(credential: Credential){

        if(credential is CustomCredential && credential.type ==
            GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){

            try {
                //validar la credencial del provedor
                val credencialGoogle = GoogleIdTokenCredential.createFrom(
                    credential.data
                )

                 //Iniciar Proceso de Autenticacion
                autenticarFireBase(credencialGoogle.idToken)

            }catch (e : GoogleIdTokenParsingException){
                Toast.makeText(this, "(x) Algo ha salido mal :(", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun iniciarSesionGoogle(){

        val inicioGoogle = GetSignInWithGoogleOption.Builder(
            getString(R.string.default_web_client_id)).build()

        val solicitud = GetCredentialRequest.Builder()
            .addCredentialOption(inicioGoogle)
            .build()
        //Proceso para Iniciar con FIrebase
        lifecycleScope.launch {
            try{
                //Verificar que el resultado sea correcto
                val resultado = credentialManager.getCredential(
                    context = this@MainActivity,
                    request = solicitud
                )

                //LLamar al metodo que permite procesar la credencial
                procesarCredencial(resultado.credential)

            }catch(e: CreateCredentialException){
                Toast.makeText(this@MainActivity, "(x) Algo ha salido mal :(", Toast.LENGTH_SHORT).show()
            }
        }

    }
}