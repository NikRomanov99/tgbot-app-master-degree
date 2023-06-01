package ru.rsu.app.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    val mongoId: String? = null,
    var reportDate: String? = null,
    var tgUserName: String? = null,
    var taskId: Long? = null,
    var comment: String? = null
)
