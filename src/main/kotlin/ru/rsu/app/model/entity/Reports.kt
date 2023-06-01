package ru.rsu.app.model.entity

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Reports(
    @BsonId
    val id: Id<Reports>? = null,
    val reportDate: String?,
    val tgUserName: String?,
    val taskId: Long?,
    val comment: String? = null
)
