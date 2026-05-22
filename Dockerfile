# --- BƯỚC 1: BUILD TOÀN BỘ PROJECT (Mã nguồn Java + Giao diện React) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy toàn bộ code từ thư mục gốc (bao gồm cả backend và frontend) vào Docker
COPY . .

# Di chuyển vào thư mục backend để chạy lệnh gộp file
WORKDIR /app/backend
RUN ./mvnw clean package -DskipTests

# --- BƯỚC 2: CHẠY ỨNG DỤNG VỚI IMAGE JAVA NHỎ GỌN ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy file .jar đã đóng gói từ Bước 1 sang Bước 2 để chạy
# Spring Boot 4.x / 3.x mặc định sẽ sinh file jar trong thư mục target của backend
COPY --from=build /app/backend/target/*.jar app.jar

# Mở cổng 8080 cho ứng dụng trên Render
EXPOSE 8080

# Lệnh khởi chạy file jar
ENTRYPOINT ["java", "-jar", "app.jar"]