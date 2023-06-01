package ru.rsu.app.util

import com.typesafe.config.ConfigFactory
import ru.rsu.app.model.utils.DbProperty
import ru.rsu.app.model.utils.KafkaProperty

class PropertyConfigUtils {
    private val config = ConfigFactory.load()
    fun getDbProperty(): DbProperty {
        return DbProperty(
            config.getString("ktor.mongo.url"),
            config.getString("ktor.mongo.username"),
            config.getString("ktor.mongo.password"),
            config.getString("ktor.mongo.reportsDb"),
            config.getString("ktor.mongo.userdataDb")
        )
    }

    fun getKafkaProperty(): KafkaProperty {
        return KafkaProperty(
            config.getString("ktor.kafka.bootstrap-servers"),
            config.getString("ktor.kafka.key-serializer"),
            config.getString("ktor.kafka.value-serializer"),
            config.getString("ktor.kafka.key-deserializer"),
            config.getString("ktor.kafka.value-deserializer"),
            config.getString("ktor.kafka.group-id"),
            config.getStringList("ktor.kafka.topics")
        )
    }

    fun getUpdateTaskStatusTopic(): String {
        return config.getStringList("ktor.kafka.topics").first()
    }

    fun geNewTaskTopic(): String {
        return config.getStringList("ktor.kafka.topics")[1]
    }

    fun getTaskServiceUrl() : String {
        return config.getString("ktor.og_equipment.taskServiceUrl")
    }


    fun getTgBotToken(): String {
        return config.getString("ktor.telegram.token")
    }

    fun getTgBotTimeOut(): Int {
        return config.getString("ktor.telegram.time_out").toInt()
    }
}