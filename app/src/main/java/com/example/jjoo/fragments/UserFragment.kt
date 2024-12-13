package com.example.jjoo.fragments

import android.annotation.SuppressLint
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
import com.example.jjoo.repositories.UserRepository
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

        binding.btnHistoryBuys.setOnClickListener {
            historyBuys()
        }

        binding.btnLogout.setOnClickListener{
            logout()
        }
    }

    private fun historyBuys() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentFL, HistoryFragment())
        transaction.addToBackStack(null) // Esto permite volver al fragmento anterior
        transaction.commit()
    }

    @SuppressLint("SetTextI18n")
    private fun loadInfo() {
        // Suponiendo que tienes el ID del usuario actual guardado en SharedPreferences
        val currentUserId = CurrentUser.user?.id ?: return

        UserRepository.findById(currentUserId,
            onSuccess = { user ->
                if (user != null) {
                    binding.tvNames.text = "${user.name} ${user.surname}"
                    binding.tvUsername.text = user.nickName
                    binding.tvTRegister.text = user.createdDate
                    binding.tvMoney.text = "${user.money}"
                } else {
                    binding.tvNames.text = "Usuario no encontrado"
                }
            },
            onFailure = { error ->
                // Manejar error
                binding.tvNames.text = "Error al cargar usuario: ${error.message}"
            }
        )
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
