package ru.freestyle.websync.utils

object Time {
    const val SECOND_TICK: Long = 20
    const val MINUTE_TICK: Long = SECOND_TICK * 60
    const val HOUR_TICK: Long = MINUTE_TICK * 60
    const val DAY_TICK: Long = HOUR_TICK * 24

    const val SECOND: Long = 1000
    const val MINUTE = 60 * SECOND
    const val HOUR = 60 * MINUTE
    const val DAY = 24 * HOUR
}