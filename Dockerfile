FROM eclipse-temurin:17

LABEL mentainer="sinhhung"

WORKDIR /app

COPY target/be_uterace-0.0.1-SNAPSHOT.jar /app/be_uterace.jar

ENTRYPOINT ["java", "-jar", "be_uterace.jar"]