package ru.rsu.app.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ru.rsu.app.model.dto.TaskInfoResponseDto
import ru.rsu.app.util.PropertyConfigUtils

class TaskInfoService(private val propertyConfigUtils: PropertyConfigUtils) {

    private val logger = LoggerFactory.getLogger(javaClass)
    fun getTasksByTgUsername(tgUserName: String): List<TaskInfoResponseDto> {
        val client = HttpClient(CIO)
        var result: List<TaskInfoResponseDto> = arrayListOf()
        runBlocking {
            try {
                val response = client.get("${propertyConfigUtils.getTaskServiceUrl()}/taskInfo/tgUserName/$tgUserName")
                if (HttpStatusCode.OK == response.status) {
                    val taskData = response.call.response.body<String>()
                    result = Json.decodeFromString(ListSerializer(TaskInfoResponseDto.serializer()), taskData)
                } else {
                    logger.error("Error from task-service: ${response.responseTime} ${response.status}")
                }
            } finally {
                client.close()
            }
        }
        return result
    }
}