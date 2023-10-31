#FROM gradle:7.2.0-jdk17 as build
#COPY api /app/api
#COPY common /app/common
#COPY settings.gradle /app/settings.gradle
#WORKDIR /app/api
#RUN gradle build --no-daemon
#
#FROM eclipse-temurin:17-jre-alpine
#COPY  --from=build /app/api/build/libs/*.jar /app/app.jar
#
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

## Bước 1: Build module Liquibase
#FROM gradle:7.2.0-jdk17 as liquibase_build
#COPY migration /app/migration
#WORKDIR /app/migration
#RUN gradle build --no-daemon
#
## Bước 2: Build module API
#FROM gradle:7.2.0-jdk17 as api_build
#COPY api /app/api
#COPY common /app/common
#COPY settings.gradle /app/settings.gradle
#WORKDIR /app/api
#RUN gradle build --no-daemon
#
## Bước 3: Sử dụng Eclipse Temurin JRE Alpine
#FROM eclipse-temurin:17-jre-alpine

FROM gradle:7.4-jdk17-alpine AS build
WORKDIR /app

COPY . .

RUN gradle build --no-daemon -x test

FROM openjdk:17-alpine

# Sao chép tệp jar
COPY --from=build /app/migration/build/libs/*.jar /app/migration.jar
COPY --from=build /app/api/build/libs/*.jar /app/app.jar

# Thêm lệnh chạy Liquibase trước khi chạy ứng dụng Spring Boot
#CMD java -jar /app/migration.jar && java -jar /app/app.jar