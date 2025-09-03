package ru.freestyle.websync.messages.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MQModel(
    @SerialName("operation") var operation: String,
    @SerialName("category_id") var type: String,
    @SerialName("online") var online: Boolean,
    @SerialName("username") var username: String,
    @SerialName("value") var value: JsonElement
)