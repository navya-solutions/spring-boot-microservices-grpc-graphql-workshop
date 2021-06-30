FROM maven:3.6-openjdk-16-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src

RUN mvn -B -f pom.xml clean package -DskipTests



FROM openjdk:16-slim
COPY --from=build /workspace/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]