package ru.freestyle.websync.messages.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MQValue {
    var username = ""

    @Serializable
    data class Privilege(@SerialName("privilege_name") val privilegeName: String) : MQValue()

    @Serializable
    data class Command(@SerialName("command") val command: String, @SerialName("amount") val amount: Int) : MQValue()
}