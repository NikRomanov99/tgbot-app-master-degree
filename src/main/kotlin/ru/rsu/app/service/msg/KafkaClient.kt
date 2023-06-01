package ru.rsu.app.service.msg

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import ru.rsu.app.util.PropertyConfigUtils
import java.util.*

class KafkaClient(private val propertyConfigUtils: PropertyConfigUtils) {
    fun createKafkaProducer(): KafkaProducer<String, String> {
        val kafkaConfig = propertyConfigUtils.getKafkaProperty()
        val config = Properties().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.bootstrapServer)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        }

        return KafkaProducer(config)
    }
}