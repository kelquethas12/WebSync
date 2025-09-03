package ru.freestyle.websync.rabbit

import ru.freestyle.websync.ApplicationPlatform.Companion.config
import ru.freestyle.websync.models.EveryLogs
import ru.freestyle.websync.models.EveryObjects
import ru.freestyle.websync.utils.Scheduler.Companion.sc

class RabbitMq {
    init {
        sc.taskAsync(1) {
            val rc = RabbitConnection(config)

            RabbitMQWebSync(
                rc = rc,
                queue = config.rabbitWebSync.queue,
                bukkit = true,
                prefetch = 200,
                queueArgs = null
            ).start()
            logs = RabbitMQLogs(
                connection = rc.getOrCreate(),
                config.rabbitLogsSync.queue
            )
        }
    }
    companion object {
        var logs: RabbitMQLogs? = null
    }
}