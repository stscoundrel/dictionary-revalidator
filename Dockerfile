# Specify the base image
FROM maven:3.9-eclipse-temurin-17-alpine

# Copy the application files
COPY . /app

# Set the working directory
WORKDIR /app

# Build the application
RUN mvn clean package

# Expose the application port
EXPOSE 8080

# Specify the command to run the application
CMD ["java", "-jar", "target/revalidator-0.0.1-SNAPSHOT.jar"]