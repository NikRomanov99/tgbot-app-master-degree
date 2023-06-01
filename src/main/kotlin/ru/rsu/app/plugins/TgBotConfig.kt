package ru.rsu.app.plugins

import com.github.kotlintelegrambot.Bot
import io.ktor.server.application.*
import org.koin.ktor.ext.inject

fun Application.configureTgBot() {
    val bot by inject<Bot>()
    bot.startPolling()
}