package com.example.jjoo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jjoo.R
import com.example.jjoo.adapters.TicketAdapter
import com.example.jjoo.data.Purchase
import com.example.jjoo.databinding.FragmentHistoryBinding
import com.example.jjoo.repositories.PurchaseRepository
import com.example.jjoo.utils.CurrentUser

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var mContext: Context
    private var ticketAdapter: TicketAdapter? = null
    private var listTicket: List<Purchase> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.RVtickets.setHasFixedSize(true)
        binding.RVtickets.layoutManager = LinearLayoutManager(mContext)

        loadPurchases()

        return binding.root
    }

    private fun loadPurchases() {
        // Obtiene los tickets de todas las compras
        val allTickets = PurchaseRepository.get()

        // Filtra los tickets para mostrar solo los del usuario actual
        val currentUser = CurrentUser.user
        if (currentUser != null) {
            // Filtrar por ID de usuario
            listTicket = allTickets.filter { it.userId == currentUser.id }
        } else {
            listTicket = emptyList()
        }

        // Asigna el adaptador con los tickets filtrados
        ticketAdapter = TicketAdapter(mContext, listTicket)
        binding.RVtickets.adapter = ticketAdapter
    }
}