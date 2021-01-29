package com.jetbrains.handson.chat.server

import io.ktor.http.cio.websocket.*
import java.util.concurrent.atomic.*

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        var lastId = AtomicInteger(0)
    }
    var name = "user${lastId.getAndIncrement()}"
    var sentMessages=0
    var numOfLetters=0
    var isBanned=false
}