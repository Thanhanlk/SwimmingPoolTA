FROM maven:3.9.5 AS builder
COPY . .
RUN mvn package -Dmaven.test.skip=true -DappName=swimming-pool -X

FROM openjdk:17-slim AS runner
EXPOSE 8080
COPY --from=builder /target/swimming-pool.jar swimming-pool.jar
CMD ["java", "-Dserver.port=8080", "-jar", "swimming-pool.jar"]

