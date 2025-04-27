FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app
#RUN addgroup --system javauser && adduser -S -s /usr/sbin/nologin -G javauser javauser
COPY --from=builder /build/target/backend-0.0.1-SNAPSHOT.jar app.jar
#RUN chown -R javauser:javauser .
#USER javauser
ENTRYPOINT ["java", "-jar", "app.jar"]