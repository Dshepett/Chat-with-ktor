# Chat-with-ktor
Simple client and not bad server for chat. You can mute user, set deadlines, send secret messages, get stats aboit your  actions in this chat and change your name.
# Contets
Includes client and server written using kotlin and ktor library.
# How to launch
1. Download code
2. Open it in IntelliJ IDEA
3. Open settings.gradle.kts
4. Press link with project
5. Press Ok
6. Wait for Gradle to download all dependencies for the project
7. Run server : server/src/main/kotlin/Application.kt
8. To run chat : client/src/main/kotlin/com/jetbrains/handson/chat/client/ChatClient.kt
# How to use
You have to launch the server and client and it will work.
Also you can use https://www.websocket.org/echo.html as a client.
In location write ws://localhost:8080/chat to use it with this server
