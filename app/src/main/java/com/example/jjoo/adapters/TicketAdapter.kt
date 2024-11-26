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
import com.example.jjoo.data.Purchase
import com.example.jjoo.event.EventActivity
import com.example.jjoo.repositories.EventRepository
import com.example.jjoo.repositories.PurchaseRepository

class TicketAdapter(
    private val context: Context,
    private val listTicket: List<Purchase>
) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    // Crea la vista para cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return ViewHolder(view)
    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int {
        return listTicket.size
    }

    // Enlaza los datos con las vistas
    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val purchase = listTicket[position]

        val event = EventRepository.getById(purchase.eventId)

        // Asigna los valores a los TextView
        holder.txtEventId.text = purchase.eventId.toString()
        holder.txtId.text = purchase.id.toString()
        holder.txtPlace.text = event.place
        holder.txtDate.text = "${event.date} - ${event.hour}"
        holder.txtSeat.text = purchase.seat
        holder.txtCreatedDate.text = purchase.createdDate
        holder.txtAmount.text = "\$${String.format("%.2f", purchase.amount)}"
    }

    // Clase ViewHolder para manejar las vistas
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtEventId: TextView = itemView.findViewById(R.id.txt_eventId)
        val txtId: TextView = itemView.findViewById(R.id.txt_id)
        val txtPlace: TextView = itemView.findViewById(R.id.txt_place)
        val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        val txtSeat: TextView = itemView.findViewById(R.id.txt_seat)
        val txtCreatedDate: TextView = itemView.findViewById(R.id.txt_createdDate)
        val txtAmount: TextView = itemView.findViewById(R.id.txt_amount)
    }
}
