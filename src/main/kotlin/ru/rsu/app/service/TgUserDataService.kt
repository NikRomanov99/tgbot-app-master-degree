package ru.rsu.app.service

import ru.rsu.app.model.entity.UserData
import ru.rsu.app.repository.TgUserDataRepository

class TgUserDataService(private val tgUserDataRepository: TgUserDataRepository) {
    fun addUserData(userData: UserData) {
        tgUserDataRepository.insertUserData(userData)
    }

    fun getUserDataByTgUserName(tgUserName: String): UserData {
        return tgUserDataRepository.selectUserDataByTgUserName(tgUserName)
    }
}