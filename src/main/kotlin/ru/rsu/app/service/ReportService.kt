package ru.rsu.app.service

import kotlinx.serialization.json.Json.Default.encodeToString
import org.apache.kafka.clients.producer.ProducerRecord
import ru.rsu.app.model.dto.ReportDto
import ru.rsu.app.model.entity.Reports
import ru.rsu.app.model.msg.TaskChangeStatusInfo
import ru.rsu.app.repository.ReportRepository
import ru.rsu.app.service.msg.KafkaClient
import ru.rsu.app.util.PropertyConfigUtils

class ReportService(
    private val repository: ReportRepository,
    private val kafkaClient: KafkaClient,
    private val propertyConfigUtils: PropertyConfigUtils
) {

    fun getAll(): List<ReportDto> {
        val reportInDb = repository.selectAllReport()
        if (!reportInDb.isEmpty()) {
            return reportInDb.map { entity -> mapEntityToDto(entity) }
        }
        return emptyList()
    }

    fun getAllByTgUserName(tgUserName: String): List<ReportDto> {
        val reportInDb = repository.selectAllReportByUserTgName(tgUserName)
        if (!reportInDb.isEmpty()) {
            return reportInDb.map { entity -> mapEntityToDto(entity) }
        }
        return emptyList()
    }

    fun addReport(report: ReportDto) {
        val entity = mapDtoToEntity(report)
        repository.insertReport(entity)

        if (report.tgUserName != null) {
            val producer = kafkaClient.createKafkaProducer()
            val topicName = propertyConfigUtils.getUpdateTaskStatusTopic()
            val msg = buildMsgForInspection(report)
            producer.send(ProducerRecord(topicName, msg))
        }
    }

    private fun buildMsgForInspection(report: ReportDto): String {
        val lastInspectionMsg = TaskChangeStatusInfo(report.taskId!!, report.tgUserName!!, report.reportDate!!)
        return encodeToString(TaskChangeStatusInfo.serializer(), lastInspectionMsg)
    }

    private fun mapEntityToDto(entity: Reports): ReportDto {
        return ReportDto(
            mongoId = entity.id.toString(),
            reportDate = entity.reportDate,
            tgUserName = entity.tgUserName,
            taskId = entity.taskId,
            comment = entity.comment
        );
    }

    private fun mapDtoToEntity(dto: ReportDto): Reports {
        return Reports(
            reportDate = dto.reportDate,
            tgUserName = dto.tgUserName,
            taskId = dto.taskId,
            comment = dto.comment
        )
    }
}