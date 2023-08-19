FROM arm64v8/eclipse-temurin
LABEL authors="mikeguzman"
VOLUME /tmp
ARG JAR_FILE="build/libs/taskapp-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]