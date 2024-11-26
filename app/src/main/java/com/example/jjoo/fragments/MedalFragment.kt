package com.example.jjoo.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jjoo.adapters.EventAdapter
import com.example.jjoo.adapters.MedalTableAdapter
import com.example.jjoo.data.Country
import com.example.jjoo.databinding.FragmentBuyBinding
import com.example.jjoo.databinding.FragmentMedalBinding
import com.example.jjoo.repositories.EventRepository
import com.example.jjoo.repositories.MedalTableRepository

class MedalFragment : Fragment() {

    private lateinit var binding: FragmentMedalBinding
    private lateinit var mContext: Context
    private var medalTableAdapter: MedalTableAdapter? = null
    private var listCountry: List<Country> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMedalBinding.inflate(inflater, container, false)

        binding.rvCountry.setHasFixedSize(true)
        binding.rvCountry.layoutManager = LinearLayoutManager(mContext)

        countryList()

        return binding.root
    }

    private fun countryList() {
        listCountry = MedalTableRepository.get() // Obtiene medallero
        medalTableAdapter = MedalTableAdapter(mContext,listCountry)
        binding.rvCountry.adapter = medalTableAdapter  // Asigna el adaptador
    }
}