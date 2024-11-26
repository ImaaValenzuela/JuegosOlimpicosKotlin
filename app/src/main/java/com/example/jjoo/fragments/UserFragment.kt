package com.example.jjoo.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jjoo.OptionsLoginActivity
import com.example.jjoo.R
import com.example.jjoo.databinding.FragmentUserBinding
import com.example.jjoo.utils.CurrentUser

class UserFragment : Fragment() {

    private lateinit var binding : FragmentUserBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadInfo()


        binding.btnLogout.setOnClickListener{
            logout()
        }
    }

    private fun loadInfo() {
        // Cargar al usuario actual desde SharedPreferences
        CurrentUser.loadUser(mContext)

        // Verificar si hay un usuario cargado
        val currentUser = CurrentUser.user
        if (currentUser != null) {
            binding.tvNames.text = "${currentUser.name} ${currentUser.surname}"
            binding.tvUsername.text = currentUser.nickName
            binding.tvTRegister.text = currentUser.createdDate
        } else {
            // Usuario no encontrado
            binding.tvNames.text = "Usuario no encontrado"
            binding.tvUsername.text = "N/A"
            binding.tvTRegister.text = "N/A"
        }
    }

    private fun logout() {
        // Limpiar el usuario actual
        CurrentUser.clearUser(mContext)

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(mContext, OptionsLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        // Finalizar el fragmento actual
        activity?.finish()
    }

}