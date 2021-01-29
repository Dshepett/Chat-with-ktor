package com.jetbrains.handson.chat.server

import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class DeadLine(val deadlineDate:LocalDate, val deadlineTime: LocalTime, val task:String) {
    var isShown = false
    override fun toString(): String {
        return "$task     Until: $deadlineTime $deadlineDate"
    }
}