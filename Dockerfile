FROM maven:3.9.2-eclipse-temurin-20-alpine as builder
COPY ./src src/
COPY ./pom.xml pom.xml
RUN mvn clean package -DskipTests
FROM eclipse-temurin:20-jre-alpine
COPY --from=builder target/*.jar app.jar
RUN mkdir -p /app/src/main/resources/static/images && chmod 777 /app/src/main/resources/static/images
EXPOSE 10000
CMD ["java","-jar","app.jar"]
