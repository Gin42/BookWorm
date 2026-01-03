package com.example.bookworm.utils

import java.time.Instant
import java.time.ZoneId

object TimeUtils {

    /** UTC millis */
    fun now(): Long = System.currentTimeMillis()

    /** UTC millis at start of day for a given millis */
    fun startOfDay(millis: Long): Long {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }
}
