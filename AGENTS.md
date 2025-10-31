# EcoGlobal `AGENTS.md`

This file provides guidance for AI agents working on the EcoGlobal project, a modular monolith built with Domain-Driven Design (DDD) principles.

## 1. Architecture Overview

The system is a **modular monolith** composed of multiple bounded contexts, each representing a specific business capability. The architecture emphasizes a clean separation of concerns and follows these key principles:

- **Clean Architecture**: Separates the project into `domain`, `application`, and `infrastructure` layers.
- **Strategic Design**: Guides module dependencies based on domain classification.
- **Modular Monolith**: Code is organized into independent modules, but deployed as a single unit.

## 2. Domain Classification

Domains are classified into three types, which dictates their dependencies and role in the system:

- **Core Domains**: The competitive advantage of the business (e.g., `order-fulfillment`, `carbon-accounting`). They should have **ZERO** dependencies on other domains' implementation modules.
- **Supporting Domains**: Necessary for business operations but not a competitive advantage (e.g., `product-catalog`, `inventory-management`). They support the core domains.
- **Generic Subdomains**: Reusable, domain-agnostic utilities (e.g., `common-time`, `common-country`).

## 3. Dependency Rules

Dependencies are strictly controlled to maintain a clean and scalable architecture.

- **Golden Rule**:
  - `Core` domains should **only** depend on `Generic` subdomains.
  - `Supporting` domains can depend on `Generic` and other `Supporting` domains.
  - `Generic` subdomains have no dependencies on other domains.

- **Dependency Inversion**: Use this pattern **only** when a `Core` domain needs data from a `Supporting` domain.
  1. The `Core` domain defines an interface in its `api` module.
  2. The `Supporting` domain implements that interface in its `impl` module.
  - **Never** use dependency inversion for `Generic` subdomains.

- **Event-Driven Integration**: Use events for asynchronous communication and to decouple bounded contexts. This is the preferred method for cross-domain updates where eventual consistency is acceptable.

## 4. Bounded Context Structure

Each bounded context is organized into three main parts:

- **`-api` module**: The public interface of the domain. This is what other modules depend on. It contains:
  - Public value objects and DTOs.
  - Domain events.
  - Provider interfaces (for dependency inversion).
- **`-impl` module**: The implementation details, structured into layers:
  - `application`: Use cases that orchestrate domain logic.
  - `domain`: Aggregates, entities, value objects, and repository interfaces. This is the heart of the bounded context.
  - `infrastructure`: Technical concerns like persistence (database), REST controllers, and messaging.
- **`-worldview` module**: Contains realistic, pre-populated data for testing and development, ensuring that test scenarios reflect real-world usage.

## 5. Key Patterns & Best Practices

- **Rich Domain Model**: Business logic should reside within domain objects (Aggregates, Entities, VOs), not in application services. Avoid anemic domain models.
- **Repository per Aggregate**: Each aggregate root has its own repository. Do not create repositories for entities within an aggregate.
- **Value Objects**: Use strongly-typed, self-validating value objects instead of primitives (e.g., `Ean18` instead of `String`, `Money` instead of `Double`).
- **Immutability**: Strive for immutability, especially in value objects and domain events. Use Kotlin's `data class` with `val` properties.

## 6. Testing Strategy

Follow the testing pyramid:

- **Unit Tests**: Test domain logic in isolation (no Spring context). These should be fast and numerous.
- **Integration Tests**: Test infrastructure components like repositories and controllers with Spring and an embedded database.
- **Use Case Tests (Cucumber)**: Write end-to-end tests for user-facing scenarios using Gherkin (`.feature` files). These tests use the `worldview` data to simulate a realistic environment.

When writing code, ensure you add appropriate tests at the correct level of the pyramid.
