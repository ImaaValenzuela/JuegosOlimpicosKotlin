package com.example.jjoo.data.tickets

import com.example.jjoo.data.DayOfWeekEnum
import com.example.jjoo.data.Event

class TicketUltimateEvent : Trade() {

    override fun tradeTicket(event: Event): Double {
        val commissionRate = if (event.day == DayOfWeekEnum.SUNDAY || event.day == DayOfWeekEnum.SATURDAY) {
            CommissionRates.WEEKEND_COMMISSION
        } else {
            CommissionRates.WEEKDAY_COMMISSION
        }
        val cost = event.price.times(1.plus(commissionRate)) //* (1 + commissionRate)
        println("The final price for Ultimate Event is: $cost")
        return cost
    }
}