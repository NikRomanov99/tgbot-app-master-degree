package ru.rsu.app.config

import com.github.kotlintelegrambot.Bot
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import ru.rsu.app.bot.ReportBot
import ru.rsu.app.repository.ReportRepository
import ru.rsu.app.repository.TgUserDataRepository
import ru.rsu.app.service.ReportService
import ru.rsu.app.service.TaskInfoService
import ru.rsu.app.service.TgUserDataService
import ru.rsu.app.service.msg.KafkaClient
import ru.rsu.app.service.msg.KafkaConsumer
import ru.rsu.app.util.PropertyConfigUtils

val appModule = module {
    //Utils
    single<PropertyConfigUtils> { PropertyConfigUtils() }

    //Repository
    single<ReportRepository> { ReportRepository(get()) }
    single<TgUserDataRepository> { TgUserDataRepository(get()) }


    //Service
    single<ReportService> { ReportService(get(), get(), get()) }
    single<TaskInfoService> { TaskInfoService(get()) }
    single<TgUserDataService> { TgUserDataService(get()) }

    //TgBot
    single<ReportBot> { ReportBot(get(), get(), get(), get()) }
    single<Bot> {
        get<ReportBot>().createBot()
    }

    //Kafka
    single<KafkaClient> { KafkaClient(get()) }
    single<KafkaConsumer> { KafkaConsumer(get(), get(), get()) }
}

