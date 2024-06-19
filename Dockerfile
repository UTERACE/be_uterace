FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc trong container
WORKDIR /app

# Sao chép file jar từ thư mục hiện tại vào thư mục làm việc trong container
COPY target/be_uterace-0.0.1-SNAPSHOT.jar /app/be_uterace.jar

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "demo.jar"]