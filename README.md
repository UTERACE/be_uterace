# UTE_RACE

Đây là một ứng dụng Spring Boot tổ chức giải chạy bộ online.

## Yêu cầu

- JDK (Java Development Kit) - phiên bản 17 trở lên
- Maven - phiên bản 3 trở lên
- Spring boot - phiên bản 3.1.3

## Cài đặt và chạy

1. **Clone dự án từ GitHub:**

   ```bash
   git clone https://github.com/UTERACE/be_uterace
2. **Chạy ứng dụng bằng Maven**
    ```bash
   cd be_uterace
    mvn clean install
    mvn spring-boot:run
3. **Chạy ứng dụng bằng Docker**
   ```bash
   cd be_uterace
    mvn clean install
    docker build -t your-docker-image-name .
   docker run -p 8080:8080 your-docker-image-name
5.  **Truy cập ứng dụng**
  http://localhost:8080


