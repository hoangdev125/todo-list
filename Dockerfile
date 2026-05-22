# --- BƯỚC 1: BUILD TOÀN BỘ PROJECT (Mã nguồn Java + Giao diện React) ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy toàn bộ code từ thư mục gốc vào Docker
COPY . .

# Di chuyển vào thư mục backend
WORKDIR /app/backend

# CẤP QUYỀN THỰC THI CHO FILE mvnw (Thêm dòng này để sửa lỗi Permission denied)
RUN chmod +x ./mvnw

# Chạy lệnh đóng gói
RUN ./mvnw clean package -DskipTests

# --- BƯỚC 2: CHẠY ỨNG DỤNG VỚI IMAGE JAVA NHỎ GỌN ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=build /app/backend/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]