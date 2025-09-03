package ru.freestyle.websync

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection

object Connection {
    var connection: Connection? = null
    var channel: Channel? = null
}