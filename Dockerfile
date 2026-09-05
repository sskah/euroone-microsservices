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

# Locale/encoding UTF-8: sem isso a JVM no Alpine assume ASCII e a
# acentuacao gravada no banco fica corrompida.
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8"

EXPOSE 8080
CMD [ "java", "-Dfile.encoding=UTF-8", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar" ]
