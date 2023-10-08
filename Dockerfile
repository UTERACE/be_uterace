
#
FROM maven AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk
COPY --from=build /target/be_uterace-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
