FROM node:alpine AS Frontend-Builder
COPY assets .

RUN apk add curl
RUN curl -f https://get.pnpm.io/v6.16.js | node - add --global pnpm

RUN pnpm install --frozen-lockfile --prod
RUN pnpm run build

FROM maven:3-openjdk-18 AS Builder
COPY . .
COPY --from=Frontend-Builder build src/main/resources/public
RUN mvn clean
RUN mvn package

FROM openjdk:18
COPY --from=Builder target/Link-*.jar app.jar
ENTRYPOINT java -jar app.jar
