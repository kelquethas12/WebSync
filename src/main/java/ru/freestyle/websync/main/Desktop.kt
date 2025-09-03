package ru.freestyle.websync.main

import ru.freestyle.websync.ApplicationPlatform
import ru.freestyle.websync.ApplicationPlatform.Companion.config
import ru.freestyle.websync.ApplicationPlatform.Companion.gson
import ru.freestyle.websync.ApplicationPlatform.Companion.instance
import ru.freestyle.websync.ApplicationPlatform.Companion.rabbitMQ
import ru.freestyle.websync.config.Config
import ru.freestyle.websync.rabbit.RabbitMq
import java.io.File

class Desktop : ApplicationPlatform {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Desktop().loadPlatform(false)
        }
    }

    override fun loadPlatform(bukkit: Boolean) {
        instance = this
        config = loadConfig(false)
        rabbitMQ = RabbitMq()
    }

    override fun loadConfig(bukkit: Boolean): Config {
        val configFile = File("config.json")
        if(!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(gson.toJson(Config()))
        }
        config = gson.fromJson(configFile.readText(), Config::class.java)
        return config
    }
}