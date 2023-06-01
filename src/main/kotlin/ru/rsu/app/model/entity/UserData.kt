package ru.rsu.app.model.entity

import org.bson.codecs.pojo.annotations.BsonId

data class UserData(
    @BsonId
    val tgUserName: String,
    val chatId: Long
)
