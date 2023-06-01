package ru.rsu.app.repository

import org.litote.kmongo.KMongo
import org.litote.kmongo.find
import org.litote.kmongo.getCollection
import ru.rsu.app.model.entity.UserData
import ru.rsu.app.util.PropertyConfigUtils

class TgUserDataRepository(private val propertyConfigUtils: PropertyConfigUtils) {
    private val dbConfig = propertyConfigUtils.getDbProperty()
    private val client =
        KMongo.createClient("mongodb://${dbConfig.username}:${dbConfig.password}@${dbConfig.url}/${dbConfig.userDataDb}?authSource=${dbConfig.userDataDb}")
    private val database = client.getDatabase(dbConfig.userDataDb)
    private val userDataCollection = database.getCollection<UserData>()

    fun selectAllUserData(): List<UserData> {
        return userDataCollection.find().toList()
    }

    fun selectUserDataByTgUserName(tgUserName: String): UserData {
        return userDataCollection.find(" {\"_id\" : \"$tgUserName\"}").first()!!
    }

    fun insertUserData(userData: UserData) {
        userDataCollection.insertOne(userData)
    }
}