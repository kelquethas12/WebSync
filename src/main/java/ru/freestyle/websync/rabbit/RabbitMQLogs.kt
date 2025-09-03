package ru.freestyle.websync.rabbit

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import kotlinx.serialization.encodeToString
import ru.freestyle.websync.ApplicationPlatform
import ru.freestyle.websync.utils.Scheduler.Companion.sc

class RabbitMQLogs(
    private val connection: Connection,
    private val queue: String = "logs"
) : AutoCloseable {

    private val channel: Channel = connection.createChannel().apply {
        queueDeclare(
            queue,
            true,
            false,
            false,
            mapOf("x-message-ttl" to 172800000)
        )
    }

    fun send(obj: Any, ttlMs: Long? = null) {
        sc.taskAsync(1) {
            val body = ApplicationPlatform.gson.toJson(obj)

            val props = AMQP.BasicProperties.Builder()
                .contentType("application/json")
                .deliveryMode(2)
                .apply { if (ttlMs != null) expiration(ttlMs.toString()) }
                .build()

            channel.basicPublish("", queue, props, body.toByteArray())
        }
    }

    override fun close() {
        channel.close()
        connection.close()
    }
}
