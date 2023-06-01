import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String = "2.2.4"
val kotlin_version: String = "1.8.20"
val kmongo_version: String = "4.9.0"
val koin_version: String = "3.4.0"
val logback_version: String = "1.2.11"
val kafka_version:String = "3.4.0"

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
}

group = "ru.rsu.app"
version = "0.0.1"
application {
    mainClass.set("ru.rsu.app.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")

    implementation("com.typesafe:config:1.4.2")

    //Ktor
    implementation("io.ktor:ktor-client-cio-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")


    //LogBack
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Koin
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    //MongoDB
    implementation("org.litote.kmongo:kmongo:$kmongo_version")

    // Telegram bot api
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.7")

    //Kafka
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    implementation("org.apache.kafka:kafka-clients:$kafka_version")

    //For Test
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}