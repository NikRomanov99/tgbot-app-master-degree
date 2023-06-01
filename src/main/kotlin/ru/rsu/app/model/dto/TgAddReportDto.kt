package ru.rsu.app.model.dto

import ru.rsu.app.model.AddReportSteps

data class TgAddReportDto(
    var currentStep: AddReportSteps,
    var taskId: Long? = null,
    var equipmentId: Long? = null,
    var comment: String? = null
)
