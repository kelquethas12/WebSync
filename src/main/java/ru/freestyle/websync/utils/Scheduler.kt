package ru.freestyle.websync.utils

import org.bukkit.scheduler.BukkitRunnable
import ru.freestyle.websync.ApplicationPlatform.Companion.instance
import ru.freestyle.websync.main.Bukkit

interface Scheduler {
    fun taskAsync(time: Long, runnable: () -> Unit)
    fun timerAsync(time: Long, runnable: () -> Unit)
    fun taskSyn(time: Long, runnable: () -> Unit)
    fun timerSyn(time: Long, runnable: () -> Unit)

    companion object {
        val sc: Scheduler = if (instance is Bukkit) {
            BukkitScheduler(instance as Bukkit)
        } else {
            DesktopScheduler()
        }
    }
}

class BukkitScheduler(private val plugin: Bukkit) : Scheduler {
    override fun taskAsync(time: Long, runnable: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                runnable()
            }
        }.runTaskLaterAsynchronously(plugin, time)
    }

    override fun timerAsync(time: Long, runnable: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                runnable()
            }
        }.runTaskTimerAsynchronously(plugin, 0, time)
    }

    override fun taskSyn(time: Long, runnable: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                runnable()
            }
        }.runTaskLater(plugin, time)
    }

    override fun timerSyn(time: Long, runnable: () -> Unit) {
        object : BukkitRunnable() {
            override fun run() {
                runnable()
            }
        }.runTaskTimer(plugin, 0, time)
    }
}

class DesktopScheduler : Scheduler {
    private val executor = java.util.concurrent.Executors.newScheduledThreadPool(4)

    override fun taskAsync(time: Long, runnable: () -> Unit) {
        executor.schedule({ runnable() }, time, java.util.concurrent.TimeUnit.MILLISECONDS)
    }

    override fun timerAsync(time: Long, runnable: () -> Unit) {
        executor.scheduleAtFixedRate({ runnable() }, 0, time, java.util.concurrent.TimeUnit.MILLISECONDS)
    }

    override fun taskSyn(time: Long, runnable: () -> Unit) {
        taskAsync(time, runnable)
    }

    override fun timerSyn(time: Long, runnable: () -> Unit) {
        timerAsync(time, runnable)
    }
}