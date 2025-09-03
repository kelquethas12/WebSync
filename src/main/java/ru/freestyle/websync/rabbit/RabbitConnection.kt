package ru.freestyle.websync.rabbit

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import ru.freestyle.websync.config.Config
import java.io.Closeable

class RabbitConnection(private val cfg: Config) : Closeable {
    @Volatile private var conn: Connection? = null
    val isOpen: Boolean get() = conn?.isOpen == true

    @Synchronized
    fun getOrCreate(): Connection {
        val current = conn
        if (current != null && current.isOpen) return current

        val f = ConnectionFactory().apply {
            host = cfg.host
            username = cfg.username
            password = cfg.password
            virtualHost = "/"
        }
        conn = f.newConnection("minion-conn")
        return conn!!
    }

    fun newChannel(): Channel = getOrCreate().createChannel()

    override fun close() { runCatching { conn?.close() } }
}
