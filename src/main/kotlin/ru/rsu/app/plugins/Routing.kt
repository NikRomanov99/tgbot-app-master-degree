package ru.rsu.app.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.rsu.app.routes.reportRouting

fun Application.configureRouting() {
    routing {
        reportRouting()
    }
}
