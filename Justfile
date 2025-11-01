# Justfile for EcoGlobal Platform

# Default command to run when no arguments are given
default: build

# Start all backend services in detached mode
up:
    @echo "Starting backend services (PostgreSQL, ActiveMQ)..."
    docker compose -f docker/compose.yaml up -d

# Stop all backend services
down:
    @echo "Stopping backend services..."
    docker compose -f docker/compose.yaml down

# Tail logs from backend services
logs:
    @echo "Tailing logs from backend services..."
    docker compose -f docker/compose.yaml logs -f

# Build the entire application, including running all tests
build:
    @echo "Building the application..."
    ./gradlew build

# Run the Spring Boot application
run:
    @echo "Running the EcoGlobal application..."
    ./gradlew :app:bootRun

# Clean the Gradle build artifacts
clean:
    @echo "Cleaning the build..."
    ./gradlew clean

# Run all tests (unit, integration, and Cucumber)
test:
    @echo "Running all tests..."
    ./gradlew test
