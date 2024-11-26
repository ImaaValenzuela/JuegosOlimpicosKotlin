package com.example.jjoo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jjoo.R
import com.example.jjoo.adapters.EventAdapter
import com.example.jjoo.data.Event
import com.example.jjoo.databinding.FragmentBuyBinding
import com.example.jjoo.repositories.EventRepository

class BuyFragment : Fragment() {

    private lateinit var binding: FragmentBuyBinding
    private lateinit var mContext: Context
    private var eventAdapter: EventAdapter? = null
    private var listEvent: List<Event> = ArrayList()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyBinding.inflate(inflater, container, false)

        binding.RVevents.setHasFixedSize(true)
        binding.RVevents.layoutManager = LinearLayoutManager(mContext)

        evenList()

        return binding.root
    }

    private fun evenList() {
        listEvent = EventRepository.get() // Obtiene los eventos
        eventAdapter = EventAdapter(mContext, listEvent)
        binding.RVevents.adapter = eventAdapter // Asigna el adaptador
    }

}