package ru.freestyle.websync.messages

import org.bukkit.Bukkit
import ru.freestyle.websync.ApplicationPlatform.Companion.config
import ru.freestyle.websync.messages.models.MQValue

class MessagePrivilege : Message<MQValue.Privilege> {
    override fun isAcknowledged(operation: String, message: MQValue.Privilege): Boolean {
        if(config.rabbitWebSync.privilegePermissionEx) {
            if(operation == "add") {
                val permission = "pex user ${message.username} group add ${message.privilegeName}"
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), permission)
            } else if (operation == "delete") {
                val permission = "pex user ${message.username} group remove ${message.privilegeName}"
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), permission)
            }
        } else {
            if(operation == "add") {
                val permission = "lp user ${message.username} parent add ${message.privilegeName}"
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), permission)
            } else if (operation == "delete") {
                val permission = "lp user ${message.username} parent remove ${message.privilegeName}"
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), permission)
            }
        }
        return true
    }
}