package ru.rsu.app.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject
import ru.rsu.app.model.dto.ReportDto
import ru.rsu.app.model.dto.StandardResponseDto
import ru.rsu.app.service.ReportService

fun Route.reportRouting() {
    val reportService by inject<ReportService>()

    route("/report") {
        get("") {
            val result = reportService.getAll()
            if (result.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, StandardResponseDto(false, HttpStatusCode.NotFound.value))
            }
        }
        get("/{tgUserName?}") {
            val tgUserName = call.parameters.getOrFail("tgUserName")
            val result = reportService.getAllByTgUserName(tgUserName)
            if (result.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, StandardResponseDto(false, HttpStatusCode.NotFound.value))
            }
        }
        post {
            val report = call.receive<ReportDto>()
            reportService.addReport(report)
            call.respond(HttpStatusCode.OK, StandardResponseDto(true, HttpStatusCode.OK.value))
        }
    }
}