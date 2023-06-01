FROM gradle:jdk17 as builder
COPY --chown=gradle:gradle . /tgbot-app-master-degree/gradle/src
WORKDIR /tgbot-app-master-degree/gradle/src
COPY build.gradle.kts ./build.gradle.kts
RUN gradle clean build

FROM openjdk:17-alpine as backend
WORKDIR /root
COPY --from=builder /tgbot-app-master-degree/gradle/src/build/libs/* ./app
ENTRYPOINT ["java", "-jar", "/root/app"]