package ru.freestyle.websync.config

data class RabbitMQSync(
    val active: Boolean = true,
    val queue: String = "",
    val privilegePermissionEx: Boolean = true
)

class Config(
    val username: String = "",
    val password: String = "",
    val host: String = "localhost",

    val rabbitWebSync: RabbitMQSync = RabbitMQSync(
        active = true,
        queue = "clanwar",
        privilegePermissionEx = false
    ),
    val rabbitLogsSync: RabbitMQSync = RabbitMQSync(
        active = false,
        queue = "logs",
        privilegePermissionEx = false
    ),
)