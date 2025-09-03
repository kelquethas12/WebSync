package ru.freestyle.websync.messages

import ru.freestyle.websync.messages.models.MQValue

class MessageCommand: Message<MQValue.Command> {
    override fun isAcknowledged(operation: String, message: MQValue.Command): Boolean {
        println(message.command.replace("<user>", message.username).replace("<amount>", message.amount.toString()))
        return true
    }

}