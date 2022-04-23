FROM maven:3.8.5-openjdk-18 AS builder

CMD maven clean && maven package

FROM openjdk:18-jdk-alpine AS runner
VOLUME /tmp

COPY --from=builder target/Link-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
