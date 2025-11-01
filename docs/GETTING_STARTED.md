# Getting Started with EcoGlobal

This guide will walk you through setting up and running the EcoGlobal platform on your local machine for development.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21**: The project is built with Java 21. You can use a tool like [SDKMAN!](https://sdkman.io/) to manage Java versions.
- **Docker**: The backend services (PostgreSQL, ActiveMQ) are managed using Docker.
  - [Install Docker Engine](https://docs.docker.com/engine/install/)
  - [Install Docker Compose](https://docs.docker.com/compose/install/)
- **Just**: We use `Justfile` as a command runner for common tasks.
  - [Installation instructions](https://github.com/casey/just#installation)

## 1. Environment Configuration

The application uses an `.env` file to configure environment variables for Docker Compose.

1.  **Copy the example file:**
    ```bash
    cp .env.example .env
    ```
2.  **Review the variables:**
    The default values in `.env` should work for a standard local setup. These variables configure the ports and credentials for the PostgreSQL and ActiveMQ services.

## 2. Start Backend Services

The required backend services are defined in `docker/compose.yaml` and can be started with a single command:

```bash
docker compose -f docker/compose.yaml up -d
```

This will start:
- **PostgreSQL**: The main database for the application.
- **ActiveMQ**: The message broker for asynchronous communication between modules.

You can verify that the services are running with `docker ps`.

## 3. Build the Application

The project uses the Gradle wrapper (`gradlew`) to ensure a consistent build environment.

To build the entire project, including running all unit and integration tests, execute:

```bash
./gradlew build
```

The first build may take some time as it downloads dependencies. A successful build indicates that your environment is set up correctly.

## 4. Run the Application

Once the backend services are running and the project has been built successfully, you can start the Spring Boot application:

```bash
./gradlew :app:bootRun
```

The application will start and connect to the services running in Docker. You should see Spring Boot logs in your terminal. The API will be available at `http://localhost:8080`.

## 5. Common Commands (`Justfile`)

A `Justfile` is provided in the root of the project to simplify common tasks. Once you have `just` installed, you can run the following commands from the project root:

- `just up`: Start all backend services with Docker Compose.
- `just down`: Stop all backend services.
- `just build`: Build the application and run all tests.
- `just run`: Run the main application.
- `just logs`: Tail the logs from the running Docker containers.

Using these commands is the recommended way to interact with the project.
