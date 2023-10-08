# Sử dụng Maven để xây dựng ứng dụng
FROM maven AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=build /app/target/be_uterace-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
