package ru.rsu.app.repository

import org.litote.kmongo.KMongo
import org.litote.kmongo.find
import org.litote.kmongo.getCollection
import ru.rsu.app.util.PropertyConfigUtils
import ru.rsu.app.model.entity.Reports

class ReportRepository(private val propertyConfigUtils: PropertyConfigUtils) {

    private val dbConfig = propertyConfigUtils.getDbProperty()
    private val client =
        KMongo.createClient(
            "mongodb://${dbConfig.username}:${dbConfig.password}@${dbConfig.url}/${dbConfig.reportDb}?authSource=${dbConfig.reportDb}")
    private val database = client.getDatabase(dbConfig.reportDb)
    private val reportCollection = database.getCollection<Reports>()

    fun selectAllReport(): List<Reports> {
        return reportCollection.find().toList()
    }

    fun selectAllReportByUserTgName(tgUserName: String): List<Reports> {
        return reportCollection.find(" {\"tgUserName\" : \"$tgUserName\"}").toList()
    }

    fun insertReport(reports: Reports) {
        reportCollection.insertOne(reports)
    }
}