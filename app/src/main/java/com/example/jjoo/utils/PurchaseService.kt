package com.example.jjoo.utils

import com.example.jjoo.data.Purchase
import com.example.jjoo.data.User
import com.example.jjoo.data.tickets.TicketElite
import com.example.jjoo.data.tickets.TicketPro
import com.example.jjoo.data.tickets.TicketUltimateEvent
import com.example.jjoo.data.tickets.Trade
import com.example.jjoo.repositories.EventRepository
import com.example.jjoo.repositories.PurchaseRepository
import java.time.LocalTime

object PurchaseService {
    fun buyTicket(user: User, eventId: Long, ticketType: Int) {
        val event = EventRepository.getById(eventId) ?: throw NoSuchElementException("Event not found")

    // Selección del tipo de ticket y creación del objeto Trade correspondiente
        val trade: Trade = when (ticketType) {
            1 -> TicketPro()
            2 -> TicketElite()
            3 -> TicketUltimateEvent()
            else -> throw IllegalArgumentException("Invalid ticket type")
        }

    // Calcular el costo del ticket
        val cost = trade.tradeTicket(event)

    // Verificar si el usuario tiene suficiente saldo
        if (user.money < cost) throw InsufficientMoneyException("Insufficient balance for this purchase.")

    // Actualizar el saldo del usuario después de la compra
        user.money -= cost
        println("Purchase successful. Remaining balance: $${user.money}")

    // Generar un asiento único para la compra
        val seat = generateUniqueSeat(event.id)

    // Crear una nueva compra con los detalles correspondientes
        val purchase = Purchase(
            id = (PurchaseRepository.get().size + 1).toLong(),
            userId = user.id,
            eventId = event.id,
            amount = cost,
            createdDate = LocalTime.now().toString(),
            seat = seat ?: "N/A",
            hour = LocalTime.now()
        )

    // Agregar la compra al repositorio de compras
        PurchaseRepository.add(purchase)
    }

    private fun generateUniqueSeat(eventId: Long): String {
        val existingSeats = PurchaseRepository.get()
            .filter { it.eventId == eventId }
            .map { it.seat }
            .toSet()

        val random = java.util.Random()
        while (true) {
            val seat = "${random.nextInt(10)}${random.nextInt(10)}${('A'..'Z').random()}"
            if (seat !in existingSeats) {
                return seat
            }
        }
    }

    fun listPurchasesByUserId(user: User) {
        val purchases = PurchaseRepository.get().filter { it.userId == user.id }

        /* if (purchases.isNotEmpty()) {
            println("Purchases for user: ${user.nickName}\n")
            purchases.forEach { purchase ->
                println("""
                |----------------------------------------
                | Purchase ID   : ${purchase.id}
                | Event ID      : ${purchase.eventId}
                | Amount        : $${purchase.amount}
                | Seat Number   : ${purchase.seat}
                | Purchase Date : ${purchase.createdDate}
                | Purchase Time : ${purchase.hour}
                |----------------------------------------
            """.trimMargin())
            }
        } else {
            println("No purchases found for user: ${user.nickName}")
        }*/
    }

    class InsufficientMoneyException(message: String) : Exception(message)  // Excepción personalizada privada para el servicio de compras
}