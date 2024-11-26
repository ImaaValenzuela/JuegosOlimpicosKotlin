package com.example.jjoo

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jjoo.databinding.ActivityOptionsLoginBinding

class OptionsLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOptionsLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.optionLogin.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        binding.optionRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }
}