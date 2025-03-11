FROM ubuntu:24.04

# Prevent interactive prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Install OpenJDK 17 and other necessary tools
RUN apt-get update && \
    apt-get install -y \
    openjdk-17-jdk \
    maven \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Create app directory
WORKDIR /app

# Copy Maven configuration
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Expose the application port
EXPOSE 8081

# Run the application
CMD ["java", "-jar", "target/pay-secure-1.0.0.jar"]