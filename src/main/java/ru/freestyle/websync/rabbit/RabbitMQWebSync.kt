package ru.freestyle.websync.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DeliverCallback
import org.bukkit.Bukkit
import ru.freestyle.websync.ApplicationPlatform.Companion.config
import ru.freestyle.websync.ApplicationPlatform.Companion.json
import ru.freestyle.websync.exstension.genericClass
import ru.freestyle.websync.messages.Message
import ru.freestyle.websync.messages.messages
import ru.freestyle.websync.messages.models.MQModel
import ru.freestyle.websync.messages.models.MQValue
import ru.freestyle.websync.messages.serializers
import java.io.Closeable
import java.nio.charset.StandardCharsets
import java.util.concurrent.atomic.AtomicBoolean

class RabbitMQWebSync(
    private val rc: RabbitConnection,
    private val queue: String,
    private val bukkit: Boolean,
    private val prefetch: Int = 200,
    private val queueArgs: Map<String, Any>? = null,
) : Closeable {

    @Volatile private var chConsume: Channel? = null
    @Volatile private var chPublish: Channel? = null
    @Volatile private var consumerTag: String? = null
    private val running = AtomicBoolean(false)

    fun start() {
        if (!running.compareAndSet(false, true)) return

        val conn = rc.getOrCreate()

        chConsume = conn.createChannel().apply {
            queueDeclare(queue, true,  false,  false, queueArgs)
            basicQos(prefetch)

            if (config.rabbitWebSync.active && bukkit) {
                consumerTag = basicConsume(queue, false, deliverCb(), cancelCb())
            }
            addShutdownListener { cause ->
                if (running.get()) println("RMQ consumer shutdown: ${cause?.message}")
            }
        }

        println("Connected to RabbitMQ; consuming=$bukkit queue=$queue")
    }

    private fun stop() {
        running.set(false)
        runCatching {
            val tag = consumerTag
            val ch = chConsume
            if (tag != null && ch?.isOpen == true) ch.basicCancel(tag)
        }
        runCatching { chConsume?.close() }
        runCatching { chPublish?.close() }
        chConsume = null
        chPublish = null
        consumerTag = null
    }

    override fun close() = stop()

    private fun deliverCb(): DeliverCallback = DeliverCallback { _, delivery ->
        val body = String(delivery.body, StandardCharsets.UTF_8)
        val tag = delivery.envelope.deliveryTag
        val ch = chConsume

        try {
            val mqModel: MQModel = json.decodeFromString(body)

            val player = Bukkit.getPlayer(mqModel.username)
            if (mqModel.online && (player == null || !player.isOnline)) {
                ch?.basicNack(tag, false, true)
                return@DeliverCallback
            }

            val serializer = serializers[mqModel.type]
            if (serializer == null) {
                ch?.basicNack(tag, false, true)
                return@DeliverCallback
            }

            val mqValue: MQValue = json.decodeFromJsonElement(serializer, mqModel.value).also {
                it.username = mqModel.username
            }

            val messageHandler = messages.find { it.genericClass<MQModel>() == mqValue::class.java }

            if (messageHandler != null) {
                @Suppress("UNCHECKED_CAST")
                val ok = (messageHandler as Message<MQValue>).isAcknowledged(mqModel.operation, mqValue)
                if (ok) ch?.basicAck(tag, false) else ch?.basicNack(tag, false, true)
            } else {
                ch?.basicNack(tag, false, true)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            ch?.basicAck(tag, false)
        }
    }

    private fun cancelCb(): CancelCallback = CancelCallback {
        println("Consumer canceled for queue=$queue")
    }
}
