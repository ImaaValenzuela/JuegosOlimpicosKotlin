package com.example.jjoo.data.tickets

import com.example.jjoo.data.Event

abstract class Trade {

    abstract fun tradeTicket(event: Event): Double // Este se implementará en la subclase. Herencia

}