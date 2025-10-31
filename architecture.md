# Comprehensive Instructions for Building a DDD Skeleton Project

## Overview

Create a skeleton project following Domain-Driven Design, Clean Architecture, and Modular Monolith principles. This is a monorepo containing multiple deployable applications, each composed of bounded contexts (domain modules), with shared common utilities.

**Key Principles:**
- Aggregates are the unit of consistency
- Bounded contexts define module boundaries
- Strategic design guides dependencies
- Explicit separation of domain, application, and infrastructure layers

---

## 1. Strategic Design First

### 1.1 Domain Classification

Before creating modules, classify your domains:

**Core Domain** (your competitive advantage)
- Contains your unique business logic
- Should have ZERO dependencies on other domains
- Example: `imbalance-calculation`, `trading-strategy`

**Supporting Domains** (necessary but not differentiating)
- Support the core domain
- Can depend on generic domains
- Cannot be depended upon by core domain
- Example: `allocation`, `metering-data`

**Generic Subdomains** (could be bought or reused)
- Highly reusable, domain-agnostic
- Can be depended upon by any domain
- Example: `common-time`, `common-ean`, `notifications`

### 1.2 Dependency Rules

```
┌─────────────────┐
│  Core Domain    │ ← [Application only]
└─────────────────┘
        ↓ (allowed)
┌─────────────────┐
│ Generic Domains │ ← [Everyone can depend]
└─────────────────┘
        ↑
┌─────────────────┐
│Supporting Domain│ ← [Application, other supporting]
└─────────────────┘
```

**Default Rule: Direct dependencies are fine when strategic design allows it.**

**Use dependency inversion ONLY when:**
- Core domain needs data from supporting domain (wrong direction)
- You need to break a circular dependency
- Testing requires it

**Never use dependency inversion when:**
- Any domain uses a generic domain (just depend directly)
- Supporting domain uses another supporting domain (just depend directly)
- Application layer orchestrates domains (just depend directly)

---

## 2. Project Structure Foundation

### 2.1 Root Structure

```
project-root/
├── build-logic/                           # Gradle conventions
├── common/                                # Generic subdomains
│   ├── common-time/
│   ├── common-ean/
│   └── common-data-jdbc/
├── [deployable-name]/                     # e.g., "nma", "trader"
│   ├── application/                       # Application layer
│   ├── [domain-name]/                     # Bounded context
│   │   ├── [domain-name]-api/            # Public API
│   │   └── [domain-name]-impl/           # Implementation
│   │       ├── application/              # Use cases
│   │       ├── domain/                   # Domain model
│   │       └── infrastructure/           # Technical concerns
│   └── test/
│       └── use-case/                     # Cucumber tests
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### 2.2 Settings Configuration

**settings.gradle.kts:**
```kotlin
rootProject.name = "your-project-name"

includeBuild("build-logic")
includeBuild("common")

// Deployable with its domains
include(":deployable-name:application")
include(":deployable-name:domain-name:domain-name-api")
include(":deployable-name:domain-name:domain-name-impl")
include(":deployable-name:test:use-case")
```

---

## 3. Generic Subdomain Structure

Generic subdomains are the foundation. They:
- Contain NO business logic
- Are domain-agnostic
- Can be depended upon by everyone
- Should be very stable

### 3.1 Example: common-ean

**common/common-ean/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
}
```

**Ean18.kt (Value Object):**
```kotlin
package com.company.common.ean

/**
 * EAN-18 identifier for electricity connections.
 * Immutable value object with built-in validation.
 */
data class Ean18(val value: String) {
    init {
        require(value.length == 18) { "EAN18 must be exactly 18 characters, got ${value.length}" }
        require(value.all { it.isDigit() }) { "EAN18 must contain only digits" }
    }
    
    override fun toString(): String = value
}
```

**Key Pattern:** Value objects are immutable, self-validating, and express domain concepts.

---

## 4. Bounded Context (Domain Module) Structure

### 4.1 Module Organization

Each bounded context has clear layers:

```
domain-name-impl/
├── application/              # Application Layer (Use Cases)
│   ├── CreateAllocationUseCase.kt
│   └── CalculateDailyTotalsUseCase.kt
├── domain/                   # Domain Layer
│   ├── model/               # Aggregates, Entities, Value Objects
│   │   ├── Allocation.kt
│   │   ├── AllocationId.kt
│   │   └── Quantity.kt
│   ├── service/             # Domain Services
│   │   └── AllocationCalculator.kt
│   └── repository/          # Repository Interfaces
│       └── AllocationRepository.kt
└── infrastructure/           # Infrastructure Layer
    ├── persistence/
    │   ├── AllocationEntity.kt
    │   ├── AllocationRepositoryJdbc.kt
    │   └── AllocationEntityMappers.kt
    ├── rest/
    │   └── v1/
    │       ├── AllocationControllerV1.kt
    │       └── AllocationDtos.kt
    └── messaging/
        └── AllocationEventPublisher.kt
```

### 4.2 API Module (Public Interface)

The API module defines what other bounded contexts can use:

**domain-name-api/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
}

dependencies {
    // ONLY common/generic dependencies allowed
    api("com.company.common:time")
    api("com.company.common:ean")
}
```

**What goes in API:**
- Query service interfaces (when needed by other domains)
- Domain events (for async integration)
- Shared DTOs for external communication
- Public value objects

**What does NOT go in API:**
- Aggregate internals
- Use case implementations
- Repository interfaces (domain-internal)
- Infrastructure concerns

**Example Query Service (only when needed):**
```kotlin
package com.company.allocation

import com.company.common.time.DayNL
import com.company.common.ean.Ean18

/**
 * Query interface for other domains to retrieve allocation data.
 * Only expose this if other domains NEED to query allocations.
 * Default: don't expose query services unless necessary.
 */
interface AllocationQueryService {
    fun findTotalForDay(day: DayNL): List<AllocationTotal>
}

data class AllocationTotal(
    val ean18: Ean18,
    val totalKwh: Double
)
```

**Domain Event:**
```kotlin
package com.company.allocation

import com.company.common.time.DayNL

/**
 * Published when allocation processing completes.
 * Other domains can react to this event.
 */
data class AllocationCompleteEvent(
    val operatingDay: DayNL,
    val allocationCount: Int
)
```

---

## 5. Implementation Module Structure

### 5.1 Build Configuration

**domain-name-impl/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
    id("buildlogic.cucumber-conventions")
}

dependencies {
    // Own API
    api(project(":deployable-name:domain-name:domain-name-api"))
    
    // Direct dependencies on generic domains (NO dependency inversion)
    implementation("com.company.common:time")
    implementation("com.company.common:ean")
    
    // Direct dependencies on other domains IF strategic design allows
    // Supporting domain can depend on another supporting domain directly
    implementation(project(":deployable-name:meter-data:meter-data-api"))
    
    // Use dependency inversion ONLY if core domain needs supporting domain
    // In that case, define interface in core-api and implement in supporting-impl
    
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.jms:jakarta.jms-api")
    
    // Testing
    testImplementation(project(":deployable-name:application"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

### 5.2 Domain Layer (Core Business Logic)

#### 5.2.1 Aggregates

**Allocation.kt (Aggregate Root):**
```kotlin
package com.company.allocation.domain.model

import com.company.common.time.DayNL
import com.company.common.ean.Ean18

/**
 * Allocation aggregate root.
 * Represents energy allocation for a connection on a specific day.
 * 
 * Aggregate invariants:
 * - Quantity must be non-negative
 * - Cannot modify after finalized
 */
data class Allocation private constructor(
    val id: AllocationId?,
    val ean18: Ean18,
    val operatingDay: DayNL,
    val quantity: Quantity,
    val status: AllocationStatus
) {
    companion object {
        fun create(
            ean18: Ean18,
            operatingDay: DayNL,
            quantity: Quantity
        ): Allocation {
            require(quantity.value >= 0.0) { "Quantity cannot be negative" }
            
            return Allocation(
                id = null,
                ean18 = ean18,
                operatingDay = operatingDay,
                quantity = quantity,
                status = AllocationStatus.DRAFT
            )
        }
    }
    
    fun finalize(): Allocation {
        check(status == AllocationStatus.DRAFT) { "Can only finalize draft allocations" }
        return copy(status = AllocationStatus.FINALIZED)
    }
    
    fun adjustQuantity(newQuantity: Quantity): Allocation {
        check(status == AllocationStatus.DRAFT) { "Cannot modify finalized allocation" }
        require(newQuantity.value >= 0.0) { "Quantity cannot be negative" }
        return copy(quantity = newQuantity)
    }
}
```

**AllocationId.kt (Value Object):**
```kotlin
package com.company.allocation.domain.model

data class AllocationId(val value: Long) {
    init {
        require(value > 0) { "AllocationId must be positive" }
    }
}
```

**Quantity.kt (Value Object):**
```kotlin
package com.company.allocation.domain.model

/**
 * Energy quantity in kWh.
 * Immutable value object.
 */
data class Quantity(val value: Double) {
    init {
        require(value.isFinite()) { "Quantity must be finite" }
    }
    
    operator fun plus(other: Quantity): Quantity = Quantity(value + other.value)
    operator fun minus(other: Quantity): Quantity = Quantity(value - other.value)
    
    companion object {
        val ZERO = Quantity(0.0)
    }
}
```

**AllocationStatus.kt:**
```kotlin
package com.company.allocation.domain.model

enum class AllocationStatus {
    DRAFT,
    FINALIZED
}
```

#### 5.2.2 Repository Interface (in domain layer)

```kotlin
package com.company.allocation.domain.repository

import com.company.allocation.domain.model.*
import com.company.common.time.DayNL

/**
 * Repository for Allocation aggregate.
 * Interface defined in domain layer, implemented in infrastructure.
 */
interface AllocationRepository {
    fun save(allocation: Allocation): Allocation
    fun findById(id: AllocationId): Allocation?
    fun findByDay(day: DayNL): List<Allocation>
}
```

**Key Principle:** One repository per aggregate root. No repositories for entities inside aggregates.

#### 5.2.3 Domain Services

```kotlin
package com.company.allocation.domain.service

import com.company.allocation.domain.model.*

/**
 * Domain service for allocation calculations.
 * Contains domain logic that doesn't belong to a single aggregate.
 */
class AllocationCalculator {
    
    fun calculateTotal(allocations: List<Allocation>): Quantity {
        return allocations
            .map { it.quantity }
            .fold(Quantity.ZERO) { acc, q -> acc + q }
    }
    
    fun calculatePercentage(allocation: Allocation, total: Quantity): Double {
        if (total.value == 0.0) return 0.0
        return (allocation.quantity.value / total.value) * 100.0
    }
}
```

### 5.3 Application Layer (Use Cases)

Application services orchestrate domain objects, handle transactions, and coordinate infrastructure.

**CreateAllocationUseCase.kt:**
```kotlin
package com.company.allocation.application

import com.company.allocation.domain.model.*
import com.company.allocation.domain.repository.AllocationRepository
import com.company.allocation.infrastructure.messaging.AllocationEventPublisher
import com.company.common.time.DayNL
import com.company.common.ean.Ean18
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Use case for creating a new allocation.
 * Orchestrates domain objects and infrastructure.
 */
@Service
class CreateAllocationUseCase(
    private val allocationRepository: AllocationRepository,
    private val eventPublisher: AllocationEventPublisher
) {
    
    @Transactional
    fun execute(command: CreateAllocationCommand): Result<AllocationId> {
        return try {
            // Create aggregate using factory method
            val allocation = Allocation.create(
                ean18 = command.ean18,
                operatingDay = command.operatingDay,
                quantity = command.quantity
            )
            
            // Save via repository
            val saved = allocationRepository.save(allocation)
            val allocationId = requireNotNull(saved.id) { "Saved allocation must have ID" }
            
            // Publish domain event (if needed)
            eventPublisher.publishCreated(saved)
            
            Result.success(allocationId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class CreateAllocationCommand(
    val ean18: Ean18,
    val operatingDay: DayNL,
    val quantity: Quantity
)
```

**CalculateDailyTotalsUseCase.kt:**
```kotlin
package com.company.allocation.application

import com.company.allocation.domain.repository.AllocationRepository
import com.company.allocation.domain.service.AllocationCalculator
import com.company.allocation.domain.model.Quantity
import com.company.common.time.DayNL
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CalculateDailyTotalsUseCase(
    private val allocationRepository: AllocationRepository,
    private val calculator: AllocationCalculator
) {
    
    @Transactional(readOnly = true)
    fun execute(day: DayNL): DailyTotalsResult {
        val allocations = allocationRepository.findByDay(day)
        val total = calculator.calculateTotal(allocations)
        
        return DailyTotalsResult(
            day = day,
            count = allocations.size,
            total = total
        )
    }
}

data class DailyTotalsResult(
    val day: DayNL,
    val count: Int,
    val total: Quantity
)
```

### 5.4 Infrastructure Layer

#### 5.4.1 Persistence

**AllocationEntity.kt:**
```kotlin
package com.company.allocation.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table(name = "allocation")
internal data class AllocationEntity(
    @Id val id: Long?,
    val ean18: String,
    val operatingDay: LocalDate,
    val quantityKwh: Double,
    val status: String
)
```

**AllocationEntityMappers.kt:**
```kotlin
package com.company.allocation.infrastructure.persistence

import com.company.allocation.domain.model.*
import com.company.common.time.DayNL
import com.company.common.ean.Ean18

internal fun AllocationEntity.toDomain(): Allocation = Allocation.create(
    ean18 = Ean18(this.ean18),
    operatingDay = DayNL(this.operatingDay),
    quantity = Quantity(this.quantityKwh)
).let {
    // Reconstitute with ID and status
    it.copy(
        id = this.id?.let { AllocationId(it) },
        status = AllocationStatus.valueOf(this.status)
    )
}

internal fun Allocation.toEntity(): AllocationEntity = AllocationEntity(
    id = this.id?.value,
    ean18 = this.ean18.value,
    operatingDay = this.operatingDay.date,
    quantityKwh = this.quantity.value,
    status = this.status.name
)
```

**AllocationRepositoryImpl.kt:**
```kotlin
package com.company.allocation.infrastructure.persistence

import com.company.allocation.domain.model.*
import com.company.allocation.domain.repository.AllocationRepository
import com.company.common.time.DayNL
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/**
 * Implementation of AllocationRepository.
 * Adapts Spring Data JDBC to domain repository interface.
 */
@Repository
internal class AllocationRepositoryImpl(
    private val jdbc: AllocationRepositoryJdbc
) : AllocationRepository {
    
    override fun save(allocation: Allocation): Allocation =
        jdbc.save(allocation.toEntity()).toDomain()
    
    override fun findById(id: AllocationId): Allocation? =
        jdbc.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    
    override fun findByDay(day: DayNL): List<Allocation> =
        jdbc.findByOperatingDay(day.date)
            .map { it.toDomain() }
}

internal interface AllocationRepositoryJdbc : CrudRepository<AllocationEntity, Long> {
    fun findByOperatingDay(operatingDay: LocalDate): List<AllocationEntity>
}
```

#### 5.4.2 REST Controllers

**AllocationControllerV1.kt:**
```kotlin
package com.company.allocation.infrastructure.rest.v1

import com.company.allocation.application.*
import com.company.allocation.domain.model.*
import com.company.common.security.RequiresRoleOperator
import com.company.common.time.DayNL
import com.company.common.ean.Ean18
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

/**
 * REST controller for allocation endpoints.
 * Belongs to infrastructure layer, delegates to use cases.
 */
@RestController
@RequestMapping("/api/v1/allocations")
@RequiresRoleOperator
internal class AllocationControllerV1(
    private val createAllocationUseCase: CreateAllocationUseCase,
    private val calculateDailyTotalsUseCase: CalculateDailyTotalsUseCase
) {
    
    @PostMapping
    fun create(@RequestBody request: CreateAllocationRequestV1): ResponseEntity<AllocationResponseV1> {
        val command = request.toCommand()
        
        return createAllocationUseCase.execute(command).fold(
            onSuccess = { allocationId ->
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(AllocationResponseV1(id = allocationId.value))
            },
            onFailure = { throw it }
        )
    }
    
    @GetMapping("/daily-total")
    fun getDailyTotal(@RequestParam date: LocalDate): ResponseEntity<DailyTotalResponseV1> {
        val result = calculateDailyTotalsUseCase.execute(DayNL(date))
        return ResponseEntity.ok(result.toResponse())
    }
}

data class CreateAllocationRequestV1(
    val ean18: String,
    val operatingDay: LocalDate,
    val quantityKwh: Double
)

data class AllocationResponseV1(
    val id: Long
)

data class DailyTotalResponseV1(
    val date: LocalDate,
    val count: Int,
    val totalKwh: Double
)

// Mappers in same file (infrastructure concern)
private fun CreateAllocationRequestV1.toCommand() = CreateAllocationCommand(
    ean18 = Ean18(this.ean18),
    operatingDay = DayNL(this.operatingDay),
    quantity = Quantity(this.quantityKwh)
)

private fun DailyTotalsResult.toResponse() = DailyTotalResponseV1(
    date = this.day.date,
    count = this.count,
    totalKwh = this.total.value
)
```

---

## 6. Cross-Bounded Context Integration

### 6.1 Default: Direct Dependencies

**When strategic design allows, use direct dependencies:**

```kotlin
// Supporting domain depending on generic domain (CORRECT - direct dependency)
dependencies {
    implementation("com.company.common:ean")
}

// Supporting domain A depending on supporting domain B (CORRECT - direct dependency)
dependencies {
    implementation(project(":nma:meter-data:meter-data-api"))
}
```

### 6.2 Dependency Inversion (Only When Needed)

**Use ONLY when dependency direction is wrong according to strategic design:**

**Scenario:** Core domain needs data from supporting domain (wrong direction!)

**Step 1: Define interface in CORE domain API:**
```kotlin
// In core-domain-api
package com.company.imbalance

import com.company.common.time.DayNL

/**
 * Provider interface defined by core domain.
 * Implementation will be in supporting domain.
 */
interface MeterDataProvider {
    fun getMeterData(day: DayNL): List<MeterReading>
}

data class MeterReading(
    val ean18: Ean18,
    val timestampUtc: Instant,
    val value: Double
)
```

**Step 2: Implement in SUPPORTING domain:**
```kotlin
// In meter-data-impl
package com.company.meterdata.infrastructure

import com.company.imbalance.MeterDataProvider
import com.company.meterdata.application.QueryMeterDataUseCase
import org.springframework.stereotype.Component

@Component
internal class MeterDataProviderImpl(
    private val queryUseCase: QueryMeterDataUseCase
) : MeterDataProvider {
    
    override fun getMeterData(day: DayNL): List<MeterReading> {
        return queryUseCase.execute(day)
            .map { it.toMeterReading() }
    }
}
```

**Step 3: Core domain uses interface:**
```kotlin
// In core-domain-impl
@Service
class ImbalanceCalculationUseCase(
    private val meterDataProvider: MeterDataProvider  // Injected
) {
    fun execute(day: DayNL): ImbalanceResult {
        val meterData = meterDataProvider.getMeterData(day)
        // Calculate imbalance
    }
}
```

**Build dependencies:**
```kotlin
// core-domain-impl build.gradle.kts
dependencies {
    // Core domain API (contains interface)
    api(project(":nma:imbalance:imbalance-api"))
    
    // NO dependency on meter-data! Dependency is inverted.
}

// meter-data-impl build.gradle.kts
dependencies {
    implementation(project(":nma:meter-data:meter-data-api"))
    
    // Depends on core domain API to implement interface
    implementation(project(":nma:imbalance:imbalance-api"))
}
```

### 6.3 Event-Driven Integration

Use for:
- Decoupling bounded contexts
- Eventual consistency scenarios
- Broadcasting state changes

**Publishing Events:**
```kotlin
package com.company.allocation.infrastructure.messaging

import com.company.allocation.domain.model.Allocation
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class AllocationEventPublisher(
    private val jmsTemplate: JmsTemplate
) {
    fun publishCreated(allocation: Allocation) {
        val event = AllocationCompleteEvent(
            operatingDay = allocation.operatingDay,
            allocationCount = 1
        )
        jmsTemplate.convertAndSend("allocation.complete", event)
    }
}
```

**Consuming Events:**
```kotlin
package com.company.imbalance.infrastructure.messaging

import com.company.allocation.AllocationCompleteEvent
import com.company.imbalance.application.StartImbalanceCalculationUseCase
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
internal class AllocationEventConsumer(
    private val useCase: StartImbalanceCalculationUseCase
) {
    
    @JmsListener(destination = "allocation.complete")
    fun handleAllocationComplete(event: AllocationCompleteEvent) {
        useCase.execute(event.operatingDay)
    }
}
```

---

## 7. Testing Strategy

### 7.1 Testing Pyramid

```
        /\
       /  \      Cucumber (Use Case Tests)
      /────\     - End-to-end scenarios
     /      \    - Feature files
    /────────\   Spring Integration Tests
   /          \  - Repository tests
  /────────────\ - Controller tests
 /              \
/────────────────\ Unit Tests
                   - Domain logic
                   - Pure functions
                   - No Spring
```

### 7.2 Unit Tests (Domain Logic)

**No Spring, fast, isolated:**

```kotlin
package com.company.allocation.domain.model

import com.company.common.ean.Ean18
import com.company.common.time.DayNL
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class AllocationTest {
    
    @Test
    fun `create should create allocation in draft status`() {
        // Given
        val ean18 = Ean18("871687120050264654")
        val day = DayNL.now()
        val quantity = Quantity(100.0)
        
        // When
        val allocation = Allocation.create(ean18, day, quantity)
        
        // Then
        assertThat(allocation.status).isEqualTo(AllocationStatus.DRAFT)
        assertThat(allocation.id).isNull()
    }
    
    @Test
    fun `finalize should change status when draft`() {
        // Given
        val allocation = buildAllocation(status = AllocationStatus.DRAFT)
        
        // When
        val finalized = allocation.finalize()
        
        // Then
        assertThat(finalized.status).isEqualTo(AllocationStatus.FINALIZED)
    }
    
    @Test
    fun `finalize should throw when already finalized`() {
        // Given
        val allocation = buildAllocation(status = AllocationStatus.FINALIZED)
        
        // When & Then
        assertThatThrownBy { allocation.finalize() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Can only finalize draft allocations")
    }
}
```

### 7.3 Integration Tests (Infrastructure)

**With Spring, database:**

```kotlin
package com.company.allocation.infrastructure.persistence

import com.company.allocation.domain.model.*
import com.company.common.ean.Ean18
import com.company.common.time.DayNL
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@Transactional
class AllocationRepositoryImplTest {
    
    @Autowired
    private lateinit var repository: AllocationRepositoryImpl
    
    @Test
    fun `save should persist allocation and return with ID`() {
        // Given
        val allocation = buildAllocation(id = null)
        
        // When
        val saved = repository.save(allocation)
        
        // Then
        assertThat(saved.id).isNotNull()
        assertThat(saved.ean18).isEqualTo(allocation.ean18)
    }
    
    @Test
    fun `findByDay should return allocations for specific day`() {
        // Given
        val day = DayNL.now()
        val allocation1 = repository.save(buildAllocation(operatingDay = day))
        val allocation2 = repository.save(buildAllocation(operatingDay = day))
        
        // When
        val found = repository.findByDay(day)
        
        // Then
        assertThat(found).hasSize(2)
        assertThat(found).extracting("id").containsExactlyInAnyOrder(allocation1.id, allocation2.id)
    }
}
```

### 7.4 Use Case Tests (Cucumber)

**Feature file:**
```gherkin
Feature: UC01.1 - Create allocation
  As an operator
  I want to create allocations
  So that energy consumption is tracked

  Scenario: Create valid allocation
    Given an EAN18 "871687120050264654"
    And an operating day "2025-01-15"
    And a quantity of 150.5 kWh
    When I create the allocation
    Then the allocation should be saved
    And the allocation should have an ID
    And the allocation status should be "DRAFT"
```

**Step definitions:**
```kotlin
package com.company.allocation

import com.company.allocation.application.*
import com.company.allocation.domain.model.*
import com.company.allocation.domain.repository.AllocationRepository
import com.company.common.ean.Ean18
import com.company.common.time.DayNL
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class StepsAllocation(
    private val createAllocationUseCase: CreateAllocationUseCase,
    private val allocationRepository: AllocationRepository
) : En {
    
    private lateinit var ean18: Ean18
    private lateinit var operatingDay: DayNL
    private lateinit var quantity: Quantity
    private lateinit var createdId: AllocationId
    
    init {
        Given("an EAN18 {string}") { value: String ->
            ean18 = Ean18(value)
        }
        
        Given("an operating day {string}") { date: String ->
            operatingDay = DayNL(LocalDate.parse(date))
        }
        
        Given("a quantity of {double} kWh") { value: Double ->
            quantity = Quantity(value)
        }
        
        When("I create the allocation") {
            val command = CreateAllocationCommand(ean18, operatingDay, quantity)
            val result = createAllocationUseCase.execute(command)
            createdId = result.getOrThrow()
        }
        
        Then("the allocation should be saved") {
            val found = allocationRepository.findById(createdId)
            assertThat(found).isNotNull
        }
        
        Then("the allocation should have an ID") {
            assertThat(createdId.value).isGreaterThan(0)
        }
        
        Then("the allocation status should be {string}") { status: String ->
            val allocation = allocationRepository.findById(createdId)
            assertThat(allocation?.status).isEqualTo(AllocationStatus.valueOf(status))
        }
    }
}
```

---

## 8. Module Configuration

Each bounded context needs a Spring configuration:

**AllocationModule.kt:**
```kotlin
package com.company.allocation

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@ComponentScan(
    basePackages = [
        "com.company.allocation.application",
        "com.company.allocation.domain",
        "com.company.allocation.infrastructure"
    ]
)
@EnableJdbcRepositories(
    basePackages = ["com.company.allocation.infrastructure.persistence"]
)
class AllocationModule
```

---

## 9. Application Module

The application module is the deployable artifact:

**Application.kt:**
```kotlin
package com.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.company.allocation",
        "com.company.imbalance",
        "com.company.trading",
        "com.company.config"
    ]
)
@ConfigurationPropertiesScan
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
```

**build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
    alias(libs.plugins.spring.boot)
    application
}

dependencies {
    // Domain implementations
    implementation(project(":nma:allocation:allocation-impl"))
    implementation(project(":nma:imbalance:imbalance-impl"))
    implementation(project(":nma:trading:trading-impl"))
    
    // Common modules
    implementation("com.company.common:time")
    implementation("com.company.common:ean")
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")
    
    // Messaging
    implementation("jakarta.jms:jakarta.jms-api")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

---

## 10. Key Patterns Summary

### 10.1 DDD Tactical Patterns

- **Aggregates:** Transactional consistency boundary
- **Entities:** Have identity, mutable within aggregate
- **Value Objects:** Immutable, self-validating, no identity
- **Domain Services:** Logic spanning multiple aggregates
- **Repository:** One per aggregate root
- **Domain Events:** Communicate state changes

### 10.2 Strategic Patterns

- **Bounded Context:** Module with clear boundaries
- **Context Map:** Explicit relationships between contexts
- **Shared Kernel:** Common modules (use sparingly)
- **Anti-Corruption Layer:** Protect domain from external models

### 10.3 Dependency Guidelines

1. **Generic subdomains** ← Everyone can depend on these
2. **Supporting domains** can depend on generic + other supporting
3. **Core domains** should depend ONLY on generic
4. Use **dependency inversion** only when direction is wrong
5. Use **events** for loose coupling
6. Use **direct dependencies** as default when strategic design allows

---

## 11. Common Anti-Patterns to Avoid

### ❌ Anemic Domain Model
```kotlin
// DON'T: Logic outside domain objects
class AllocationService {
    fun finalize(allocation: Allocation) {
        if (allocation.status != AllocationStatus.DRAFT) {
            throw IllegalStateException("...")
        }
        allocation.status = AllocationStatus.FINALIZED
    }
}
```

### ✅ Rich Domain Model
```kotlin
// DO: Behavior in domain objects
data class Allocation {
    fun finalize(): Allocation {
        check(status == AllocationStatus.DRAFT) { "..." }
        return copy(status = AllocationStatus.FINALIZED)
    }
}
```

### ❌ Unnecessary Dependency Inversion
```kotlin
// DON'T: Invert when not needed
// If supporting domain can depend on another supporting domain, just do it!
implementation(project(":meter-data-api"))  // Direct is fine
```

### ❌ Primitive Obsession
```kotlin
// DON'T: Use primitives for domain concepts
data class Allocation(
    val ean18: String,  // Should be Ean18 value object
    val quantityKwh: Double  // Should be Quantity value object
)
```

### ❌ Repository per Table
```kotlin
// DON'T: Repository for every entity
interface AllocationRepository
interface AllocationLineRepository  // NO!
interface AllocationMetadataRepository  // NO!
```

### ✅ Repository per Aggregate
```kotlin
// DO: One repository for aggregate root
interface AllocationRepository {
    fun save(allocation: Allocation): Allocation  // Saves entire aggregate
}
```
