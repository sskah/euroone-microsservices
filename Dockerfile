FROM maven:3.9.8-eclipse-temurin-17-alpine AS build
WORKDIR /opt/app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-alpine-3.23
WORKDIR /opt/app
COPY --from=build /opt/app/target/app.jar /opt/app/app.jar

# Perfil padrão da aplicação (dev). Pode ser sobrescrito via -e no docker run,
# ex.:  docker run -e SPRING_PROFILES_ACTIVE=prd euroone-api:1.0.0
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8080
CMD [ "java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar" ]
