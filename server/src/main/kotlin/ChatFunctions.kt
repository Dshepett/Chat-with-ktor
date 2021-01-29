package com.jetbrains.handson.chat.server

import io.ktor.http.cio.websocket.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

public class ChatFunctions {
    //Function that change user's name
    suspend fun changeName(connection: Connection, data:String){
        connection.name=data
        var receivedText="Your name changed on ${connection.name}"
        //Send information about what happend
        connection.session.send("[info] $receivedText")
    }

    //Function that mutes user
    suspend fun mute(connection: Connection, name:String,connections:MutableSet<Connection>){
        var isFound = false
        //Searching for user
        for(con in connections){
            if(con.name==name){
                connection.session.send("[info] We found him")
                con.session.send("[info] You have been muted")
                //Mutes him
                isFound=true
                con.isBanned=true
                break
            }
        }
        //Check if this user is real
        if(isFound){
            connections.forEach{
                it.session.send("[info] User: ${connection.name} muted user: $name")
            }
        }
        else
            connection.session.send("[info] We didn't found him")
    }

    //Function that unmutes user
    suspend fun unmute(connection: Connection, name:String,connections:MutableSet<Connection>){
        var shouldSend=false
        var isFound = false
        //Searching for user
        for(con in connections){
            if(con.name==name){
                if(con.isBanned){
                    connection.session.send("[info] We found him")
                    con.session.send("[info] You have been unmuted")
                    isFound=true
                    con.isBanned=false
                    shouldSend=true
                }
                //Check if this user is real
                else{
                    if(isFound)
                        connection.session.send("[info] We found him, but he is unmuted")
                    else
                        connection.session.send("[info] We didn't found him")
                }
                break
            }
        }
        if(shouldSend){
            connections.forEach{
                it.session.send("[info] User: ${connection.name} unmuted user: $name")
            }
        }
    }

    //Function that send you information about your sent messages and number of written letters
    suspend fun stats(connection: Connection){
        var stats="[info] User: ${connection.name }\nsent messages: ${connection.sentMessages}" +
                "\nnumber of written letters: ${connection.numOfLetters}"
        connection.session.send("$stats")
    }

    //Sends secret message for user
    suspend fun secretFor(connection: Connection, name:String, textMessage: String, connections: MutableSet<Connection>){
        var isFound = false
        //Searching for this user
        for(a in connections){
            if(a.name==name){
                val text="[SECRET ${connection.name}]$textMessage"
                a.session.send(text)
                isFound=true
                break
            }
        }
        //Check if we found this user
        if(isFound)
            connection.session.send("[info] Your message is sent to $name")
        else
            connection.session.send("[info] We didn't found this user")
    }

    //Sets new deadline
    suspend fun setDeadLine(data: String,deadlines:MutableSet<DeadLine>,connections: MutableSet<Connection>,connection: Connection){
        try{
            val time = data.substring(14,data.indexOf(']')).split(' ')[0].split(':')
            val date = data.substring(14,data.indexOf(']')).split(' ')[1].split('.')
            val deadLineDate = LocalDate.of(date[2].toInt(),date[1].toInt(),date[0].toInt())
            val deadLineTime = LocalTime.of(time[0].toInt(),time[1].toInt())
            var deadLine=DeadLine(deadLineDate,deadLineTime ,data.substring((data.indexOf(']')+1)))
            deadlines+=deadLine
            connections.forEach {
                it.session.send("[info] NEW DEADLINE: $deadLine")
            }
        }
        catch (e:Exception){
            connection.session.send("[info] Error happened")
        }
    }

    //Allow user to leave the chat
    suspend  fun leave(connection: Connection,connections: MutableSet<Connection>){
        connections -= connection
        //Notify other users about it
        connections.forEach {
            it.session.send("[info] User ${connection.name} left the chat")
        }
    }

    //Shows information about deadlines
    suspend fun deadlines(connection: Connection,deadlines: MutableSet<DeadLine>){
        if(deadlines.size!=0){
            deadlines.forEach{
                connection.session.send(it.toString())
            }
        }
        else{
            connection.session.send("[info] No deadlines")
        }

    }

    //Show all the functions user can use
    suspend fun help(connection: Connection){
        connection.session.send("/change new_name - change your name to a new name")
        connection.session.send("/mute [user_name] - mutes user")
        connection.session.send("/unmute [user_name] - unmutes user")
        connection.session.send("/stats - gives you information about sent messages and written letters")
        connection.session.send("/secretfor [user_name] message - send message only for user user_name")
        connection.session.send("/setDeadLine [hh:mm dd.mm.yyyy] task_info - create a new deadline")
        connection.session.send("/deadlines - see all deadlines")
        connection.session.send("/leave - disconnect you from chat")
    }

    //Check if it is needed to notify users about deadlines
    suspend fun checkDeadLines(deadlines: MutableSet<DeadLine>, connections: MutableSet<Connection>){

        for( i in deadlines){
            //Search for finished deadlines
            if((i.deadlineDate<LocalDate.now())||((i.deadlineTime<= LocalTime.now()))&&(i.deadlineDate<=LocalDate.now())){
                connections.forEach{
                    it.session.send("[info] FINISHED: $i")
                }
                //Delete them after notifying
                deadlines-=i
                continue
            }
            //Search for deadlines that will finish less than in one day
            if(ChronoUnit.DAYS.between(i.deadlineDate,LocalDate.now())<=1){
                if(!i.isShown){
                    connections.forEach{
                        it.session.send("[info] IT WILL FINISH LESS THAN IN ONE DAY: $i")
                    }
                    i.isShown=true
                }
            }
        }
    }
}