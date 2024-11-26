package com.example.jjoo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.databinding.ActivityLoginBinding
import com.example.jjoo.repositories.UserRepository
import com.example.jjoo.utils.CurrentUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si ya hay un usuario autenticado
        CurrentUser.loadUser(this)  // Cargar el usuario almacenado
        if (CurrentUser.user != null) {
            // Si el usuario ya está autenticado, redirigir a MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

        binding.btnLogin.setOnClickListener {
            validateInfo()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }

    private fun validateInfo() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validar campos vacíos
        if (username.isEmpty()) {
            binding.etUsername.error = "Ingrese nombre de usuario"
            binding.etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
            return
        }

        // Intentar iniciar sesión
        val user = UserRepository.login(nickname = username, password = password)
        if (user != null) {
            // Guardar el usuario actual en SharedPreferences
            CurrentUser.setUser(this, user)

            // Redirigir al usuario a MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        } else {
            // Mostrar mensaje de error
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
        }
    }
}
