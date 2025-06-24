# Stage 1: Build the application with Maven
FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copy pom.xml and source code
#COPY pom.xml .
#COPY src ./src
COPY . .

# Package the application (without tests)
RUN mvn clean package -DskipTests

# Stage 2: Extract layers from the built jar
FROM eclipse-temurin:17.0.1_12-jdk AS extracted

ARG JAR_FILE=target/*.jar
WORKDIR /build

# Copy the built jar file
#ADD ${JAR_FILE} application.jar
COPY --from=build /app/target/${JAR_FILE} application.jar


# Extract layers for Spring Boot optimized startup
RUN java -Djarmode=layertools -jar application.jar extract --destination extracted

# Stage 3: Final runtime image
FROM eclipse-temurin:17.0.1_12-jdk

VOLUME /tmp
WORKDIR /application

# Copy extracted layers
COPY --from=extracted /build/extracted/dependencies/ ./
COPY --from=extracted /build/extracted/spring-boot-loader/ ./
COPY --from=extracted /build/extracted/snapshot-dependencies/ ./
COPY --from=extracted /build/extracted/application/ ./

# Run the application
#ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "org.springframework.boot.loader.launch.JarLauncher"]