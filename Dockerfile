FROM node:alpine AS Frontend-Builder
COPY assets .
RUN npm install
RUN npm run build

FROM maven:3-openjdk-18 AS Builder
COPY . .
COPY --from=Frontend-Builder build src/main/resources/public
RUN cp src/main/resources/application.example.properties src/main/resources/application.properties
RUN mvn clean
RUN mvn package

FROM openjdk:18
COPY --from=Builder target/Link-*.jar app.jar
ENTRYPOINT java -jar app.jar
