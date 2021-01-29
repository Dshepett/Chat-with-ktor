plugins {
    kotlin("jvm") version "1.4.10"
}

group = "dshepett"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-netty:1.1.5")
    implementation("io.ktor:ktor-websockets:1.1.5")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}