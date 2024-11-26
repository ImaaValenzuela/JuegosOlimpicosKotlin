package com.example.jjoo.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jjoo.R
import com.example.jjoo.data.Event
import com.example.jjoo.event.EventActivity

class EventAdapter(
    private val context: Context,
    private val listEvent: List<Event>
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    // Crea la vista para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_events, parent, false)
        return ViewHolder(view)
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return listEvent.size
    }

    // Enlaza los datos con las vistas
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = listEvent[position]

        // Asigna los valores a los TextView e ImageView
        holder.textSport.text = event.sport.name // Nombre del deporte
        holder.textPlace.text = event.place // Lugar del evento
        holder.textDate.text = event.date // Fecha del evento
        holder.textPrice.text = "$${event.price}" // Precio del evento

        // Usar Glide para cargar la imagen
        Glide.with(context)
            .load(event.sport.logo) // URL del logo
            .placeholder(R.drawable.ic_event) // Placeholder
            .into(holder.image) // Imagen del evento

        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventActivity::class.java)
            intent.putExtra("id", event.id.toString()) // Selecciono el ID del evento
            Toast.makeText(
                context,
                "Has seleccionado ${holder.textSport.text}",
                Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
    }

    // Clase ViewHolder para manejar las vistas
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.txt_id)
        val textSport: TextView = itemView.findViewById(R.id.txt_sport)
        val textPlace: TextView = itemView.findViewById(R.id.txt_place)
        val textDate: TextView = itemView.findViewById(R.id.txt_date)
        val textPrice: TextView = itemView.findViewById(R.id.txt_price)
        val image: ImageView = itemView.findViewById(R.id.ic_event)
    }
}
