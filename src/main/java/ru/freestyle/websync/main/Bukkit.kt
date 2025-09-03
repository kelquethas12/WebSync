package ru.freestyle.websync.main

import org.bukkit.plugin.java.JavaPlugin
import ru.freestyle.websync.ApplicationPlatform
import ru.freestyle.websync.ApplicationPlatform.Companion.gson
import ru.freestyle.websync.ApplicationPlatform.Companion.instance
import ru.freestyle.websync.ApplicationPlatform.Companion.rabbitMQ
import ru.freestyle.websync.config.Config
import ru.freestyle.websync.rabbit.RabbitMq
import java.io.File

class Bukkit: JavaPlugin(), ApplicationPlatform {

    override fun onEnable() {
        loadPlatform(true)
    }

    override fun loadPlatform(bukkit: Boolean) {
        instance = this
        ApplicationPlatform.config = loadConfig(bukkit)
        rabbitMQ = RabbitMq()
    }

    override fun loadConfig(bukkit: Boolean): Config {
        if(!this.dataFolder.exists()) {
            this.dataFolder.mkdirs()
        }
        val configFile = File(dataFolder, "config.json")
        if(!configFile.exists()) {
            configFile.createNewFile()
            configFile.writeText(gson.toJson(Config()))
        }
        ApplicationPlatform.config = gson.fromJson(configFile.readText(), Config::class.java)
        return ApplicationPlatform.config
    }
}