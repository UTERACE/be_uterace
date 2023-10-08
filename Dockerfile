FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY /target/be_uterace-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]