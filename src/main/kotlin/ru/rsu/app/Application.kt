package ru.rsu.app

import io.ktor.server.application.*
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import ru.rsu.app.plugins.configureKoin
import ru.rsu.app.plugins.configureRouting
import ru.rsu.app.plugins.configureSerialization
import ru.rsu.app.plugins.configureTgBot
import ru.rsu.app.service.msg.KafkaConsumer

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureKoin()
    configureTgBot()
    configureRouting()
    configureSerialization()

    val kafkaConsumer by inject<KafkaConsumer>()
    launch { kafkaConsumer.initConsumers() }
}
