package com.example.jjoo.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.R
import com.example.jjoo.data.tickets.TicketElite
import com.example.jjoo.data.tickets.TicketPro
import com.example.jjoo.data.tickets.TicketUltimateEvent
import com.example.jjoo.data.tickets.Trade
import com.example.jjoo.databinding.ActivityEventBinding
import com.example.jjoo.repositories.EventRepository
import com.example.jjoo.utils.EventNotFoundException

class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding
    private var id = ""

    private lateinit var backButton: ImageView
    private lateinit var buyTicketButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backButton = findViewById(R.id.IbBack)
        buyTicketButton = findViewById(R.id.buy_ticket_button)

        id = intent.getStringExtra("id") ?: ""
        if (id.isBlank()) {
            showError("No event ID provided.")
            return
        }

        loadInfo()
        setupTicketSelection()


        binding.IbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // No funciona
        }


        buyTicketButton.setOnClickListener {
            // TODO: Implementar logica
            val selectedTicket = getSelectedTicket()
            Toast.makeText(this, "Ticket Selected: $selectedTicket", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadInfo() {
        try {
            val eventId = id.toLongOrNull()
            if (eventId == null) {
                showError("Invalid event ID format.")
                return
            }

            val event = EventRepository.getById(eventId)

            binding.apply {
                txtSport.text = event.sport.name
                txtPlace.text = event.place
                txtDay.text = event.day.name
                txtNumberDay.text = event.date
                txtHour.text = event.hour.toString()
                txtFinalPrice.text = event.price.toString()
            }
        } catch (e: EventNotFoundException) {
            showError(e.message ?: "Event not found.")
        } catch (e: Exception) {
            Log.e("EventActivity", "Unexpected error occurred", e)
            showError("An unexpected error occurred. Please try again later.")
        }
    }

    private fun setupTicketSelection() {
        // Obtenemos el RadioGroup
        val radioGroup = binding.ticketTypeGroup

        // Establecer un listener para cambios de selección
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedTicket = when (checkedId) {
                R.id.ticket_type_pro -> TicketPro()
                R.id.ticket_type_elite -> TicketElite()
                R.id.ticket_type_ultimate -> TicketUltimateEvent()
                else -> null
            }

            selectedTicket?.let {
                updatePrice(it)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun updatePrice(trade: Trade) {
        val event = EventRepository.getById(id.toLong())  // Suponemos que tienes el id del evento aquí.
        val finalPrice = trade.tradeTicket(event) // Calculamos el precio final

        // Formatear el precio con 2 decimales
        val formattedPrice = String.format("%.2f", finalPrice)

        binding.txtFinalPrice.text = "Precio final: $$formattedPrice" // Actualizamos el texto en el UI
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getSelectedTicket(): String {
        // Aquí puedes agregar lógica para obtener el tipo de ticket seleccionado
        val radioGroup = binding.ticketTypeGroup
        return when (radioGroup.checkedRadioButtonId) {
            R.id.ticket_type_pro -> "Pro Ticket"
            R.id.ticket_type_elite -> "Elite Ticket"
            R.id.ticket_type_ultimate -> "Ultimate Ticket"
            else -> "No ticket selected"
        }
    }
}
