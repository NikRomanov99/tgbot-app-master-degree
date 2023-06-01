package ru.rsu.app.util

import ru.rsu.app.model.dto.ReportDto
import ru.rsu.app.model.dto.TaskInfoResponseDto
import java.lang.StringBuilder

class TgMessageBuilder {
    companion object {
        fun buildMsgForTasks(tasks: List<TaskInfoResponseDto>): String {
            var sb = StringBuilder("Ваш список задач:\n\n")
            if (tasks.isEmpty()) {
                sb.append("На данный момент нет задач\n")
            } else {
                tasks.forEach { task ->
                    val taskMsg = "Идентификатор задачи: ${task.id} \n " +
                            "Наименование:  ${task.name} \n" +
                            "Описание:  ${task.taskDescription} \n" +
                            "Тип задачи:  ${task.taskTypeName} \n" +
                            "Статус:  ${task.taskStatusName} \n" +
                            "Идентификатор оборудования:  ${task.availableEquipmentId} \n" +
                            "Время постановки:  ${task.startTaskDate} \n" +
                            "Крайняя дата выполнения:  ${task.startTaskDate} \n" +
                            "Постановщик:  ${task.userDirectorName} \n\n"
                    sb.append(taskMsg)
                }
            }

            return sb.toString()
        }

        fun buildMsgForReport(report: ReportDto): String {
            var sb = StringBuilder("Отчёт о выполнении задачи:\n\n")
            val reportMsg = "Дата отчёта: ${report.reportDate} \n" +
                    "Идентификатор задачи: ${report.taskId} \n" +
                    "Телеграмм автора отчёта:  ${report.tgUserName} \n" +
                    "Комментраий к задаче:  ${report.comment} \n"
            sb.append(reportMsg)

            return sb.toString()
        }
    }
}