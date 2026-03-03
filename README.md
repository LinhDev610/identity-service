# Identity Service – Cosmetics Website

## Overview

Identity Service là một RESTful API được xây dựng bằng Spring Boot 3, cung cấp chức năng xác thực (authentication) và quản lý người dùng (user management) cho hệ thống Cosmetics Website.

Service này được thiết kế theo kiến trúc phân tầng (layered architecture) và sẵn sàng tích hợp trong môi trường microservice. 

**Lưu ý:** Tất cả API của service này đều bắt đầu bằng context path: `/identity_service`.

---

## Tech Stack

- Java 21 (Amazon Corretto)
- Spring Boot 3.4.1
- Spring Security
- JWT Authentication
- Spring Data JPA
- MySQL 8
- Maven
- Docker

---

## Architecture

Dự án tuân theo mô hình kiến trúc phân tầng:

- Controller Layer: Xử lý HTTP request/response
- Service Layer: Xử lý business logic
- Repository Layer: Truy cập dữ liệu với JPA
- Security Layer: JWT Filter + Authentication Provider

### Authentication Flow

1. Client gửi thông tin đăng nhập đến `/identity_service/auth/token`
2. Server xác thực và trả về JWT access token
3. Client gửi token trong header `Authorization: Bearer <token>`
4. JWT filter kiểm tra và cho phép truy cập endpoint được bảo vệ

---

## System Requirements

### Java Development Kit (JDK)

- Khuyến nghị: JDK 21
- Tối thiểu: JDK 21
- Kiểm tra version:
  ```bash
  java -version
  ```

### Maven

- Version 3.9 trở lên
- Kiểm tra version:
  ```bash
  mvn -version
  ```

### MySQL

- Version 8.0 trở lên
- Port mặc định: 3306

---

## Setup & Run (Local Environment)

### 1. Clone repository

```bash
git clone <repository-url>
cd identity-service
```

### 2. Tạo database

```sql
CREATE DATABASE identity_service;
CREATE USER 'cosmetics_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON identity_service.* TO 'cosmetics_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Cấu hình Environment Variables (Khuyến nghị)

Thay vì hard-code thông tin database, sử dụng biến môi trường:

```bash
export DB_URL=jdbc:mysql://localhost:3306/identity_service
export DB_USERNAME=cosmetics_user
export DB_PASSWORD=your_password
```

Cấu hình trong `application.yaml`:

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### 4. Chạy ứng dụng

Chạy trực tiếp bằng Maven:

```bash
mvn spring-boot:run
```

Hoặc build và chạy file JAR:

```bash
mvn clean package
java -jar target/identity-service-0.9.0-SNAPSHOT.jar
```

---

## API Endpoints

Cấu hinh Context Path: `/identity_service`

### Authentication

- `POST /identity_service/auth/token` – Đăng nhập
- `POST /identity_service/auth/refresh` – Refresh token
- `POST /identity_service/auth/logout` – Đăng xuất
- `POST /identity_service/auth/introspect` – Kiểm tra token

### User Management

- `POST /identity_service/users` – Tạo user mới
- `GET /identity_service/users` – Lấy danh sách users
- `GET /identity_service/users/my-info` – Lấy thông tin user hiện tại

---

## API Example

### Login Request

`POST /identity_service/auth/token`

Request body:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Response:

```json
{
  "code": 1000,
  "result": {
    "token": "jwt_token_here",
    "authenticated": true
  }
}
```

---

## Run with Docker

### 1. Build Docker image

```bash
docker build -t linhdev610/identity-service:0.9.0 .
```

### 2. Tạo Docker network

```bash
docker network create duclinh-network
```

### 3. Chạy MySQL container

```bash
docker run --network duclinh-network \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=identity_service \
  -p 3306:3306 \
  -d mysql:8.0
```

### 4. Chạy Identity Service container

```bash
docker run --network duclinh-network \
  --name identity-service \
  -p 8080:8080 \
  -e DB_URL=jdbc:mysql://mysql:3306/identity_service \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=root \
  linhdev610/identity-service:0.9.0
```

---

## Testing

Chạy unit test:

```bash
mvn test
```

Build production:

```bash
mvn clean package -Pprod
```

---

## Troubleshooting

### Lỗi JDK version

Kiểm tra JAVA_HOME:

```bash
echo $JAVA_HOME
```

Set lại JAVA_HOME nếu cần:

```bash
export JAVA_HOME=/path/to/jdk17
```

### Lỗi database connection

Kiểm tra:

1. MySQL service đang chạy
2. Database đã được tạo
3. Environment variables đã được set đúng
4. Port 3306 không bị chặn

---

## Future Improvements

- Thêm Swagger / OpenAPI Documentation
- Thêm Global Exception Handler
- Thêm DTO validation với @Valid
- Thêm Logging configuration
- Tạo Docker Compose file
- Triển khai CI/CD pipeline

---

## License

MIT License
