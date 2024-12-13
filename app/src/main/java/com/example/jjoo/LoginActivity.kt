package com.example.jjoo

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth y Realtime Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        // Verificar si ya hay un usuario autenticado
        if (auth.currentUser != null) {
            redirectToMain()
        }

        binding.btnLogin.setOnClickListener {
            validateInfo()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInfo() {
        val identifier = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validar campos vacíos
        if (identifier.isEmpty()) {
            binding.etUsername.error = "Ingrese email o nickname"
            binding.etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
            return
        }

        // Determinar si es email o nickname
        if (Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            // Es un email, iniciar sesión directamente
            loginUser(identifier, password)
        } else {
            // Es un nickname, buscar el email asociado en Realtime Database
            findEmailByNickname(identifier) { email ->
                if (email != null) {
                    // Encontramos el email, proceder al inicio de sesión
                    loginUser(email, password)
                } else {
                    // Nickname no encontrado
                    Toast.makeText(
                        this,
                        "El nickname no está registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    Toast.makeText(this, "Bienvenido, ${firebaseUser?.email}", Toast.LENGTH_SHORT).show()
                    redirectToMain()
                } else {
                    Toast.makeText(
                        this,
                        "Error al iniciar sesión: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun findEmailByNickname(nickname: String, callback: (String?) -> Unit) {
        // Consultar en la base de datos por el nickname
        database.orderByChild("nickName")
            .equalTo(nickname)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Obtener el email del primer usuario encontrado
                        val email = snapshot.children.firstOrNull()
                            ?.child("email")?.getValue(String::class.java)
                        callback(email)
                    } else {
                        callback(null) // Nickname no encontrado
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al buscar nickname: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)
                }
            })
    }

    private fun redirectToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
