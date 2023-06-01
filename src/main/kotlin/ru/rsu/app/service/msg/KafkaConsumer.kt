package ru.rsu.app.service.msg

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import ru.rsu.app.model.msg.NewTaskMsg
import ru.rsu.app.service.TgUserDataService
import ru.rsu.app.util.PropertyConfigUtils
import ru.rsu.app.util.TgKeyboardMarkups
import java.time.Duration
import java.util.*

class KafkaConsumer(
    private val propertyConfigUtils: PropertyConfigUtils,
    private val reportBot: Bot,
    private val tgUserDataService: TgUserDataService
) {

    fun initConsumers() {
        val kafkaProperty = propertyConfigUtils.getKafkaProperty()
        val config = Properties().apply {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperty.bootstrapServer)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java)
            put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperty.groupId)
        }

        val newTaskConsumer = KafkaConsumer<String, String>(config)
        newTaskConsumer.subscribe(listOf(propertyConfigUtils.geNewTaskTopic()))
        while (true) {
            val records: ConsumerRecords<String, String> = newTaskConsumer.poll(Duration.ofMillis(1000))
            for (record in records) {
                val value = record.value()
                val newTaskMsg = Json.decodeFromString(NewTaskMsg.serializer(), value)
                if (newTaskMsg.taskId > 0 && newTaskMsg.tgUserName.isNotBlank()) {
                    val userData = tgUserDataService.getUserDataByTgUserName(newTaskMsg.tgUserName)
                    if (Objects.nonNull(userData) && Objects.nonNull(userData.chatId)) {
                        val startMarkup = TgKeyboardMarkups.getStartMarkup()
                        val chatIdForUser = ChatId.fromId(userData.chatId)
                        reportBot.sendMessage(
                            chatId = chatIdForUser,
                            "Вам поступила новая задача. Идентификатор задачи ${newTaskMsg.taskId}",
                            replyMarkup = startMarkup
                        )
                    }
                }
            }
        }
        closeConsumers(listOf(newTaskConsumer))
    }

    private fun closeConsumers(consumers: List<KafkaConsumer<String, String>>) {
        consumers.forEach { consumer -> consumer.close() }
    }
}