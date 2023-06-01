package ru.rsu.app.model.msg

import kotlinx.serialization.Serializable

@Serializable
data class NewTaskMsg(val taskId: Long, val tgUserName: String)