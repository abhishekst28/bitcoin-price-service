# Step 1: Use an official Maven image to build the application
FROM maven:3.8.4-openjdk-17 AS build

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the pom.xml and download the dependencies
COPY pom.xml .

# Step 4: Download all necessary dependencies
RUN mvn dependency:go-offline

# Step 5: Copy the entire application source code
COPY src ./src

# Step 6: Build the Spring Boot application
RUN mvn clean package -DskipTests

# Step 7: Use an OpenJDK image for running the application
FROM openjdk:17-jdk-slim

# Step 8: Set a working directory for the app
WORKDIR /app

# Step 9: Copy the built JAR from the previous build stage
COPY --from=build /app/target/*.jar app.jar

# Step 10: Expose the port the Spring Boot application runs on
EXPOSE 8080

# Step 11: Specify the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
