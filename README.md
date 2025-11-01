# EcoGlobal: Sustainable Products Platform

EcoGlobal is a B2C e-commerce platform for sustainable, eco-friendly products, serving as a comprehensive example of a modular monolith architecture built with Domain-Driven Design (DDD) and Clean Architecture principles.

## Business Context

EcoGlobal operates in multiple European countries (Netherlands, Germany, France), each with unique logistical, tax, and regulatory requirements. The platform is designed to handle the complexities of multi-warehouse inventory, dynamic shipping and carbon cost calculations, and country-specific business rules.

The core mission is to provide customers with transparent, sustainable purchasing options, which is reflected in the system's core domains.

## Architecture & Design

The project follows a carefully designed architecture to manage complexity and ensure the longevity of the system.

- **Modular Monolith**: The system is built as a single deployable unit, but internally divided into highly decoupled, independently maintainable modules (bounded contexts).
- **Domain-Driven Design (DDD)**: We apply strategic design to classify domains into Core, Supporting, and Generic, which dictates strict dependency rules. Tactical patterns like Aggregates, Value Objects, and Repositories are used to model the business logic.
- **Clean Architecture**: The codebase is separated into `domain`, `application`, and `infrastructure` layers to isolate business rules from technical details.
- **Dependency Inversion**: Used strategically to ensure that core domains do not depend on supporting domains, maintaining a clean and logical dependency graph.
- **Event-Driven Integration**: Bounded contexts communicate asynchronously via events (using ActiveMQ) to maintain loose coupling and improve resilience.

### Core Business Domains

- **Order Fulfillment**: The intelligent heart of the system, responsible for optimizing order fulfillment based on cost, delivery time, and carbon footprint.
- **Carbon Accounting**: A key competitive differentiator, providing transparent and accurate carbon tracking for every order.

For a deeper dive into the architecture and domain classifications, please see:
- `docs/ARCHITECTURE.md`
- `docs/STRATEGIC_DESIGN.md`

## Tech Stack

- **Backend**: Kotlin 2.0 & Spring Boot 3.3
- **Build Tool**: Gradle 8.8 with a `build-logic` composite build for convention plugins
- **Database**: PostgreSQL (managed via Docker Compose)
- **Messaging**: ActiveMQ (managed via Docker Compose)
- **Testing**:
  - JUnit 5 for unit and integration tests
  - Cucumber for Behavior-Driven Development (BDD) and use-case testing
  - Testcontainers for integration testing with real services
- **Local Development**: Docker Compose, Justfile for command automation

## Getting Started

For instructions on how to set up and run the project locally, please refer to the **[Getting Started Guide](docs/GETTING_STARTED.md)**.
