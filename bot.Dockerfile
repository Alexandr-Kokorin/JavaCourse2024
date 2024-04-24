FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=bot/target/bot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8090
