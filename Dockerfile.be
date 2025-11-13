# ------------------------------------
# Stage 1: Build Ứng dụng (Builder)
# Project uses Java 17 and Maven
# ------------------------------------
FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file cấu hình Maven (pom.xml)
COPY pom.xml .

# Tải dependencies trước để tận dụng Docker cache.
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn
COPY src /app/src

# Thực hiện build. Lệnh này tạo ra file JAR/WAR trong thư mục target.
RUN mvn clean package -DskipTests

# ------------------------------------
# Stage 2: Runtime (Chạy Ứng dụng)
# Sử dụng JRE 17 tối giản để giảm kích thước image
# ------------------------------------
FROM eclipse-temurin:17-jre-alpine

# Đặt biến môi trường cho Spring Boot Context Path
ENV SERVER_SERVLET_CONTEXT_PATH=/carshop

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ stage 'build'
COPY --from=build /app/target/*.jar app.jar

# Mở cổng mặc định của Spring Boot
EXPOSE 8080

# Chạy ứng dụng Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]