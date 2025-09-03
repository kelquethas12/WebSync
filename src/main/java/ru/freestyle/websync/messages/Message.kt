package ru.freestyle.websync.messages

import kotlinx.serialization.KSerializer
import ru.freestyle.websync.messages.models.MQValue

interface Message<T : MQValue> {
    fun isAcknowledged(operation: String, message: T): Boolean
}

    val serializers: Map<String, KSerializer<out MQValue>> = mapOf(
        "privilege" to MQValue.Privilege.serializer(),
        "command" to MQValue.Command.serializer(),
    )
    val messages = mutableListOf(
        MessageCommand(),
        MessagePrivilege()
    )