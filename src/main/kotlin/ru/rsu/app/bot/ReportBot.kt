package ru.rsu.app.bot

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.callbackQuery
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.extensions.filters.Filter
import com.github.kotlintelegrambot.logging.LogLevel
import com.mongodb.MongoWriteException
import org.slf4j.LoggerFactory
import ru.rsu.app.model.AddReportSteps
import ru.rsu.app.model.dto.ReportDto
import ru.rsu.app.model.dto.TgAddReportDto
import ru.rsu.app.model.entity.UserData
import ru.rsu.app.service.ReportService
import ru.rsu.app.util.PropertyConfigUtils
import ru.rsu.app.service.TaskInfoService
import ru.rsu.app.service.TgUserDataService
import ru.rsu.app.util.TgKeyboardMarkups
import ru.rsu.app.util.TgMessageBuilder
import java.time.LocalDateTime


class ReportBot(
    private val propertyConfigUtils: PropertyConfigUtils,
    private val taskInfoService: TaskInfoService,
    private val reportService: ReportService,
    private val tgUserDataService: TgUserDataService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var _chatId: ChatId.Id? = null
    private val chatId by lazy { requireNotNull(_chatId) }

    val userStates = mutableMapOf<Long, TgAddReportDto>()
    val reportUserMap = mutableMapOf<String, ReportDto>()

    fun createBot(): Bot {
        return bot {
            token = propertyConfigUtils.getTgBotToken()
            timeout = propertyConfigUtils.getTgBotTimeOut()
            logLevel = LogLevel.Network.Body

            dispatch {
                setUpCommands()
                setUpCallbacks()
            }
        }
    }


    private fun Dispatcher.setUpCallbacks() {
        callbackQuery(callbackData = "getMyTasks") {
            val addReportMarkup = TgKeyboardMarkups.getAddReportMarkup()
            val tgUserName = callbackQuery.from.username
            val listOfTasks = taskInfoService.getTasksByTgUsername(tgUserName!!)
            val msg = TgMessageBuilder.buildMsgForTasks(listOfTasks)
            bot.sendMessage(
                chatId = chatId,
                text = msg,
                replyMarkup = addReportMarkup
            )
        }

        callbackQuery(callbackData = "addReport") {
            val userId = callbackQuery.from.id
            userStates[userId] = TgAddReportDto(AddReportSteps.AWAITING_TASK_ID)
            bot.sendMessage(chatId = chatId, text = "Введите идентификатор задачи")
        }

        message(Filter.Text) {
            val userId = message.from?.id ?: return@message
            val userState = userStates[userId] ?: return@message

            when (userState.currentStep) {
                AddReportSteps.AWAITING_TASK_ID -> {
                    userState.taskId = message.text!!.toLong()
                    userState.currentStep = AddReportSteps.AWAITING_COMMENT
                    bot.sendMessage(chatId = chatId, text = "Введите комментарий о выполнении задачи")
                }

                AddReportSteps.AWAITING_COMMENT -> {
                    val showReportMarkup = TgKeyboardMarkups.getShowReportMarkup()
                    userState.comment = message.text.toString()
                    val tgUserName = message.from?.username!!
                    val report = createReport(tgAddReport = userState, tgUserName = tgUserName)
                    reportService.addReport(report)
                    reportUserMap[tgUserName] = report
                    bot.sendMessage(
                        chatId = chatId,
                        text = "Отчёт готов и сохранён",
                        replyMarkup = showReportMarkup
                    )
                    userState.currentStep = AddReportSteps.NONE
                }

                AddReportSteps.NONE -> {}
            }
        }

        callbackQuery(callbackData = "showReport") {
            val startMarkup = TgKeyboardMarkups.getStartMarkup()
            val tgUserName = callbackQuery.from.username!!
            val report = reportUserMap[tgUserName]
            if (report != null) {
                val msg = TgMessageBuilder.buildMsgForReport(report = report)
                bot.sendMessage(
                    chatId = chatId,
                    text = msg,
                    replyMarkup = startMarkup
                )
            }
        }
    }

    private fun Dispatcher.setUpCommands() {
        command("start") {
            val startMarkup = TgKeyboardMarkups.getStartMarkup()
            _chatId = ChatId.fromId(message.chat.id)
            try {
                tgUserDataService.addUserData(UserData(message.from!!.username!!, _chatId!!.id))
            } catch (e: MongoWriteException) {
                logger.warn("MongoWriteException. User  try do auth more than on time ")
            }
            bot.sendMessage(
                chatId = chatId,
                text = "Привет! Я бот для получения твоих рабочих задач и создания простых отчётов к ним",
                replyMarkup = startMarkup
            )
        }
    }

    private fun createReport(tgAddReport: TgAddReportDto, tgUserName: String): ReportDto {
        val currentDate = LocalDateTime.now().toString()
        return ReportDto(
            reportDate = currentDate,
            tgUserName = tgUserName,
            taskId = tgAddReport.taskId,
            comment = tgAddReport.comment
        )
    }
}