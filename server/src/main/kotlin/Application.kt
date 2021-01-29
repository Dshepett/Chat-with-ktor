package com.jetbrains.handson.chat.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import java.util.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
@Suppress("unused")
fun Application.module() {
    var cf=ChatFunctions()
    install(WebSockets)
    routing {
        //Collection with all connections
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        //Collection with all deadlines
        val deadlines = Collections.synchronizedSet<DeadLine?>(LinkedHashSet())
        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here." +
                        "\n send /help to find information about command of this chat")
                connections.forEach {
                    if(it!=thisConnection)
                        it.session.send("[info] New user connected : ${thisConnection.name}")
                }
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    //Check deadlines
                    cf.checkDeadLines(deadlines, connections)
                    var receivedText = frame.readText()
                    //Check for calling of functions
                    if(receivedText.indexOf("/change")==0){
                        cf.changeName(thisConnection,frame.readText().substring(8))
                        continue
                    }
                    if(receivedText.indexOf("/mute")==0){
                        cf.mute(thisConnection,
                            receivedText.substring(receivedText.indexOf('[')+1,receivedText.indexOf(']')+1),
                            connections)
                        continue
                    }
                    if(receivedText.indexOf("/unmute")==0){
                        cf.unmute(thisConnection,
                            receivedText.substring(receivedText.indexOf('[')+1,receivedText.indexOf(']')+1),
                            connections)
                        continue
                    }
                    if(receivedText=="/stats"){
                        cf.stats(thisConnection)
                        continue
                    }
                    if(receivedText.indexOf("/secretfor")==0){
                        cf.secretFor(thisConnection,
                            receivedText.substring(receivedText.indexOf('[')+1,receivedText.indexOf(']')),
                            receivedText.substring(receivedText.indexOf(']')+1),connections)
                        continue
                    }
                    if(receivedText.indexOf("/setDeadLine")==0) {
                        cf.setDeadLine(receivedText,deadlines,connections,thisConnection)
                        continue
                    }
                    if(receivedText.indexOf("/deadlines")==0) {
                        cf.deadlines(thisConnection,deadlines)
                        continue
                    }
                    if(receivedText=="/leave"){
                        cf.leave(thisConnection, connections)
                        break
                    }
                    if(receivedText=="/help"){
                        cf.help(thisConnection)
                        continue
                    }
                    //Get information about messages
                    thisConnection.numOfLetters+=receivedText.length
                    thisConnection.sentMessages++
                    //Check if user is muted
                    if(!thisConnection.isBanned){
                        val textWithUsername = "[${thisConnection.name}]: $receivedText"
                        connections.forEach {
                            if(it!=thisConnection)
                                it.session.send(textWithUsername)
                            else
                                it.session.send("[info] Message sent")
                        }
                    }

                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }
    }
}

