package com.example.jjoo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.data.User
import com.example.jjoo.databinding.ActivityRegisterBinding
import com.example.jjoo.repositories.UserRepository
import com.example.jjoo.utils.CurrentUser
import com.example.jjoo.utils.PasswordValidator
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            validateInfo()
        }
    }

    private var name = ""
    private var surname = ""
    private var username = ""
    private var password = ""
    private var Rpassword = ""

    private fun validateInfo() {
        // Obtener los valores de los campos
        name = binding.etName.text.toString().trim()
        surname = binding.etSurname.text.toString().trim()
        username = binding.etUsername.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        Rpassword = binding.etRpassword.text.toString().trim()

        // Validar cada campo secuencialmente y detenerse en el primer error
        if (name.isEmpty()) {
            binding.etName.error = "Ingrese nombre"
            binding.etName.requestFocus()
            return
        }

        if (surname.isEmpty()) {
            binding.etSurname.error = "Ingrese apellido"
            binding.etSurname.requestFocus()
            return
        }

        if (username.isEmpty()) {
            binding.etUsername.error = "Ingrese nombre de usuario"
            binding.etUsername.requestFocus()
            return
        }

        if (UserRepository.findByNickname(username) != null) {
            binding.etUsername.error = "Nombre de Usuario en uso o inválido. Por favor ingrese uno nuevo"
            binding.etUsername.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese contraseña"
            binding.etPassword.requestFocus()
            return
        }

        if (!PasswordValidator.validate(password)) {
            binding.etPassword.error = "Contraseña inválida. Asegúrese de que contenga una mayúscula, una minúscula y un número. No debe tener espacios en blanco y que contenga entre 8 y 16 caracteres"
            binding.etPassword.requestFocus()
            return
        }

        if (Rpassword.isEmpty()) {
            binding.etRpassword.error = "Repita la contraseña"
            binding.etRpassword.requestFocus()
            return
        }

        if (password != Rpassword) {
            binding.etRpassword.error = "No coinciden las contraseñas"
            binding.etRpassword.requestFocus()
            return
        }

        // Si todas las validaciones pasan, intenta registrar al usuario
        registerUser()
    }

    private fun registerUser() {
        // Obtener la fecha actual en formato "yyyy/MM/dd"
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        val currentDate = LocalDate.now().format(dateFormatter)

        val user = User(
            id = UserRepository.generateRandomId(),
            nickName = username,
            password = password,
            name = name,
            surname = surname,
            createdDate = currentDate
        )
        // Agrega el usuario al repositorio
        UserRepository.add(user)

        CurrentUser.user = user

        // Referencia al menú principal
        startActivity(Intent(applicationContext, MainActivity::class.java))
        finishAffinity() // Se asegura que el usuario no puede regresar a la pantalla de registro
    }
}
