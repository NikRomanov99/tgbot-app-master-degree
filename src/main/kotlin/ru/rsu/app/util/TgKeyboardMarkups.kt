package ru.rsu.app.util

import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton

class TgKeyboardMarkups {
    companion object {
        fun getStartMarkup(): InlineKeyboardMarkup {
            return InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Посмотреть список активных задач",
                        callbackData = "getMyTasks"
                    )
                )
            )
        }

        fun getAddReportMarkup(): InlineKeyboardMarkup {
            return InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Добавить отчёт о выполнении задачи",
                        callbackData = "addReport"
                    )
                )
            )
        }

        fun getShowReportMarkup(): InlineKeyboardMarkup {
            return InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        text = "Показать отчёт",
                        callbackData = "showReport"
                    ),
                    InlineKeyboardButton.CallbackData(
                        text = "Посмотреть список активных задач",
                        callbackData = "getMyTasks"
                    )
                )
            )
        }
    }
}