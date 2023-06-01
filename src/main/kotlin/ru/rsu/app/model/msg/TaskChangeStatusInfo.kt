package ru.rsu.app.model.msg

import kotlinx.serialization.Serializable

@Serializable
data class TaskChangeStatusInfo(
    val taskId: Long,
    val tgUserName: String,
    val lastInspectionDate: String
)
