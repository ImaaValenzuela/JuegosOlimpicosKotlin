package com.example.jjoo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jjoo.R
import com.example.jjoo.data.Country
import com.example.jjoo.data.Event

class MedalTableAdapter(
    private val context: Context,
    private val listCountry: List<Country>
) : RecyclerView.Adapter<MedalTableAdapter.ViewHolder>() {

    // Crea la vista para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false)
        return ViewHolder(view)
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return listCountry.size
    }

    // Enlaza los datos con las vistas
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = listCountry[position]

        // Asigna los valores a los TextView e ImageView
        holder.textCountry.text = country.name
        holder.textGold.text = "🥇 ${country.goldMedals}"
        holder.textSilver.text = "🥈 ${country.silverMedals}"
        holder.textBronze.text = "🥉 ${country.bronzeMedals}"

        // Usar Glide para cargar la imagen
        Glide.with(context)
            .load(country.flag) // URL del logo
            .placeholder(R.drawable.ic_flag) // Placeholder
            .into(holder.image) // Imagen del pais
    }

    // Clase ViewHolder para manejar las vistas
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCountry: TextView = itemView.findViewById(R.id.item_country)
        val textGold: TextView = itemView.findViewById(R.id.item_gold)
        val textSilver: TextView = itemView.findViewById(R.id.item_silver)
        val textBronze: TextView = itemView.findViewById(R.id.item_bronze)
        val image: ImageView = itemView.findViewById(R.id.iv_flag)
    }
}
