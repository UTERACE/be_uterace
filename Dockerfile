FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY target/be_uterace.jar be_uterace.jar
ENTRYPOINT ["java", "-jar", "be_uterace.jar"]