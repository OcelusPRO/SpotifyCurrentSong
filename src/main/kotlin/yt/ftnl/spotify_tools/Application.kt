package yt.ftnl.spotify_tools

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import yt.ftnl.spotify_tools.plugins.*
import java.io.File

val CONFIG = Configuration.loadConfiguration(File("./config.json"))
fun main() {
    embeddedServer(Netty, port = CONFIG.port, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureMonitoring()
    configureTemplating()
    configureSerialization()
    configureRouting()
}
