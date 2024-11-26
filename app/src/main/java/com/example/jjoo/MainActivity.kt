package com.example.jjoo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.databinding.ActivityMainBinding
import com.example.jjoo.fragments.BuyFragment
import com.example.jjoo.fragments.MedalFragment
import com.example.jjoo.fragments.UserFragment
import com.example.jjoo.utils.CurrentUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si el usuario está autenticado
        CurrentUser.loadUser(this)
        if (CurrentUser.user == null) {
            // Si no hay usuario, redirigir a LoginActivity
            optionsLogin()
            return
        }

        // Fragmento por defecto
        seeProfile()

        binding.bottomNV.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_profile -> {
                    // Ver perfil
                    seeProfile()
                    true
                }
                R.id.item_medalTable -> {
                    // Ver medallero
                    seeMedalTable()
                    true
                }
                R.id.item_buy -> {
                    // Ver compras
                    seeBuys()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun optionsLogin() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish() // Finalizar MainActivity para evitar que el usuario regrese
    }

    private fun seeProfile() {
        binding.tvTitle.text = "Perfil"

        val fragment = UserFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentFL.id, fragment, "Fragment Profile")
        fragmentTransaction.commit()
    }

    private fun seeMedalTable() {
        binding.tvTitle.text = "Medallero olimpico"

        val fragment = MedalFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentFL.id, fragment, "Fragment Medal Table")
        fragmentTransaction.commit()
    }

    private fun seeBuys() {
        binding.tvTitle.text = "Eventos disponibles"

        val fragment = BuyFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.fragmentFL.id, fragment, "Fragment Buys")
        fragmentTransaction.commit()
    }
}
