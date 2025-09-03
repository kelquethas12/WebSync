package ru.freestyle.websync

import com.google.gson.GsonBuilder
import kotlinx.serialization.json.Json
import ru.freestyle.websync.config.Config
import ru.freestyle.websync.rabbit.RabbitMq

interface ApplicationPlatform {

    fun loadPlatform(bukkit: Boolean)
    fun loadConfig(bukkit: Boolean): Config

    companion object {
        lateinit var instance : ApplicationPlatform

        lateinit var config: Config
        lateinit var rabbitMQ: RabbitMq

        val json = Json { ignoreUnknownKeys = true }
        //Требуется что бы не наследоваться от жесткой типизации.
        val gson = GsonBuilder().setPrettyPrinting().create()



    }
}