package com.example.jjoo.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jjoo.R
import com.example.jjoo.data.User
import com.example.jjoo.data.tickets.TicketElite
import com.example.jjoo.data.tickets.TicketPro
import com.example.jjoo.data.tickets.TicketUltimateEvent
import com.example.jjoo.data.tickets.Trade
import com.example.jjoo.databinding.ActivityEventBinding
import com.example.jjoo.fragments.UserFragment
import com.example.jjoo.repositories.EventRepository
import com.example.jjoo.utils.CurrentUser
import com.example.jjoo.utils.EventNotFoundException
import com.example.jjoo.utils.PurchaseService

class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding
    private var id = ""

    private lateinit var backButton: ImageView
    private lateinit var buyTicketButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // backButton = findViewById(R.id.IbBack)
        buyTicketButton = findViewById(R.id.buy_ticket_button)

        id = intent.getStringExtra("id") ?: ""
        if (id.isBlank()) {
            showError("No event ID provided.")
            return
        }

        // Cargar usuario activo desde CurrentUser
        CurrentUser.loadUser(this)

        // Verificar si hay un usuario activo
        val user = CurrentUser.user
        if (user == null) {
            showError("No active user found. Please log in.")
            return
        }

        loadInfo()
        setupTicketSelection()

        /* backButton.setOnClickListener {
            Log.d("EventActivity", "Back button clicked")
            finish()
        }

        binding.IbBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        } */
        binding.tvCancel.setOnClickListener{
            finish()
        }
        buyTicketButton.setOnClickListener {
            buyTicket(user)
        }
    }

    private fun buyTicket(user: User) {
        try {
            val selectedTicketId = when (binding.ticketTypeGroup.checkedRadioButtonId) {
                R.id.ticket_type_pro -> 1
                R.id.ticket_type_elite -> 2
                R.id.ticket_type_ultimate -> 3
                else -> throw IllegalArgumentException("No ticket type selected.")
            }

            // Realizar la compra
            PurchaseService.buyTicket(user, id.toLong(), selectedTicketId)

            // Actualizar los datos del usuario con el nuevo saldo
            CurrentUser.setUser(this, user)

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Ticket comprado exitosamente.", Toast.LENGTH_LONG).show()

            // Finalizar la actividad
            finish()
        } catch (e: PurchaseService.InsufficientMoneyException) {
            Toast.makeText(this, "Saldo insuficiente: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("EventActivity", "Error al comprar ticket", e)
            Toast.makeText(this, "Error al realizar la compra: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }


    @SuppressLint("SetTextI18n")
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
                txtFinalPrice.text = "Precio: $${event.price.toString()}"
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
}
