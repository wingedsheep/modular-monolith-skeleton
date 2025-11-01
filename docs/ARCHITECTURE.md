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
- Example: `order-fulfillment`, `carbon-accounting`

**Supporting Domains** (necessary but not differentiating)
- Support the core domain
- Can depend on generic domains
- Cannot be depended upon by core domain
- Example: `product-catalog`, `inventory-management`, `logistics`

**Generic Subdomains** (could be bought or reused)
- Highly reusable, domain-agnostic
- Can be depended upon by any domain
- Example: `common-country`, `common-identity`, `notifications`

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
ecoglobal-platform/
├── build-logic/                           # Gradle conventions
├── common/                                # Generic subdomains
│   ├── common-country/
│   ├── common-identity/
│   └── common-data-jdbc/
├── ecoglobal/                             # The deployable application
│   ├── application/                       # Application layer (Spring Boot main)
│   ├── order-fulfillment/                 # Bounded context (Core Domain)
│   │   ├── order-fulfillment-api/
│   │   └── order-fulfillment-impl/
│   ├── product-catalog/                   # Bounded context (Supporting Domain)
│   │   ├── product-catalog-api/
│   │   └── product-catalog-impl/
│   └── test/
│       └── use-case/                     # Cucumber tests
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### 2.2 Settings Configuration

**settings.gradle.kts:**
```kotlin
rootProject.name = "ecoglobal-platform"

includeBuild("build-logic")
includeBuild("common")

// EcoGlobal deployable with its domains
include(":ecoglobal:application")
include(":ecoglobal:order-fulfillment:order-fulfillment-api")
include(":ecoglobal:order-fulfillment:order-fulfillment-impl")
include(":ecoglobal:product-catalog:product-catalog-api")
include(":ecoglobal:product-catalog:product-catalog-impl")
include(":ecoglobal:test:use-case")
```

---

## 3. Generic Subdomain Structure

Generic subdomains are the foundation. They:
- Contain NO business logic
- Are domain-agnostic
- Can be depended upon by everyone
- Should be very stable

### 3.1 Example: common-country

**common/common-country/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
}
```

**CountryCode.kt (Value Object):**
```kotlin
package com.ecoglobal.common.country

/**
 * ISO 3166-1 alpha-2 country code.
 * Immutable value object with built-in validation.
 */
data class CountryCode(val value: String) {
    init {
        require(value.length == 2) { "Country code must be 2 characters" }
        require(value.all { it.isUpperCase() }) { "Country code must be upper case" }
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
product-catalog-impl/
├── application/
│   ├── AddProductUseCase.kt
│   └── GetProductDetailsUseCase.kt
├── domain/
│   ├── model/
│   │   ├── Product.kt
│   │   ├── ProductId.kt
│   │   └── Weight.kt
│   ├── service/
│   │   └── ProductValidator.kt
│   └── repository/
│       └── ProductRepository.kt
└── infrastructure/
    ├── persistence/
    │   ├── ProductEntity.kt
    │   ├── ProductRepositoryJdbc.kt
    │   └── ProductEntityMappers.kt
    ├── rest/
    │   └── v1/
    │       ├── ProductControllerV1.kt
    │       └── ProductDtos.kt
    └── messaging/
        └── ProductEventPublisher.kt
```

### 4.2 API Module (Public Interface)

The API module defines what other bounded contexts can use:

**product-catalog-api/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
}

dependencies {
    // ONLY common/generic dependencies allowed
    api("com.ecoglobal.common:country")
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
package com.ecoglobal.productcatalog

import com.ecoglobal.common.country.CountryCode

/**
 * Query interface for other domains to retrieve product data.
 */
interface ProductQueryService {
    fun findAvailableIn(countryCode: CountryCode): List<ProductSummary>
}

data class ProductSummary(
    val productId: ProductId,
    val name: String
)
```

**Domain Event:**
```kotlin
package com.ecoglobal.productcatalog

import com.ecoglobal.common.country.CountryCode

/**
 * Published when a product's details change.
 * Other domains can react to this event.
 */
data class ProductUpdatedEvent(
    val productId: ProductId,
    val availableInCountries: Set<CountryCode>
)
```

---

## 5. Implementation Module Structure

### 5.1 Build Configuration

**product-catalog-impl/build.gradle.kts:**
```kotlin
plugins {
    id("buildlogic.kotlin-conventions")
    id("buildlogic.cucumber-conventions")
}

dependencies {
    // Own API
    api(project(":ecoglobal:product-catalog:product-catalog-api"))
    
    // Direct dependencies on generic domains (NO dependency inversion)
    implementation("com.ecoglobal.common:country")
    
    // Direct dependencies on other domains IF strategic design allows
    // e.g., Logistics might depend on Product Catalog to get weights
    // implementation(project(":ecoglobal:logistics:logistics-api"))
    
    // Use dependency inversion ONLY if core domain needs supporting domain
    // In that case, define interface in core-api and implement in supporting-impl
    
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("jakarta.jms:jakarta.jms-api")
    
    // Testing
    testImplementation(project(":ecoglobal:application"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

### 5.2 Domain Layer (Core Business Logic)

#### 5.2.1 Aggregates

**Product.kt (Aggregate Root):**
```kotlin
package com.ecoglobal.productcatalog.domain.model

import com.ecoglobal.common.country.CountryCode

/**
 * Product aggregate root.
 * Represents a sellable item in the catalog.
 *
 * Aggregate invariants:
 * - Weight must be non-negative.
 * - Product must have a name.
 */
data class Product private constructor(
    val id: ProductId?,
    val name: String,
    val description: String,
    val weight: Weight,
    val status: ProductStatus,
    val availableIn: Set<CountryCode>
) {
    companion object {
        fun create(
            name: String,
            description: String,
            weight: Weight,
            availableIn: Set<CountryCode>
        ): Product {
            require(name.isNotBlank()) { "Product name cannot be blank" }
            require(weight.valueGrams >= 0) { "Weight cannot be negative" }
            
            return Product(
                id = null,
                name = name,
                description = description,
                weight = weight,
                status = ProductStatus.DRAFT,
                availableIn = availableIn
            )
        }
    }
    
    fun publish(): Product {
        check(status == ProductStatus.DRAFT) { "Can only publish draft products" }
        return copy(status = ProductStatus.PUBLISHED)
    }
    
    fun updateAvailability(newAvailability: Set<CountryCode>): Product {
        check(status != ProductStatus.DISCONTINUED) { "Cannot modify discontinued product" }
        return copy(availableIn = newAvailability)
    }
}
```

**ProductId.kt (Value Object):**
```kotlin
package com.ecoglobal.productcatalog.domain.model

import java.util.UUID

data class ProductId(val value: UUID) {
    companion object {
        fun generate() = ProductId(UUID.randomUUID())
    }
}
```

**Weight.kt (Value Object):**
```kotlin
package com.ecoglobal.productcatalog.domain.model

/**
 * Product weight in grams.
 * Immutable value object.
 */
data class Weight(val valueGrams: Int) {
    init {
        require(valueGrams >= 0) { "Weight must be non-negative" }
    }
    
    operator fun plus(other: Weight): Weight = Weight(valueGrams + other.valueGrams)
    
    companion object {
        val ZERO = Weight(0)
    }
}
```

**ProductStatus.kt:**
```kotlin
package com.ecoglobal.productcatalog.domain.model

enum class ProductStatus {
    DRAFT,
    PUBLISHED,
    DISCONTINUED
}
```

#### 5.2.2 Repository Interface (in domain layer)

```kotlin
package com.ecoglobal.productcatalog.domain.repository

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.common.country.CountryCode

/**
 * Repository for Product aggregate.
 * Interface defined in domain layer, implemented in infrastructure.
 */
interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: ProductId): Product?
    fun findAvailableIn(country: CountryCode): List<Product>
}
```

**Key Principle:** One repository per aggregate root. No repositories for entities inside aggregates.

#### 5.2.3 Domain Services

```kotlin
package com.ecoglobal.productcatalog.domain.service

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.common.country.CountryCode

/**
 * Domain service for product validation logic.
 * Contains domain logic that doesn't belong to a single aggregate.
 */
class ProductValidator(
    private val countryConfigProvider: CountryConfigProvider // From a generic domain
) {
    fun canBeSoldIn(product: Product, country: CountryCode): Boolean {
        if (!product.availableIn.contains(country)) {
            return false
        }

        // Example of a more complex rule
        val requiredCerts = countryConfigProvider.getRequiredCertifications(country)
        return product.certifications.containsAll(requiredCerts)
    }
}
```

### 5.3 Application Layer (Use Cases)

Application services orchestrate domain objects, handle transactions, and coordinate infrastructure.

**AddProductUseCase.kt:**
```kotlin
package com.ecoglobal.productcatalog.application

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.productcatalog.domain.repository.ProductRepository
import com.ecoglobal.productcatalog.infrastructure.messaging.ProductEventPublisher
import com.ecoglobal.common.country.CountryCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Use case for adding a new product to the catalog.
 */
@Service
class AddProductUseCase(
    private val productRepository: ProductRepository,
    private val eventPublisher: ProductEventPublisher
) {
    
    @Transactional
    fun execute(command: AddProductCommand): Result<ProductId> {
        return try {
            val product = Product.create(
                name = command.name,
                description = command.description,
                weight = command.weight,
                availableIn = command.availableIn
            )
            
            val saved = productRepository.save(product)
            val productId = requireNotNull(saved.id) { "Saved product must have ID" }
            
            eventPublisher.publishCreated(saved)
            
            Result.success(productId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class AddProductCommand(
    val name: String,
    val description: String,
    val weight: Weight,
    val availableIn: Set<CountryCode>
)
```

**GetProductDetailsUseCase.kt:**
```kotlin
package com.ecoglobal.productcatalog.application

import com.ecoglobal.productcatalog.domain.repository.ProductRepository
import com.ecoglobal.productcatalog.domain.model.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetProductDetailsUseCase(
    private val productRepository: ProductRepository
) {
    
    @Transactional(readOnly = true)
    fun execute(productId: ProductId): ProductDetailsResult? {
        return productRepository.findById(productId)?.let {
            ProductDetailsResult(
                id = it.id!!,
                name = it.name,
                status = it.status
            )
        }
    }
}

data class ProductDetailsResult(
    val id: ProductId,
    val name: String,
    val status: ProductStatus
)
```

### 5.4 Infrastructure Layer

#### 5.4.1 Persistence

**ProductEntity.kt:**
```kotlin
package com.ecoglobal.productcatalog.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "product")
internal data class ProductEntity(
    @Id val id: UUID?,
    val name: String,
    val description: String,
    val weightGrams: Int,
    val status: String,
    val availableIn: String // Stored as comma-separated string
)
```

**ProductEntityMappers.kt:**
```kotlin
package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.common.country.CountryCode

internal fun ProductEntity.toDomain(): Product = Product.create(
    name = this.name,
    description = this.description,
    weight = Weight(this.weightGrams),
    availableIn = this.availableIn.split(',').map { CountryCode(it) }.toSet()
).let {
    // Reconstitute with ID and status
    it.copy(
        id = this.id?.let { ProductId(it) },
        status = ProductStatus.valueOf(this.status)
    )
}

internal fun Product.toEntity(): ProductEntity = ProductEntity(
    id = this.id?.value,
    name = this.name,
    description = this.description,
    weightGrams = this.weight.valueGrams,
    status = this.status.name,
    availableIn = this.availableIn.joinToString(",") { it.value }
)
```

**ProductRepositoryImpl.kt:**
```kotlin
package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.productcatalog.domain.repository.ProductRepository
import com.ecoglobal.common.country.CountryCode
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
internal class ProductRepositoryImpl(
    private val jdbc: ProductRepositoryJdbc
) : ProductRepository {
    
    override fun save(product: Product): Product =
        jdbc.save(product.toEntity()).toDomain()
    
    override fun findById(id: ProductId): Product? =
        jdbc.findById(id.value).map { it.toDomain() }.orElse(null)
    
    override fun findAvailableIn(country: CountryCode): List<Product> =
        jdbc.findByAvailableInContaining(country.value).map { it.toDomain() }
}

internal interface ProductRepositoryJdbc : CrudRepository<ProductEntity, UUID> {
    fun findByAvailableInContaining(countryCode: String): List<ProductEntity>
}
```

#### 5.4.2 REST Controllers

**ProductControllerV1.kt:**
```kotlin
package com.ecoglobal.productcatalog.infrastructure.rest.v1

import com.ecoglobal.productcatalog.application.*
import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.common.country.CountryCode
import com.ecoglobal.common.security.RequiresRoleAdmin
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * REST controller for product catalog endpoints.
 * Belongs to infrastructure layer, delegates to use cases.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiresRoleAdmin
internal class ProductControllerV1(
    private val addProductUseCase: AddProductUseCase,
    private val getProductDetailsUseCase: GetProductDetailsUseCase
) {
    
    @PostMapping
    fun addProduct(@RequestBody request: AddProductRequestV1): ResponseEntity<ProductResponseV1> {
        val command = request.toCommand()
        
        return addProductUseCase.execute(command).fold(
            onSuccess = { productId ->
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ProductResponseV1(id = productId.value))
            },
            onFailure = { throw it }
        )
    }
    
    @GetMapping("/{id}")
    fun getProductDetails(@PathVariable id: UUID): ResponseEntity<ProductDetailsResponseV1> {
        val result = getProductDetailsUseCase.execute(ProductId(id))
        return result?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()
    }
}

data class AddProductRequestV1(
    val name: String,
    val description: String,
    val weightGrams: Int,
    val availableIn: Set<String>
)

data class ProductResponseV1(
    val id: UUID
)

data class ProductDetailsResponseV1(
    val id: UUID,
    val name: String,
    val status: String
)

// Mappers
private fun AddProductRequestV1.toCommand() = AddProductCommand(
    name = this.name,
    description = this.description,
    weight = Weight(this.weightGrams),
    availableIn = this.availableIn.map { CountryCode(it) }.toSet()
)

private fun ProductDetailsResult.toResponse() = ProductDetailsResponseV1(
    id = this.id.value,
    name = this.name,
    status = this.status.name
)
```

---

## 6. Cross-Bounded Context Integration

### 6.1 Default: Direct Dependencies

**When strategic design allows, use direct dependencies:**

```kotlin
// Tax Compliance (supporting) depending on Country Configuration (generic)
dependencies {
    implementation("com.ecoglobal.common:country")
}

// Tax Compliance (supporting) depending on Order Fulfillment (core)
// This is fine because the dependency points "inward" towards the core domain
dependencies {
    implementation(project(":ecoglobal:order-fulfillment:order-fulfillment-api"))
}
```

### 6.2 Dependency Inversion (Only When Needed)

**Use ONLY when dependency direction is wrong according to strategic design:**

**Scenario:** Order Fulfillment (core) needs inventory data from Inventory Management (supporting).

**Step 1: Define interface in CORE domain API:**
```kotlin
// In order-fulfillment-api
package com.ecoglobal.orderfulfillment.spi

import com.ecoglobal.productcatalog.domain.model.ProductId
import com.ecoglobal.inventory.domain.model.WarehouseId

/**
 * Service Provider Interface (SPI) defined by core domain.
 * Implementation will be in supporting domain.
 */
interface InventoryAvailabilityProvider {
    fun checkAvailability(productId: ProductId, warehouseId: WarehouseId): Int
}
```

**Step 2: Implement in SUPPORTING domain:**
```kotlin
// In inventory-impl
package com.ecoglobal.inventory.infrastructure

import com.ecoglobal.orderfulfillment.spi.InventoryAvailabilityProvider
import com.ecoglobal.inventory.application.CheckStockUseCase
import org.springframework.stereotype.Component

@Component
internal class InventoryAvailabilityProviderImpl(
    private val checkStockUseCase: CheckStockUseCase
) : InventoryAvailabilityProvider {
    
    override fun checkAvailability(productId: ProductId, warehouseId: WarehouseId): Int {
        return checkStockUseCase.execute(productId, warehouseId).getOrDefault(0)
    }
}
```

**Step 3: Core domain uses interface:**
```kotlin
// In order-fulfillment-impl
@Service
class FulfillmentOptimizer(
    private val inventoryProvider: InventoryAvailabilityProvider // Injected
) {
    fun findBestWarehouse(order: Order): WarehouseId {
        val availableWarehouses = order.items.map {
            inventoryProvider.checkAvailability(it.productId, WAREHOUSE_NL)
            // ... logic to find best warehouse
        }
        // ...
    }
}
```

**Build dependencies:**
```kotlin
// order-fulfillment-impl build.gradle.kts
dependencies {
    // Core domain's own API
    api(project(":ecoglobal:order-fulfillment:order-fulfillment-api"))
    
    // NO dependency on inventory-impl! Dependency is inverted.
}

// inventory-impl build.gradle.kts
dependencies {
    implementation(project(":ecoglobal:inventory:inventory-api"))
    
    // Depends on core domain API to implement the SPI
    implementation(project(":ecoglobal:order-fulfillment:order-fulfillment-api"))
}
```

### 6.3 Event-Driven Integration

Use for:
- Decoupling bounded contexts
- Eventual consistency scenarios
- Broadcasting state changes

**Publishing Events (in Order Fulfillment):**
```kotlin
package com.ecoglobal.orderfulfillment.infrastructure.messaging

import com.ecoglobal.orderfulfillment.domain.model.Order
import com.ecoglobal.orderfulfillment.api.events.OrderPlacedEvent
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventPublisher(
    private val jmsTemplate: JmsTemplate
) {
    fun publishOrderPlaced(order: Order) {
        val event = OrderPlacedEvent(
            orderId = order.id!!,
            customerId = order.customerId,
            // ... other details
        )
        jmsTemplate.convertAndSend("order.placed", event)
    }
}
```

**Consuming Events (in Inventory):**
```kotlin
package com.ecoglobal.inventory.infrastructure.messaging

import com.ecoglobal.orderfulfillment.api.events.OrderPlacedEvent
import com.ecoglobal.inventory.application.ReserveStockUseCase
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
internal class OrderEventConsumer(
    private val reserveStockUseCase: ReserveStockUseCase
) {
    
    @JmsListener(destination = "order.placed")
    fun handleOrderPlaced(event: OrderPlacedEvent) {
        reserveStockUseCase.execute(event.orderId, event.items)
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
package com.ecoglobal.productcatalog.domain.model

import com.ecoglobal.common.country.CountryCode
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductTest {
    
    @Test
    fun `create should create product in draft status`() {
        // When
        val product = Product.create(
            name = "Reusable Water Bottle",
            description = "A stainless steel water bottle.",
            weight = Weight(200),
            availableIn = setOf(CountryCode("NL"))
        )
        
        // Then
        assertThat(product.status).isEqualTo(ProductStatus.DRAFT)
        assertThat(product.id).isNull()
    }
    
    @Test
    fun `publish should change status when draft`() {
        // Given
        val product = buildProduct(status = ProductStatus.DRAFT)
        
        // When
        val published = product.publish()
        
        // Then
        assertThat(published.status).isEqualTo(ProductStatus.PUBLISHED)
    }
    
    @Test
    fun `publish should throw when not draft`() {
        // Given
        val product = buildProduct(status = ProductStatus.PUBLISHED)
        
        // When & Then
        assertThatThrownBy { product.publish() }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Can only publish draft products")
    }
}
```

### 7.3 Integration Tests (Infrastructure)

**With Spring, database:**

```kotlin
package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.common.country.CountryCode
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureEmbeddedDatabase
@Transactional
class ProductRepositoryImplTest {
    
    @Autowired
    private lateinit var repository: ProductRepositoryImpl
    
    @Test
    fun `save should persist product and return with ID`() {
        // Given
        val product = buildProduct(id = null)
        
        // When
        val saved = repository.save(product)
        
        // Then
        assertThat(saved.id).isNotNull()
        assertThat(saved.name).isEqualTo(product.name)
    }
    
    @Test
    fun `findAvailableIn should return products for specific country`() {
        // Given
        val countryNL = CountryCode("NL")
        val product1 = repository.save(buildProduct(availableIn = setOf(countryNL)))
        val product2 = repository.save(buildProduct(availableIn = setOf(countryNL, CountryCode("DE"))))
        
        // When
        val found = repository.findAvailableIn(countryNL)
        
        // Then
        assertThat(found).hasSize(2)
        assertThat(found).extracting("id").containsExactlyInAnyOrder(product1.id, product2.id)
    }
}
```

### 7.4 Use Case Tests (Cucumber)

**Feature file:**
```gherkin
Feature: UC01 - Add product to catalog
  As an administrator
  I want to add new products
  So they can be sold on the platform

  Scenario: Add a valid product
    Given a new product named "Solar-Powered Charger"
    And a weight of 250 grams
    And it is available in "NL" and "DE"
    When I add the product to the catalog
    Then the product should be saved
    And the product status should be "DRAFT"
```

**Step definitions:**
```kotlin
package com.ecoglobal.productcatalog

import com.ecoglobal.productcatalog.application.*
import com.ecoglobal.productcatalog.domain.model.*
import com.ecoglobal.productcatalog.domain.repository.ProductRepository
import com.ecoglobal.common.country.CountryCode
import io.cucumber.java8.En
import org.assertj.core.api.Assertions.*
import org.springframework.transaction.annotation.Transactional

@Transactional
class StepsProductCatalog(
    private val addProductUseCase: AddProductUseCase,
    private val productRepository: ProductRepository
) : En {
    
    private lateinit var name: String
    private lateinit var weight: Weight
    private val availableIn = mutableSetOf<CountryCode>()
    private lateinit var createdId: ProductId
    
    init {
        Given("a new product named {string}") { value: String ->
            name = value
        }
        
        Given("a weight of {int} grams") { value: Int ->
            weight = Weight(value)
        }
        
        Given("it is available in {string} and {string}") { cc1: String, cc2: String ->
            availableIn.add(CountryCode(cc1))
            availableIn.add(CountryCode(cc2))
        }
        
        When("I add the product to the catalog") {
            val command = AddProductCommand(name, "description", weight, availableIn)
            createdId = addProductUseCase.execute(command).getOrThrow()
        }
        
        Then("the product should be saved") {
            assertThat(productRepository.findById(createdId)).isNotNull
        }
        
        Then("the product status should be {string}") { status: String ->
            val product = productRepository.findById(createdId)
            assertThat(product?.status).isEqualTo(ProductStatus.valueOf(status))
        }
    }
}
```

---

## 8. Module Configuration

Each bounded context needs a Spring configuration:

**ProductCatalogModule.kt:**
```kotlin
package com.ecoglobal.productcatalog

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@ComponentScan(
    basePackages = [
        "com.ecoglobal.productcatalog.application",
        "com.ecoglobal.productcatalog.domain",
        "com.ecoglobal.productcatalog.infrastructure"
    ]
)
@EnableJdbcRepositories(
    basePackages = ["com.ecoglobal.productcatalog.infrastructure.persistence"]
)
class ProductCatalogModule
```

---

## 9. Application Module

The application module is the deployable artifact:

**Application.kt:**
```kotlin
package com.ecoglobal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.ecoglobal.orderfulfillment",
        "com.ecoglobal.productcatalog",
        "com.ecoglobal.config"
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
    implementation(project(":ecoglobal:order-fulfillment:order-fulfillment-impl"))
    implementation(project(":ecoglobal:product-catalog:product-catalog-impl"))
    implementation(project(":ecoglobal:inventory:inventory-impl"))
    
    // Common modules
    implementation("com.ecoglobal.common:country")
    
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // Database
    implementation("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    
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
class ProductService {
    fun publish(product: Product) {
        if (product.status != ProductStatus.DRAFT) {
            throw IllegalStateException("...")
        }
        product.status = ProductStatus.PUBLISHED
    }
}
```

### ✅ Rich Domain Model
```kotlin
// DO: Behavior in domain objects
data class Product {
    fun publish(): Product {
        check(status == ProductStatus.DRAFT) { "..." }
        return copy(status = ProductStatus.PUBLISHED)
    }
}
```

### ❌ Unnecessary Dependency Inversion
```kotlin
// DON'T: Invert when not needed
// If logistics (supporting) can depend on product-catalog (supporting), just do it!
implementation(project(":ecoglobal:product-catalog:product-catalog-api"))
```

### ❌ Primitive Obsession
```kotlin
// DON'T: Use primitives for domain concepts
data class Product(
    val name: String,
    val weightGrams: Int, // Should be Weight value object
    val countryCode: String // Should be CountryCode value object
)
```

### ❌ Repository per Table
```kotlin
// DON'T: Repository for every entity
interface ProductRepository
interface ProductCertificationRepository // NO!
interface ProductCountryAvailabilityRepository // NO!
```

### ✅ Repository per Aggregate
```kotlin
// DO: One repository for aggregate root
interface ProductRepository {
    fun save(product: Product): Product // Saves entire aggregate
}
```
