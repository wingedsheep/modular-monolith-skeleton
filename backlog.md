# EcoGlobal Implementation Backlog

Complete checklist for implementing the EcoGlobal sustainable products platform.

---

## Epic 0: Project Skeleton & Infrastructure Setup

### 0.0 AGENTS.md

- [x] Create comprehensive AGENTS.md for use by AI agents, with main principles. Don't include anything about the domain. Just the architecture.

### 0.1 Repository Structure
- [x] Create root project structure with `build-logic`, `common`, `ecoglobal` folders
- [x] Set up `build-logic` folder with convention plugins
- [ ] Create `buildlogic.kotlin-conventions.gradle.kts`
- [ ] Create `buildlogic.cucumber-conventions.gradle.kts`
- [ ] Create `buildlogic.jacoco-conventions.gradle.kts`
- [ ] Create `buildlogic.errorprone.gradle.kts`
- [ ] Create `buildlogic.project-root.gradle.kts`
- [ ] Create `libs.versions.toml` with all dependencies
- [x] Create root `build.gradle.kts`
- [x] Create root `settings.gradle.kts` (initially empty, will add modules as we go)
- [x] Create `gradle.properties` with configuration
- [x] Create `.gitignore`

### 0.2 Docker Infrastructure
- [x] Create `docker/compose.yaml` with PostgreSQL
- [x] Create `docker/compose.yaml` with ActiveMQ
- [x] Create `docker/sql/init.sql` for database initialization
- [x] Create `.env.example` for environment variables
- [x] Test: `docker compose up` starts successfully

### 0.3 Application Module
- [x] Create `ecoglobal/application` module structure
- [x] Create `build.gradle.kts` for application module
- [x] Create `Application.kt` with Spring Boot main class
- [x] Create `SecurityConfig.kt` (basic setup, no authentication yet)
- [x] Create `application.yaml` with basic configuration
- [x] Create `application-test.yaml` for test profile
- [x] Test: Application starts successfully

### 0.4 Test Infrastructure
- [ ] Create `ecoglobal/test/use-case` module structure
- [ ] Create `build.gradle.kts` for use-case tests
- [ ] Create `CucumberSpringBootTest.kt`
- [ ] Create `CucumberTestRunner.kt`
- [ ] Create `SpringTransactionHooks.kt`
- [ ] Create `src/test/resources/features` folder structure
- [ ] Test: Cucumber runner executes (with no features yet)

### 0.5 Documentation
- [ ] Create `README.md` with project overview
- [ ] Create `docs/ARCHITECTURE.md` describing bounded contexts
- [ ] Create `docs/GETTING_STARTED.md` with setup instructions
- [ ] Create `docs/STRATEGIC_DESIGN.md` with domain classification
- [ ] Create `Justfile` with common commands

---

## Epic 1: Common Modules (Generic Subdomains)

### 1.1 common-country Module
- [ ] Create `common/common-country` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `CountryCode.kt` enum (NL, DE, FR)
- [ ] Create `Currency.kt` enum (EUR)
- [ ] Create `CountryRules.kt` data class
- [ ] Create `CountryConfigProvider.kt` interface
- [ ] Create `CountryConfigProviderImpl.kt` with hardcoded rules
- [ ] Unit test: `CountryCodeTest.kt`
- [ ] Unit test: `CountryConfigProviderImplTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.2 common-money Module
- [ ] Create `common/common-money` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Money.kt` value object with validation
- [ ] Create `Money.kt` arithmetic operators (+, -, *, /)
- [ ] Create `Money.ZERO` constant
- [ ] Unit test: `MoneyTest.kt` covering all operations
- [ ] Unit test: Validate currency matching in operations
- [ ] Add to root `settings.gradle.kts`

### 1.3 common-measurement Module
- [ ] Create `common/common-measurement` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Weight.kt` value object (in kg)
- [ ] Create `Distance.kt` value object (in km)
- [ ] Create `Dimensions.kt` value object (length, width, height in cm)
- [ ] Create `CarbonAmount.kt` value object (in kg CO2)
- [ ] Create `Volume.kt` calculated from Dimensions
- [ ] Unit test: `WeightTest.kt`
- [ ] Unit test: `DimensionsTest.kt`
- [ ] Unit test: `CarbonAmountTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.4 common-address Module
- [ ] Create `common/common-address` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Address.kt` value object (street, city, postal code, country)
- [ ] Create `PostalCode.kt` value object with country-specific validation
- [ ] Create validation for NL postal codes (1234 AB format)
- [ ] Create validation for DE postal codes (5 digits)
- [ ] Create validation for FR postal codes (5 digits)
- [ ] Unit test: `AddressTest.kt`
- [ ] Unit test: `PostalCodeTest.kt` with country-specific validation
- [ ] Add to root `settings.gradle.kts`

### 1.5 common-time Module
- [ ] Create `common/common-time` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Period.kt` value object (start date, end date)
- [ ] Create `Period.kt` overlap detection
- [ ] Create `Period.kt` contains date check
- [ ] Create `Period.kt` duration calculation
- [ ] Unit test: `PeriodTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.6 common-data-jdbc Module
- [ ] Create `common/common-data-jdbc` module structure
- [ ] Create `build.gradle.kts` with Spring Data JDBC dependency
- [ ] Create base repository utilities if needed
- [ ] Create common database converters (for value objects)
- [ ] Create `MoneyConverter.kt` for JDBC
- [ ] Create `WeightConverter.kt` for JDBC
- [ ] Create `CarbonAmountConverter.kt` for JDBC
- [ ] Add to root `settings.gradle.kts`

---

## Epic 2: Product Catalog (Supporting Domain)

### 2.1 Module Setup
- [ ] Create `ecoglobal/product-catalog/product-catalog-api` structure
- [ ] Create `ecoglobal/product-catalog/product-catalog-impl` structure
- [ ] Create `ecoglobal/product-catalog/product-catalog-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `ProductCatalogModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 2.2 Domain Model (in -impl)
- [ ] Create `domain/model/Product.kt` aggregate root
- [ ] Create `domain/model/ProductId.kt` value object
- [ ] Create `domain/model/ProductName.kt` value object
- [ ] Create `domain/model/SustainabilityRating.kt` enum (A_PLUS to F)
- [ ] Create `domain/model/Certification.kt` enum (EU_ECOLABEL, FAIR_TRADE, etc.)
- [ ] Create `domain/model/PackagingType.kt` enum (RECYCLABLE_CARDBOARD, etc.)
- [ ] Create `domain/model/ProductCategory.kt` enum (SOLAR_PANELS, CONTAINERS, TEXTILES)
- [ ] Create factory method `Product.create()` with validation
- [ ] Unit test: `ProductTest.kt` for invariants and business rules

### 2.3 Repository (in -impl)
- [ ] Create `domain/repository/ProductRepository.kt` interface
- [ ] Create `infrastructure/persistence/ProductEntity.kt`
- [ ] Create `infrastructure/persistence/ProductEntityMappers.kt`
- [ ] Create `infrastructure/persistence/ProductRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/ProductRepositoryJdbc.kt` interface

### 2.4 Use Cases (in -impl)
- [ ] Create `application/CreateProductUseCase.kt`
- [ ] Create `application/CreateProductCommand.kt`
- [ ] Create `application/FindProductsUseCase.kt`
- [ ] Create `application/UpdateProductAvailabilityUseCase.kt`
- [ ] Create `application/UpdateProductAvailabilityCommand.kt`

### 2.5 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/ProductControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductResponseV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductDtoMappers.kt`
- [ ] Create `infrastructure/rest/internal/ProductInternalController.kt` for operators

### 2.6 Database (in application)
- [ ] Create migration `V001__create_product_catalog_schema.sql`
- [ ] Create `product` table with all fields
- [ ] Create `product_certifications` junction table
- [ ] Create `product_country_availability` table
- [ ] Create indexes on `product(category)`, `product(sustainability_rating)`

### 2.7 Events (in -api)
- [ ] Create `ProductCreatedEvent.kt` data class
- [ ] Create `ProductAvailabilityChangedEvent.kt` data class
- [ ] Create `infrastructure/messaging/ProductEventPublisher.kt` (in -impl)

### 2.8 Worldview (in -worldview)
- [ ] Create `WorldviewProduct.kt` object with realistic products
- [ ] Create 5 solar panel products (different wattages, brands)
- [ ] Create 5 reusable container products (different sizes, materials)
- [ ] Create 5 organic textile products (shirts, bags, towels)
- [ ] Create `ProductBuilder.kt` for tests
- [ ] Create `insertWorldviewProducts()` function
- [ ] Integration test: Verify worldview products can be inserted

### 2.9 Testing
- [ ] Unit test: `CreateProductUseCaseTest.kt`
- [ ] Unit test: `ProductEntityMappersTest.kt`
- [ ] Integration test: `ProductRepositoryImplTest.kt`
- [ ] Integration test: `ProductControllerV1Test.kt`
- [ ] Feature file: `features/02-product-catalog/01-create-product.feature`
- [ ] Feature file: `features/02-product-catalog/02-list-products-by-category.feature`
- [ ] Feature file: `features/02-product-catalog/03-update-availability.feature`
- [ ] Create `StepsProduct.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

---

## Epic 3: Inventory Management (Supporting Domain)

### 3.1 Module Setup
- [ ] Create `ecoglobal/inventory/inventory-api` structure
- [ ] Create `ecoglobal/inventory/inventory-impl` structure
- [ ] Create `ecoglobal/inventory/inventory-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `InventoryModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 3.2 Domain Model (in -impl)
- [ ] Create `domain/model/InventoryLevel.kt` aggregate root
- [ ] Create `domain/model/WarehouseId.kt` value object (NL_WAREHOUSE, DE_WAREHOUSE, FR_WAREHOUSE)
- [ ] Create `domain/model/StockQuantity.kt` value object
- [ ] Create `domain/model/Reservation.kt` entity
- [ ] Create `domain/model/ReservationId.kt` value object
- [ ] Create `InventoryLevel.reserve()` method with validation
- [ ] Create `InventoryLevel.commitReservation()` method
- [ ] Create `InventoryLevel.releaseReservation()` method
- [ ] Create `InventoryLevel.adjustStock()` method
- [ ] Unit test: `InventoryLevelTest.kt` for all business rules

### 3.3 Repository (in -impl)
- [ ] Create `domain/repository/InventoryRepository.kt` interface
- [ ] Create `infrastructure/persistence/InventoryEntity.kt`
- [ ] Create `infrastructure/persistence/ReservationEntity.kt`
- [ ] Create `infrastructure/persistence/InventoryEntityMappers.kt`
- [ ] Create `infrastructure/persistence/InventoryRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/InventoryRepositoryJdbc.kt` interface

### 3.4 Use Cases (in -impl)
- [ ] Create `application/ReserveStockUseCase.kt`
- [ ] Create `application/ReserveStockCommand.kt`
- [ ] Create `application/CommitReservationUseCase.kt`
- [ ] Create `application/ReleaseReservationUseCase.kt`
- [ ] Create `application/AdjustStockUseCase.kt`
- [ ] Create `application/CheckAvailabilityUseCase.kt`
- [ ] Create `application/CheckAvailabilityQuery.kt`

### 3.5 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/InventoryInternalController.kt`
- [ ] Create `infrastructure/rest/internal/InventoryDto.kt`
- [ ] Create `infrastructure/rest/internal/StockAdjustmentRequest.kt`

### 3.6 Database (in application)
- [ ] Create migration `V002__create_inventory_schema.sql`
- [ ] Create `inventory_level` table
- [ ] Create `reservation` table
- [ ] Create indexes on `inventory_level(product_id, warehouse_id)`
- [ ] Create unique constraint on `inventory_level(product_id, warehouse_id)`

### 3.7 Provider Interface Implementation (in -impl)
- [ ] Create interface `InventoryAvailabilityProvider.kt` in order-fulfillment-api (will do in Epic 4)
- [ ] Create `infrastructure/provider/InventoryAvailabilityProviderImpl.kt` implementing the interface
- [ ] Integration test: Provider returns correct availability

### 3.8 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to reserve stock
- [ ] Implement `onOrderCancelled()` to release reservation
- [ ] Implement `onOrderShipped()` to commit reservation

### 3.9 Worldview (in -worldview)
- [ ] Create `WorldviewInventory.kt` with initial stock levels
- [ ] Create realistic stock for NL warehouse (100-500 units per product)
- [ ] Create realistic stock for DE warehouse (50-300 units per product)
- [ ] Create realistic stock for FR warehouse (50-300 units per product)
- [ ] Create `InventoryBuilder.kt` for tests
- [ ] Create `insertWorldviewInventory()` function
- [ ] Integration test: Verify worldview inventory can be inserted

### 3.10 Testing
- [ ] Unit test: `ReserveStockUseCaseTest.kt`
- [ ] Unit test: `InventoryEntityMappersTest.kt`
- [ ] Integration test: `InventoryRepositoryImplTest.kt`
- [ ] Integration test: `InventoryAvailabilityProviderImplTest.kt`
- [ ] Feature file: `features/03-inventory/01-reserve-stock.feature`
- [ ] Feature file: `features/03-inventory/02-commit-reservation.feature`
- [ ] Feature file: `features/03-inventory/03-release-reservation.feature`
- [ ] Feature file: `features/03-inventory/04-adjust-stock.feature`
- [ ] Create `StepsInventory.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

---

## Epic 4: Order Fulfillment (Core Domain)

### 4.1 Module Setup
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-api` structure
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-impl` structure
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-worldview` structure
- [ ] Create `build.gradle.kts` for each module (NO dependencies on supporting domains in -impl!)
- [ ] Create `OrderFulfillmentModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 4.2 Provider Interfaces (in -api)
- [ ] Create `InventoryAvailabilityProvider.kt` interface
- [ ] Create `ShippingCostProvider.kt` interface
- [ ] Create `CarbonDataProvider.kt` interface
- [ ] Create DTOs for these providers

### 4.3 Domain Model (in -impl)
- [ ] Create `domain/model/Order.kt` aggregate root
- [ ] Create `domain/model/OrderId.kt` value object
- [ ] Create `domain/model/CustomerId.kt` value object
- [ ] Create `domain/model/OrderLine.kt` entity
- [ ] Create `domain/model/OrderStatus.kt` enum (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- [ ] Create `domain/model/FulfillmentStrategy.kt` enum (FASTEST, CHEAPEST, LOWEST_CARBON)
- [ ] Create `domain/model/FulfillmentDecision.kt` value object
- [ ] Create `Order.create()` factory method
- [ ] Create `Order.confirm()` method (after payment)
- [ ] Create `Order.cancel()` method
- [ ] Create `Order.markShipped()` method
- [ ] Unit test: `OrderTest.kt` for all business rules and state transitions

### 4.4 Domain Service (in -impl)
- [ ] Create `domain/service/FulfillmentOptimizer.kt` domain service
- [ ] Inject the three provider interfaces via constructor
- [ ] Create `optimize()` method that takes order and strategy
- [ ] Implement FASTEST strategy (choose warehouse with stock and shortest distance)
- [ ] Implement CHEAPEST strategy (calculate total cost per warehouse)
- [ ] Implement LOWEST_CARBON strategy (calculate carbon per warehouse)
- [ ] Unit test: `FulfillmentOptimizerTest.kt` with mocked providers

### 4.5 Repository (in -impl)
- [ ] Create `domain/repository/OrderRepository.kt` interface
- [ ] Create `infrastructure/persistence/OrderEntity.kt`
- [ ] Create `infrastructure/persistence/OrderLineEntity.kt`
- [ ] Create `infrastructure/persistence/OrderEntityMappers.kt`
- [ ] Create `infrastructure/persistence/OrderRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/OrderRepositoryJdbc.kt` interface

### 4.6 Use Cases (in -impl)
- [ ] Create `application/PlaceOrderUseCase.kt`
- [ ] Create `application/PlaceOrderCommand.kt`
- [ ] Create `application/ConfirmOrderUseCase.kt` (after payment)
- [ ] Create `application/CancelOrderUseCase.kt`
- [ ] Create `application/FindOrderUseCase.kt`
- [ ] Create `application/ListOrdersUseCase.kt`

### 4.7 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/OrderControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/PlaceOrderRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderResponseV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderLineRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderDtoMappers.kt`
- [ ] Create `infrastructure/rest/internal/OrderInternalController.kt`

### 4.8 Database (in application)
- [ ] Create migration `V003__create_order_fulfillment_schema.sql`
- [ ] Create `customer_order` table (order is reserved keyword)
- [ ] Create `order_line` table with foreign key to customer_order
- [ ] Create indexes on `customer_order(customer_id)`, `customer_order(status)`
- [ ] Create index on `order_line(order_id)`

### 4.9 Events (in -api)
- [ ] Create `OrderPlacedEvent.kt` data class
- [ ] Create `OrderConfirmedEvent.kt` data class
- [ ] Create `OrderCancelledEvent.kt` data class
- [ ] Create `OrderShippedEvent.kt` data class
- [ ] Create `infrastructure/messaging/OrderEventPublisher.kt` (in -impl)

### 4.10 Worldview (in -worldview)
- [ ] Create `WorldviewCustomer.kt` with 10 realistic customers
- [ ] Include addresses in NL, DE, FR (at least 3 per country)
- [ ] Create `WorldviewOrder.kt` with 5 realistic order scenarios
- [ ] Scenario 1: Small order, single product, NL customer
- [ ] Scenario 2: Large order, multiple products, DE customer
- [ ] Scenario 3: Mixed products from different categories, FR customer
- [ ] Scenario 4: Bulk order requesting LOWEST_CARBON strategy
- [ ] Scenario 5: Express order requesting FASTEST strategy
- [ ] Create `OrderBuilder.kt` for tests
- [ ] Create `insertWorldviewOrders()` function

### 4.11 Testing
- [ ] Unit test: `PlaceOrderUseCaseTest.kt` with mocked providers
- [ ] Unit test: `FulfillmentOptimizerTest.kt` testing all strategies
- [ ] Unit test: `OrderEntityMappersTest.kt`
- [ ] Integration test: `OrderRepositoryImplTest.kt`
- [ ] Integration test: `OrderControllerV1Test.kt`
- [ ] Feature file: `features/04-order-fulfillment/01-place-order-fastest.feature`
- [ ] Feature file: `features/04-order-fulfillment/02-place-order-cheapest.feature`
- [ ] Feature file: `features/04-order-fulfillment/03-place-order-lowest-carbon.feature`
- [ ] Feature file: `features/04-order-fulfillment/04-confirm-order.feature`
- [ ] Feature file: `features/04-order-fulfillment/05-cancel-order.feature`
- [ ] Create `StepsOrder.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

---

## Epic 5: Logistics & Shipping (Supporting Domain)

### 5.1 Module Setup
- [ ] Create `ecoglobal/logistics/logistics-api` structure
- [ ] Create `ecoglobal/logistics/logistics-impl` structure
- [ ] Create `ecoglobal/logistics/logistics-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `LogisticsModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 5.2 Domain Model (in -impl)
- [ ] Create `domain/model/Shipment.kt` aggregate root
- [ ] Create `domain/model/ShipmentId.kt` value object
- [ ] Create `domain/model/Carrier.kt` enum (POSTNL, DHL, DPD, CHRONOPOST)
- [ ] Create `domain/model/TransportMode.kt` enum (VAN, TRUCK, PARCEL_SERVICE)
- [ ] Create `domain/model/TrackingNumber.kt` value object
- [ ] Create `domain/model/ShipmentStatus.kt` enum (CREATED, IN_TRANSIT, DELIVERED)
- [ ] Create `Shipment.create()` factory method
- [ ] Create `Shipment.markInTransit()` method
- [ ] Create `Shipment.markDelivered()` method with actual carbon
- [ ] Unit test: `ShipmentTest.kt` for business rules

### 5.3 Domain Services (in -impl)
- [ ] Create `domain/service/ShippingCostCalculator.kt` domain service
- [ ] Implement cost calculation per carrier and distance
- [ ] Implement country-specific pricing rules
- [ ] Create `domain/service/CarbonCalculator.kt` domain service
- [ ] Implement carbon calculation: distance * weight * carbon-per-km
- [ ] Define carbon-per-km constants for each carrier/transport mode combination
- [ ] Unit test: `ShippingCostCalculatorTest.kt`
- [ ] Unit test: `CarbonCalculatorTest.kt`

### 5.4 Repository (in -impl)
- [ ] Create `domain/repository/ShipmentRepository.kt` interface
- [ ] Create `infrastructure/persistence/ShipmentEntity.kt`
- [ ] Create `infrastructure/persistence/ShipmentEntityMappers.kt`
- [ ] Create `infrastructure/persistence/ShipmentRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/ShipmentRepositoryJdbc.kt` interface

### 5.5 Use Cases (in -impl)
- [ ] Create `application/CreateShipmentUseCase.kt`
- [ ] Create `application/CreateShipmentCommand.kt`
- [ ] Create `application/UpdateTrackingUseCase.kt`
- [ ] Create `application/MarkDeliveredUseCase.kt`
- [ ] Create `application/CalculateShippingCostUseCase.kt`

### 5.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/ShipmentInternalController.kt`
- [ ] Create `infrastructure/rest/internal/ShipmentDto.kt`
- [ ] Create `infrastructure/rest/internal/CreateShipmentRequest.kt`

### 5.7 Database (in application)
- [ ] Create migration `V004__create_logistics_schema.sql`
- [ ] Create `shipment` table
- [ ] Create indexes on `shipment(order_id)`, `shipment(status)`

### 5.8 Provider Interface Implementations (in -impl)
- [ ] Create `infrastructure/provider/ShippingCostProviderImpl.kt`
- [ ] Create `infrastructure/provider/CarbonDataProviderImpl.kt`
- [ ] Integration test: Providers return correct calculations

### 5.9 Event Consumers & Publishers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderConfirmed()` to create shipment
- [ ] Create `infrastructure/messaging/ShipmentEventPublisher.kt`
- [ ] Publish `ShipmentCreatedEvent` after creating shipment
- [ ] Publish `ShipmentDeliveredEvent` after delivery

### 5.10 Events (in -api)
- [ ] Create `ShipmentCreatedEvent.kt` data class
- [ ] Create `ShipmentDeliveredEvent.kt` data class

### 5.11 Worldview (in -worldview)
- [ ] Create `WorldviewCarrier.kt` with carrier data
- [ ] Define shipping costs per carrier and country
- [ ] Define carbon-per-km per carrier and transport mode
- [ ] Create realistic distance calculations between warehouses and cities
- [ ] Create `ShipmentBuilder.kt` for tests
- [ ] Create `insertWorldviewShipmentData()` function

### 5.12 Testing
- [ ] Unit test: `CreateShipmentUseCaseTest.kt`
- [ ] Unit test: `ShippingCostCalculatorTest.kt` with various scenarios
- [ ] Unit test: `CarbonCalculatorTest.kt` with various scenarios
- [ ] Unit test: `ShipmentEntityMappersTest.kt`
- [ ] Integration test: `ShipmentRepositoryImplTest.kt`
- [ ] Integration test: `ShippingCostProviderImplTest.kt`
- [ ] Integration test: `CarbonDataProviderImplTest.kt`
- [ ] Feature file: `features/05-logistics/01-create-shipment.feature`
- [ ] Feature file: `features/05-logistics/02-calculate-shipping-cost.feature`
- [ ] Feature file: `features/05-logistics/03-calculate-carbon-emissions.feature`
- [ ] Feature file: `features/05-logistics/04-mark-delivered.feature`
- [ ] Create `StepsShipment.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

---

## Epic 6: Carbon Accounting (Core Domain)

### 6.1 Module Setup
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-api` structure
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-impl` structure
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-worldview` structure
- [ ] Create `build.gradle.kts` for each module (NO dependencies on supporting!)
- [ ] Create `CarbonAccountingModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 6.2 Provider Interfaces (in -api)
- [ ] Create `ShippingDetailsProvider.kt` interface
- [ ] Create DTOs for shipping details

### 6.3 Domain Model (in -impl)
- [ ] Create `domain/model/CarbonFootprint.kt` aggregate root
- [ ] Create `domain/model/CarbonFootprintId.kt` value object
- [ ] Create `domain/model/EmissionsBreakdown.kt` value object
- [ ] Create `domain/model/ShippingEmissions.kt` component
- [ ] Create `domain/model/PackagingEmissions.kt` component
- [ ] Create `CarbonFootprint.create()` factory method
- [ ] Create `CarbonFootprint.updateWithActualShipping()` method
- [ ] Create `CarbonFootprint.calculateTotal()` method
- [ ] Unit test: `CarbonFootprintTest.kt`

### 6.4 Domain Services (in -impl)
- [ ] Create `domain/service/CarbonFootprintCalculator.kt` domain service
- [ ] Implement `calculateShippingEmissions()` method
- [ ] Implement `calculatePackagingEmissions()` method (based on packaging type)
- [ ] Unit test: `CarbonFootprintCalculatorTest.kt`

### 6.5 Repository (in -impl)
- [ ] Create `domain/repository/CarbonFootprintRepository.kt` interface
- [ ] Create `infrastructure/persistence/CarbonFootprintEntity.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintEntityMappers.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintRepositoryJdbc.kt` interface

### 6.6 Use Cases (in -impl)
- [ ] Create `application/CalculateOrderCarbonUseCase.kt`
- [ ] Create `application/CalculateOrderCarbonCommand.kt`
- [ ] Create `application/UpdateWithActualEmissionsUseCase.kt`
- [ ] Create `application/GetCarbonFootprintUseCase.kt`
- [ ] Create `application/GeneratePeriodReportUseCase.kt`

### 6.7 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/CarbonFootprintControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/CarbonFootprintResponseV1.kt`
- [ ] Create endpoint to get carbon footprint by order ID
- [ ] Create `infrastructure/rest/internal/CarbonReportController.kt`
- [ ] Create endpoint to generate period reports

### 6.8 Database (in application)
- [ ] Create migration `V005__create_carbon_accounting_schema.sql`
- [ ] Create `carbon_footprint` table
- [ ] Create indexes on `carbon_footprint(order_id)`

### 6.9 Provider Implementation (in logistics-impl)
- [ ] Create `infrastructure/provider/ShippingDetailsProviderImpl.kt` in logistics-impl
- [ ] Implement interface defined in carbon-accounting-api

### 6.10 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to calculate estimated carbon
- [ ] Implement `onOrderShipped()` to update with actual shipping method
- [ ] Create `infrastructure/messaging/ShipmentEventConsumer.kt`
- [ ] Implement `onShipmentDelivered()` to update with actual emissions

### 6.11 Worldview (in -worldview)
- [ ] Create `WorldviewCarbonData.kt` with reference carbon values
- [ ] Define packaging emissions per packaging type
- [ ] Define typical emissions ranges per product category
- [ ] Create `CarbonFootprintBuilder.kt` for tests
- [ ] Create realistic carbon scenarios

### 6.12 Testing
- [ ] Unit test: `CalculateOrderCarbonUseCaseTest.kt` with mocked provider
- [ ] Unit test: `CarbonFootprintCalculatorTest.kt`
- [ ] Unit test: `CarbonFootprintEntityMappersTest.kt`
- [ ] Integration test: `CarbonFootprintRepositoryImplTest.kt`
- [ ] Integration test: `CarbonFootprintControllerV1Test.kt`
- [ ] Feature file: `features/06-carbon-accounting/01-calculate-order-carbon.feature`
- [ ] Feature file: `features/06-carbon-accounting/02-update-actual-emissions.feature`
- [ ] Feature file: `features/06-carbon-accounting/03-generate-period-report.feature`
- [ ] Create `StepsCarbonFootprint.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

---

## Epic 7: Tax Compliance (Supporting Domain)

### 7.1 Module Setup
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-api` structure
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-impl` structure
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `TaxComplianceModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 7.2 Domain Model (in -impl)
- [ ] Create `domain/model/TaxCalculation.kt` aggregate root
- [ ] Create `domain/model/TaxCalculationId.kt` value object
- [ ] Create `domain/model/TaxRate.kt` value object
- [ ] Create `domain/model/TaxBreakdown.kt` value object
- [ ] Create `domain/model/InvoiceNumber.kt` value object
- [ ] Create `TaxCalculation.create()` factory method
- [ ] Unit test: `TaxCalculationTest.kt`

### 7.3 Domain Services (in -impl)
- [ ] Create `domain/service/TaxCalculationService.kt` domain service
- [ ] Inject `CountryConfigProvider` for VAT rates
- [ ] Implement `calculateTax()` method with country-specific rules
- [ ] Implement NL special rule: reduced VAT for eco products over €150
- [ ] Implement DE rule: different VAT for different product categories
- [ ] Implement FR standard VAT calculation
- [ ] Unit test: `TaxCalculationServiceTest.kt` for all country scenarios

### 7.4 Repository (in -impl)
- [ ] Create `domain/repository/TaxCalculationRepository.kt` interface
- [ ] Create `infrastructure/persistence/TaxCalculationEntity.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationEntityMappers.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationRepositoryJdbc.kt` interface

### 7.5 Use Cases (in -impl)
- [ ] Create `application/CalculateTaxUseCase.kt`
- [ ] Create `application/CalculateTaxCommand.kt`
- [ ] Create `application/GenerateInvoiceUseCase.kt`
- [ ] Create `application/FindTaxCalculationUseCase.kt`

### 7.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/TaxInternalController.kt`
- [ ] Create `infrastructure/rest/internal/TaxCalculationDto.kt`
- [ ] Create endpoint to calculate tax for order
- [ ] Create endpoint to generate invoice

### 7.7 Database (in application)
- [ ] Create migration `V006__create_tax_compliance_schema.sql`
- [ ] Create `tax_calculation` table
- [ ] Create `invoice` table
- [ ] Create indexes on `tax_calculation(order_id)`, `invoice(order_id)`

### 7.8 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to calculate tax
- [ ] Implement `onOrderConfirmed()` to generate invoice

### 7.9 Worldview (in -worldview)
- [ ] Create `WorldviewTaxRules.kt` with realistic VAT rates
- [ ] NL: Standard 21%, Reduced 9% for eco products > €150
- [ ] DE: Standard 19%, Reduced 7% for food/textiles
- [ ] FR: Standard 20%
- [ ] Create `TaxCalculationBuilder.kt` for tests
- [ ] Create realistic tax calculation scenarios

### 7.10 Testing
- [ ] Unit test: `CalculateTaxUseCaseTest.kt`
- [ ] Unit test: `TaxCalculationServiceTest.kt` with all country rules
- [ ] Unit test: `TaxCalculationEntityMappersTest.kt`
- [ ] Integration test: `TaxCalculationRepositoryImplTest.kt`
- [ ] Feature file: `features/07-tax-compliance/01-calculate-tax-nl.feature`
- [ ] Feature file: `features/07-tax-compliance/02-calculate-tax-de.feature`
- [ ] Feature file: `features/07-tax-compliance/03-calculate-tax-fr.feature`
- [ ] Feature file: `features/07-tax-compliance/04-generate-invoice.feature`
- [ ] Create `StepsTax.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

---

## Epic 8: Payment Processing (Supporting Domain - Mocked)

### 8.1 Module Setup
- [ ] Create `ecoglobal/payment/payment-api` structure
- [ ] Create `ecoglobal/payment/payment-impl` structure
- [ ] Create `ecoglobal/payment/payment-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `PaymentModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 8.2 Domain Model (in -impl)
- [ ] Create `domain/model/Payment.kt` aggregate root
- [ ] Create `domain/model/PaymentId.kt` value object
- [ ] Create `domain/model/PaymentStatus.kt` enum (PENDING, COMPLETED, FAILED, REFUNDED)
- [ ] Create `domain/model/PaymentMethod.kt` enum (IDEAL, CREDITCARD, PAYPAL, SOFORT)
- [ ] Create `Payment.create()` factory method
- [ ] Create `Payment.complete()` method
- [ ] Create `Payment.fail()` method
- [ ] Unit test: `PaymentTest.kt`

### 8.3 Domain Services (in -impl)
- [ ] Create `domain/service/MockPaymentGateway.kt` domain service
- [ ] Implement `processPayment()` method (always succeeds after 100ms delay)
- [ ] Implement `refund()` method (always succeeds)
- [ ] Add configurable failure rate for testing (default 0%)
- [ ] Unit test: `MockPaymentGatewayTest.kt`

### 8.4 Repository (in -impl)
- [ ] Create `domain/repository/PaymentRepository.kt` interface
- [ ] Create `infrastructure/persistence/PaymentEntity.kt`
- [ ] Create `infrastructure/persistence/PaymentEntityMappers.kt`
- [ ] Create `infrastructure/persistence/PaymentRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/PaymentRepositoryJdbc.kt` interface

### 8.5 Use Cases (in -impl)
- [ ] Create `application/InitiatePaymentUseCase.kt`
- [ ] Create `application/InitiatePaymentCommand.kt`
- [ ] Create `application/ProcessPaymentUseCase.kt`
- [ ] Create `application/RefundPaymentUseCase.kt`
- [ ] Create `application/FindPaymentUseCase.kt`

### 8.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/PaymentControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/InitiatePaymentRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/PaymentResponseV1.kt`
- [ ] Create endpoint to initiate payment
- [ ] Create webhook endpoint (mocked, always succeeds)

### 8.7 Database (in application)
- [ ] Create migration `V007__create_payment_schema.sql`
- [ ] Create `payment` table
- [ ] Create indexes on `payment(order_id)`, `payment(status)`

### 8.8 Event Consumers & Publishers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to initiate payment
- [ ] Create `infrastructure/messaging/PaymentEventPublisher.kt`
- [ ] Publish `PaymentCompletedEvent` after successful payment
- [ ] Publish `PaymentFailedEvent` after failed payment

### 8.9 Events (in -api)
- [ ] Create `PaymentCompletedEvent.kt` data class
- [ ] Create `PaymentFailedEvent.kt` data class

### 8.10 Worldview (in -worldview)
- [ ] Create `WorldviewPaymentMethods.kt` with available methods per country
- [ ] NL: iDEAL, Credit Card, PayPal
- [ ] DE: SOFORT, Credit Card, PayPal
- [ ] FR: Credit Card, PayPal
- [ ] Create `PaymentBuilder.kt` for tests

### 8.11 Testing
- [ ] Unit test: `InitiatePaymentUseCaseTest.kt`
- [ ] Unit test: `MockPaymentGatewayTest.kt`
- [ ] Unit test: `PaymentEntityMappersTest.kt`
- [ ] Integration test: `PaymentRepositoryImplTest.kt`
- [ ] Integration test: `PaymentControllerV1Test.kt`
- [ ] Feature file: `features/08-payment/01-initiate-payment.feature`
- [ ] Feature file: `features/08-payment/02-payment-success.feature`
- [ ] Feature file: `features/08-payment/03-payment-failure.feature`
- [ ] Create `StepsPayment.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

---

## Epic 9: End-to-End Integration

### 9.1 Full Order Flow Test
- [ ] Feature file: `features/09-e2e/01-complete-order-flow.feature`
- [ ] Scenario: Customer places order → payment → shipment → delivery
- [ ] Verify: Order status transitions correctly
- [ ] Verify: Stock is reserved, then committed
- [ ] Verify: Tax is calculated
- [ ] Verify: Shipment is created
- [ ] Verify: Carbon footprint is calculated and updated
- [ ] Verify: All events are published and consumed

### 9.2 Multi-Country Orders
- [ ] Feature file: `features/09-e2e/02-nl-customer-order.feature`
- [ ] Feature file: `features/09-e2e/03-de-customer-order.feature`
- [ ] Feature file: `features/09-e2e/04-fr-customer-order.feature`
- [ ] Verify country-specific tax rates applied
- [ ] Verify country-specific carriers used
- [ ] Verify country-specific payment methods available

### 9.3 Fulfillment Strategy Tests
- [ ] Feature file: `features/09-e2e/05-fastest-fulfillment.feature`
- [ ] Feature file: `features/09-e2e/06-cheapest-fulfillment.feature`
- [ ] Feature file: `features/09-e2e/07-lowest-carbon-fulfillment.feature`
- [ ] Verify different warehouses chosen based on strategy
- [ ] Verify different carriers chosen based on strategy

### 9.4 Edge Cases
- [ ] Feature file: `features/09-e2e/08-out-of-stock-handling.feature`
- [ ] Feature file: `features/09-e2e/09-order-cancellation.feature`
- [ ] Feature file: `features/09-e2e/10-payment-failure-handling.feature`
- [ ] Verify reservation is released when order cancelled
- [ ] Verify payment failure prevents order confirmation

### 9.5 Performance & Load Tests
- [ ] Create `LoadTest.kt` simulating 100 concurrent orders
- [ ] Verify no deadlocks in stock reservation
- [ ] Verify event processing handles load
- [ ] Verify database connection pool sizing

---

## Epic 10: Documentation & Deployment

### 10.1 API Documentation
- [ ] Add Springdoc OpenAPI dependency
- [ ] Add OpenAPI annotations to all controllers
- [ ] Generate OpenAPI spec at `/api-docs`
- [ ] Add API examples to documentation
- [ ] Create Postman collection for manual testing

### 10.2 Operational Documentation
- [ ] Create `docs/DEPLOYMENT.md` with deployment instructions
- [ ] Create `docs/MONITORING.md` with monitoring strategy
- [ ] Create `docs/TROUBLESHOOTING.md` with common issues
- [ ] Document all environment variables
- [ ] Document database backup/restore procedures

### 10.3 Architecture Decision Records
- [ ] Create `docs/adr/001-modular-monolith.md`
- [ ] Create `docs/adr/002-dependency-inversion-for-core-domains.md`
- [ ] Create `docs/adr/003-event-driven-integration.md`
- [ ] Create `docs/adr/004-mocked-payment-gateway.md`
- [ ] Create `docs/adr/005-worldview-for-testing.md`

### 10.4 Developer Onboarding
- [ ] Create `docs/ONBOARDING.md` for new developers
- [ ] Document bounded context boundaries
- [ ] Document dependency rules
- [ ] Document testing strategy
- [ ] Create architecture diagram

### 10.5 Final Polish
- [ ] Run `./gradlew pitest` on all modules, ensure 0 surviving mutants
- [ ] Run `./gradlew test` ensure all tests pass
- [ ] Run `./gradlew build` ensure clean build
- [ ] Verify all Cucumber scenarios pass
- [ ] Code review checklist complete
- [ ] Update README with final project status
# EcoGlobal Implementation Backlog (Updated with Documentation)

Complete checklist for implementing the EcoGlobal sustainable products platform.

---

## Epic 0: Project Skeleton & Infrastructure Setup

### 0.1 Repository Structure
- [ ] Create root project structure with `build-logic`, `common`, `ecoglobal` folders
- [ ] Set up `build-logic` folder with convention plugins
- [ ] Create `buildlogic.kotlin-conventions.gradle.kts`
- [ ] Create `buildlogic.cucumber-conventions.gradle.kts`
- [ ] Create `buildlogic.jacoco-conventions.gradle.kts`
- [ ] Create `buildlogic.pitest-conventions.gradle.kts`
- [ ] Create `buildlogic.errorprone.gradle.kts`
- [ ] Create `buildlogic.project-root.gradle.kts`
- [ ] Create `libs.versions.toml` with all dependencies
- [ ] Create root `build.gradle.kts`
- [ ] Create root `settings.gradle.kts` (initially empty, will add modules as we go)
- [ ] Create `gradle.properties` with configuration
- [ ] Create `.gitignore`

### 0.2 Docker Infrastructure
- [ ] Create `docker/compose.yaml` with PostgreSQL
- [ ] Create `docker/compose.yaml` with ActiveMQ
- [ ] Create `docker/sql/init.sql` for database initialization
- [ ] Create `.env.example` for environment variables
- [ ] Test: `docker compose up` starts successfully

### 0.3 Application Module
- [ ] Create `ecoglobal/application` module structure
- [ ] Create `build.gradle.kts` for application module
- [ ] Create `Application.kt` with Spring Boot main class
- [ ] Create `SecurityConfig.kt` (basic setup, no authentication yet)
- [ ] Create `application.yaml` with basic configuration
- [ ] Create `application-test.yaml` for test profile
- [ ] Test: Application starts successfully

### 0.4 Test Infrastructure
- [ ] Create `ecoglobal/test/use-case` module structure
- [ ] Create `build.gradle.kts` for use-case tests
- [ ] Create `CucumberSpringBootTest.kt`
- [ ] Create `CucumberTestRunner.kt`
- [ ] Create `SpringTransactionHooks.kt`
- [ ] Create `src/test/resources/features` folder structure
- [ ] Test: Cucumber runner executes (with no features yet)

### 0.5 Documentation
- [ ] Create `README.md` with project overview
- [ ] Create `docs/ARCHITECTURE.md` describing bounded contexts
- [ ] Create `docs/GETTING_STARTED.md` with setup instructions
- [ ] Create `docs/STRATEGIC_DESIGN.md` with domain classification
- [ ] Create `Justfile` with common commands

---

## Epic 0.5: MkDocs Documentation Infrastructure

### 0.5.1 Documentation Directory Setup
- [ ] Create `documentation/` directory at root level
- [ ] Create `documentation/docs/` directory for content
- [ ] Create `documentation/docker/` directory for Docker configs
- [ ] Create directory structure:
  ```
  documentation/
  ├── docs/
  │   ├── index.md
  │   ├── getting-started/
  │   ├── architecture/
  │   │   ├── overview.md
  │   │   ├── bounded-contexts/
  │   │   └── adr/
  │   ├── domains/
  │   └── development/
  ├── docker/
  └── mkdocs.yml
  ```

### 0.5.2 MkDocs Configuration
- [ ] Create `documentation/mkdocs.yml` with Material theme
- [ ] Configure plugins: mkdocs-material, kroki, glightbox, table-reader, swagger-ui
- [ ] Set up navigation structure
- [ ] Configure theme colors (eco-friendly green theme)
- [ ] Add site metadata (name, description, repo_url)
- [ ] Configure markdown extensions (admonitions, code highlighting, tables)

### 0.5.3 Poetry Setup
- [ ] Create `documentation/pyproject.toml` with Poetry config
- [ ] Add dependencies:
  - mkdocs-material
  - mkdocs-kroki-plugin
  - mkdocs-glightbox
  - mkdocs-table-reader-plugin
  - mkdocs-swagger-ui-tag
  - mkdocs-awesome-pages-plugin
- [ ] Create `documentation/poetry.lock`
- [ ] Test: `poetry install` succeeds

### 0.5.4 Docker Configuration for Documentation
- [ ] Create `documentation/docker/docker-compose.yml` with:
  - MkDocs service (port 8000)
  - Kroki service (for diagram rendering)
  - Kroki supporting services (mermaid, bpmn, excalidraw)
- [ ] Create `documentation/docker/Dockerfile.serve` for MkDocs
- [ ] Create `documentation/.dockerignore`
- [ ] Test: `docker compose up` starts all services

### 0.5.5 Justfile for Documentation
- [ ] Create `documentation/justfile` with commands:
  - `docker-up` - Start documentation server
  - `docker-down` - Stop all services
  - `poetry-install` - Install dependencies
  - `poetry-build` - Build static site
  - `poetry-serve` - Serve locally without Docker
- [ ] Test: All just commands work

### 0.5.6 Initial Documentation Content
- [ ] Create `docs/index.md` - Project overview
- [ ] Create `docs/getting-started/setup.md` - Development setup
- [ ] Create `docs/getting-started/architecture-overview.md` - High-level architecture
- [ ] Create `docs/architecture/strategic-design.md` - Domain classification
- [ ] Create `docs/architecture/bounded-contexts/overview.md` - Bounded context map
- [ ] Create `docs/development/coding-standards.md` - Copy from project standards
- [ ] Create `docs/development/testing-strategy.md` - Including worldview explanation

### 0.5.7 ADR Template and Structure
- [ ] Create `docs/architecture/adr/template.md` with ADR structure
- [ ] Create `docs/architecture/adr/0001-modular-monolith.md`
- [ ] Create `docs/architecture/adr/0002-ddd-tactical-patterns.md`
- [ ] Create `docs/architecture/adr/0003-dependency-inversion-strategy.md`
- [ ] Create `docs/architecture/adr/0004-worldview-testing.md`
- [ ] Create `docs/architecture/adr/0005-event-driven-integration.md`

### 0.5.8 Diagram Examples
- [ ] Create example PlantUML diagram in documentation
- [ ] Create example Mermaid diagram in documentation
- [ ] Create bounded context diagram showing all domains
- [ ] Create dependency diagram showing core vs supporting
- [ ] Test: All diagrams render via Kroki

### 0.5.9 Documentation README
- [ ] Create `documentation/README.md` based on provided guide
- [ ] Include quick start instructions
- [ ] Document available commands
- [ ] Include troubleshooting section
- [ ] Add links to relevant resources

### 0.5.10 Verification
- [ ] Test: Documentation builds without errors (`poetry run mkdocs build`)
- [ ] Test: Documentation serves locally (`just docker-up`)
- [ ] Test: All diagrams render correctly
- [ ] Test: Navigation works properly
- [ ] Verify: All links resolve correctly

---

## Epic 1: Common Modules (Generic Subdomains)

### 1.1 common-country Module
- [ ] Create `common/common-country` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `CountryCode.kt` enum (NL, DE, FR)
- [ ] Create `Currency.kt` enum (EUR)
- [ ] Create `CountryRules.kt` data class
- [ ] Create `CountryConfigProvider.kt` interface
- [ ] Create `CountryConfigProviderImpl.kt` with hardcoded rules
- [ ] Unit test: `CountryCodeTest.kt`
- [ ] Unit test: `CountryConfigProviderImplTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.2 common-money Module
- [ ] Create `common/common-money` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Money.kt` value object with validation
- [ ] Create `Money.kt` arithmetic operators (+, -, *, /)
- [ ] Create `Money.ZERO` constant
- [ ] Unit test: `MoneyTest.kt` covering all operations
- [ ] Unit test: Validate currency matching in operations
- [ ] Add to root `settings.gradle.kts`

### 1.3 common-measurement Module
- [ ] Create `common/common-measurement` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Weight.kt` value object (in kg)
- [ ] Create `Distance.kt` value object (in km)
- [ ] Create `Dimensions.kt` value object (length, width, height in cm)
- [ ] Create `CarbonAmount.kt` value object (in kg CO2)
- [ ] Create `Volume.kt` calculated from Dimensions
- [ ] Unit test: `WeightTest.kt`
- [ ] Unit test: `DimensionsTest.kt`
- [ ] Unit test: `CarbonAmountTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.4 common-address Module
- [ ] Create `common/common-address` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Address.kt` value object (street, city, postal code, country)
- [ ] Create `PostalCode.kt` value object with country-specific validation
- [ ] Create validation for NL postal codes (1234 AB format)
- [ ] Create validation for DE postal codes (5 digits)
- [ ] Create validation for FR postal codes (5 digits)
- [ ] Unit test: `AddressTest.kt`
- [ ] Unit test: `PostalCodeTest.kt` with country-specific validation
- [ ] Add to root `settings.gradle.kts`

### 1.5 common-time Module
- [ ] Create `common/common-time` module structure
- [ ] Create `build.gradle.kts`
- [ ] Create `Period.kt` value object (start date, end date)
- [ ] Create `Period.kt` overlap detection
- [ ] Create `Period.kt` contains date check
- [ ] Create `Period.kt` duration calculation
- [ ] Unit test: `PeriodTest.kt`
- [ ] Add to root `settings.gradle.kts`

### 1.6 common-data-jdbc Module
- [ ] Create `common/common-data-jdbc` module structure
- [ ] Create `build.gradle.kts` with Spring Data JDBC dependency
- [ ] Create base repository utilities if needed
- [ ] Create common database converters (for value objects)
- [ ] Create `MoneyConverter.kt` for JDBC
- [ ] Create `WeightConverter.kt` for JDBC
- [ ] Create `CarbonAmountConverter.kt` for JDBC
- [ ] Add to root `settings.gradle.kts`

### 1.7 Documentation
- [ ] Document: `docs/domains/common/overview.md` - Purpose of common modules
- [ ] Document: `docs/domains/common/country.md` - Country configuration
- [ ] Document: `docs/domains/common/money.md` - Money value object
- [ ] Document: `docs/domains/common/measurement.md` - Measurement value objects
- [ ] Document: `docs/domains/common/address.md` - Address validation rules
- [ ] Document: `docs/domains/common/time.md` - Time utilities
- [ ] Add code examples for each common module
- [ ] Create Mermaid diagram showing common module dependencies

---

## Epic 2: Product Catalog (Supporting Domain)

### 2.1 Module Setup
- [ ] Create `ecoglobal/product-catalog/product-catalog-api` structure
- [ ] Create `ecoglobal/product-catalog/product-catalog-impl` structure
- [ ] Create `ecoglobal/product-catalog/product-catalog-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `ProductCatalogModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 2.2 Domain Model (in -impl)
- [ ] Create `domain/model/Product.kt` aggregate root
- [ ] Create `domain/model/ProductId.kt` value object
- [ ] Create `domain/model/ProductName.kt` value object
- [ ] Create `domain/model/SustainabilityRating.kt` enum (A_PLUS to F)
- [ ] Create `domain/model/Certification.kt` enum (EU_ECOLABEL, FAIR_TRADE, etc.)
- [ ] Create `domain/model/PackagingType.kt` enum (RECYCLABLE_CARDBOARD, etc.)
- [ ] Create `domain/model/ProductCategory.kt` enum (SOLAR_PANELS, CONTAINERS, TEXTILES)
- [ ] Create factory method `Product.create()` with validation
- [ ] Unit test: `ProductTest.kt` for invariants and business rules

### 2.3 Repository (in -impl)
- [ ] Create `domain/repository/ProductRepository.kt` interface
- [ ] Create `infrastructure/persistence/ProductEntity.kt`
- [ ] Create `infrastructure/persistence/ProductEntityMappers.kt`
- [ ] Create `infrastructure/persistence/ProductRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/ProductRepositoryJdbc.kt` interface

### 2.4 Use Cases (in -impl)
- [ ] Create `application/CreateProductUseCase.kt`
- [ ] Create `application/CreateProductCommand.kt`
- [ ] Create `application/FindProductsUseCase.kt`
- [ ] Create `application/UpdateProductAvailabilityUseCase.kt`
- [ ] Create `application/UpdateProductAvailabilityCommand.kt`

### 2.5 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/ProductControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductResponseV1.kt`
- [ ] Create `infrastructure/rest/v1/ProductDtoMappers.kt`
- [ ] Create `infrastructure/rest/internal/ProductInternalController.kt` for operators

### 2.6 Database (in application)
- [ ] Create migration `V001__create_product_catalog_schema.sql`
- [ ] Create `product` table with all fields
- [ ] Create `product_certifications` junction table
- [ ] Create `product_country_availability` table
- [ ] Create indexes on `product(category)`, `product(sustainability_rating)`

### 2.7 Events (in -api)
- [ ] Create `ProductCreatedEvent.kt` data class
- [ ] Create `ProductAvailabilityChangedEvent.kt` data class
- [ ] Create `infrastructure/messaging/ProductEventPublisher.kt` (in -impl)

### 2.8 Worldview (in -worldview)
- [ ] Create `WorldviewProduct.kt` object with realistic products
- [ ] Create 5 solar panel products (different wattages, brands)
- [ ] Create 5 reusable container products (different sizes, materials)
- [ ] Create 5 organic textile products (shirts, bags, towels)
- [ ] Create `ProductBuilder.kt` for tests
- [ ] Create `insertWorldviewProducts()` function
- [ ] Integration test: Verify worldview products can be inserted

### 2.9 Testing
- [ ] Unit test: `CreateProductUseCaseTest.kt`
- [ ] Unit test: `ProductEntityMappersTest.kt`
- [ ] Integration test: `ProductRepositoryImplTest.kt`
- [ ] Integration test: `ProductControllerV1Test.kt`
- [ ] Feature file: `features/02-product-catalog/01-create-product.feature`
- [ ] Feature file: `features/02-product-catalog/02-list-products-by-category.feature`
- [ ] Feature file: `features/02-product-catalog/03-update-availability.feature`
- [ ] Create `StepsProduct.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

### 2.10 Documentation
- [ ] Document: `docs/domains/product-catalog/overview.md` - Domain purpose and boundaries
- [ ] Document: `docs/domains/product-catalog/model.md` - Aggregate structure with diagram
- [ ] Document: `docs/domains/product-catalog/worldview.md` - Worldview products reference
- [ ] Document: `docs/domains/product-catalog/api.md` - REST API endpoints (import OpenAPI)
- [ ] Document: `docs/domains/product-catalog/events.md` - Published events
- [ ] Create PlantUML diagram of Product aggregate
- [ ] Create table showing worldview products with key properties
- [ ] Add sustainability rating explanation

---

## Epic 3: Inventory Management (Supporting Domain)

### 3.1 Module Setup
- [ ] Create `ecoglobal/inventory/inventory-api` structure
- [ ] Create `ecoglobal/inventory/inventory-impl` structure
- [ ] Create `ecoglobal/inventory/inventory-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `InventoryModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 3.2 Domain Model (in -impl)
- [ ] Create `domain/model/InventoryLevel.kt` aggregate root
- [ ] Create `domain/model/WarehouseId.kt` value object (NL_WAREHOUSE, DE_WAREHOUSE, FR_WAREHOUSE)
- [ ] Create `domain/model/StockQuantity.kt` value object
- [ ] Create `domain/model/Reservation.kt` entity
- [ ] Create `domain/model/ReservationId.kt` value object
- [ ] Create `InventoryLevel.reserve()` method with validation
- [ ] Create `InventoryLevel.commitReservation()` method
- [ ] Create `InventoryLevel.releaseReservation()` method
- [ ] Create `InventoryLevel.adjustStock()` method
- [ ] Unit test: `InventoryLevelTest.kt` for all business rules

### 3.3 Repository (in -impl)
- [ ] Create `domain/repository/InventoryRepository.kt` interface
- [ ] Create `infrastructure/persistence/InventoryEntity.kt`
- [ ] Create `infrastructure/persistence/ReservationEntity.kt`
- [ ] Create `infrastructure/persistence/InventoryEntityMappers.kt`
- [ ] Create `infrastructure/persistence/InventoryRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/InventoryRepositoryJdbc.kt` interface

### 3.4 Use Cases (in -impl)
- [ ] Create `application/ReserveStockUseCase.kt`
- [ ] Create `application/ReserveStockCommand.kt`
- [ ] Create `application/CommitReservationUseCase.kt`
- [ ] Create `application/ReleaseReservationUseCase.kt`
- [ ] Create `application/AdjustStockUseCase.kt`
- [ ] Create `application/CheckAvailabilityUseCase.kt`
- [ ] Create `application/CheckAvailabilityQuery.kt`

### 3.5 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/InventoryInternalController.kt`
- [ ] Create `infrastructure/rest/internal/InventoryDto.kt`
- [ ] Create `infrastructure/rest/internal/StockAdjustmentRequest.kt`

### 3.6 Database (in application)
- [ ] Create migration `V002__create_inventory_schema.sql`
- [ ] Create `inventory_level` table
- [ ] Create `reservation` table
- [ ] Create indexes on `inventory_level(product_id, warehouse_id)`
- [ ] Create unique constraint on `inventory_level(product_id, warehouse_id)`

### 3.7 Provider Interface Implementation (in -impl)
- [ ] Create interface `InventoryAvailabilityProvider.kt` in order-fulfillment-api (will do in Epic 4)
- [ ] Create `infrastructure/provider/InventoryAvailabilityProviderImpl.kt` implementing the interface
- [ ] Integration test: Provider returns correct availability

### 3.8 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to reserve stock
- [ ] Implement `onOrderCancelled()` to release reservation
- [ ] Implement `onOrderShipped()` to commit reservation

### 3.9 Worldview (in -worldview)
- [ ] Create `WorldviewInventory.kt` with initial stock levels
- [ ] Create realistic stock for NL warehouse (100-500 units per product)
- [ ] Create realistic stock for DE warehouse (50-300 units per product)
- [ ] Create realistic stock for FR warehouse (50-300 units per product)
- [ ] Create `InventoryBuilder.kt` for tests
- [ ] Create `insertWorldviewInventory()` function
- [ ] Integration test: Verify worldview inventory can be inserted

### 3.10 Testing
- [ ] Unit test: `ReserveStockUseCaseTest.kt`
- [ ] Unit test: `InventoryEntityMappersTest.kt`
- [ ] Integration test: `InventoryRepositoryImplTest.kt`
- [ ] Integration test: `InventoryAvailabilityProviderImplTest.kt`
- [ ] Feature file: `features/03-inventory/01-reserve-stock.feature`
- [ ] Feature file: `features/03-inventory/02-commit-reservation.feature`
- [ ] Feature file: `features/03-inventory/03-release-reservation.feature`
- [ ] Feature file: `features/03-inventory/04-adjust-stock.feature`
- [ ] Create `StepsInventory.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

### 3.11 Documentation
- [ ] Document: `docs/domains/inventory/overview.md` - Domain purpose and reservation pattern
- [ ] Document: `docs/domains/inventory/model.md` - Aggregate structure with diagram
- [ ] Document: `docs/domains/inventory/worldview.md` - Warehouse locations and stock levels
- [ ] Document: `docs/domains/inventory/api.md` - Internal API endpoints
- [ ] Document: `docs/domains/inventory/integration.md` - Provider implementation for Order Fulfillment
- [ ] Create sequence diagram showing reservation flow (reserve → commit/release)
- [ ] Create table showing worldview inventory per warehouse
- [ ] Document eventual consistency patterns used

---

## Epic 4: Order Fulfillment (Core Domain)

### 4.1 Module Setup
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-api` structure
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-impl` structure
- [ ] Create `ecoglobal/order-fulfillment/order-fulfillment-worldview` structure
- [ ] Create `build.gradle.kts` for each module (NO dependencies on supporting domains in -impl!)
- [ ] Create `OrderFulfillmentModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 4.2 Provider Interfaces (in -api)
- [ ] Create `InventoryAvailabilityProvider.kt` interface
- [ ] Create `ShippingCostProvider.kt` interface
- [ ] Create `CarbonDataProvider.kt` interface
- [ ] Create DTOs for these providers

### 4.3 Domain Model (in -impl)
- [ ] Create `domain/model/Order.kt` aggregate root
- [ ] Create `domain/model/OrderId.kt` value object
- [ ] Create `domain/model/CustomerId.kt` value object
- [ ] Create `domain/model/OrderLine.kt` entity
- [ ] Create `domain/model/OrderStatus.kt` enum (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- [ ] Create `domain/model/FulfillmentStrategy.kt` enum (FASTEST, CHEAPEST, LOWEST_CARBON)
- [ ] Create `domain/model/FulfillmentDecision.kt` value object
- [ ] Create `Order.create()` factory method
- [ ] Create `Order.confirm()` method (after payment)
- [ ] Create `Order.cancel()` method
- [ ] Create `Order.markShipped()` method
- [ ] Unit test: `OrderTest.kt` for all business rules and state transitions

### 4.4 Domain Service (in -impl)
- [ ] Create `domain/service/FulfillmentOptimizer.kt` domain service
- [ ] Inject the three provider interfaces via constructor
- [ ] Create `optimize()` method that takes order and strategy
- [ ] Implement FASTEST strategy (choose warehouse with stock and shortest distance)
- [ ] Implement CHEAPEST strategy (calculate total cost per warehouse)
- [ ] Implement LOWEST_CARBON strategy (calculate carbon per warehouse)
- [ ] Unit test: `FulfillmentOptimizerTest.kt` with mocked providers

### 4.5 Repository (in -impl)
- [ ] Create `domain/repository/OrderRepository.kt` interface
- [ ] Create `infrastructure/persistence/OrderEntity.kt`
- [ ] Create `infrastructure/persistence/OrderLineEntity.kt`
- [ ] Create `infrastructure/persistence/OrderEntityMappers.kt`
- [ ] Create `infrastructure/persistence/OrderRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/OrderRepositoryJdbc.kt` interface

### 4.6 Use Cases (in -impl)
- [ ] Create `application/PlaceOrderUseCase.kt`
- [ ] Create `application/PlaceOrderCommand.kt`
- [ ] Create `application/ConfirmOrderUseCase.kt` (after payment)
- [ ] Create `application/CancelOrderUseCase.kt`
- [ ] Create `application/FindOrderUseCase.kt`
- [ ] Create `application/ListOrdersUseCase.kt`

### 4.7 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/OrderControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/PlaceOrderRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderResponseV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderLineRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/OrderDtoMappers.kt`
- [ ] Create `infrastructure/rest/internal/OrderInternalController.kt`

### 4.8 Database (in application)
- [ ] Create migration `V003__create_order_fulfillment_schema.sql`
- [ ] Create `customer_order` table (order is reserved keyword)
- [ ] Create `order_line` table with foreign key to customer_order
- [ ] Create indexes on `customer_order(customer_id)`, `customer_order(status)`
- [ ] Create index on `order_line(order_id)`

### 4.9 Events (in -api)
- [ ] Create `OrderPlacedEvent.kt` data class
- [ ] Create `OrderConfirmedEvent.kt` data class
- [ ] Create `OrderCancelledEvent.kt` data class
- [ ] Create `OrderShippedEvent.kt` data class
- [ ] Create `infrastructure/messaging/OrderEventPublisher.kt` (in -impl)

### 4.10 Worldview (in -worldview)
- [ ] Create `WorldviewCustomer.kt` with 10 realistic customers
- [ ] Include addresses in NL, DE, FR (at least 3 per country)
- [ ] Create `WorldviewOrder.kt` with 5 realistic order scenarios
- [ ] Scenario 1: Small order, single product, NL customer
- [ ] Scenario 2: Large order, multiple products, DE customer
- [ ] Scenario 3: Mixed products from different categories, FR customer
- [ ] Scenario 4: Bulk order requesting LOWEST_CARBON strategy
- [ ] Scenario 5: Express order requesting FASTEST strategy
- [ ] Create `OrderBuilder.kt` for tests
- [ ] Create `insertWorldviewOrders()` function

### 4.11 Testing
- [ ] Unit test: `PlaceOrderUseCaseTest.kt` with mocked providers
- [ ] Unit test: `FulfillmentOptimizerTest.kt` testing all strategies
- [ ] Unit test: `OrderEntityMappersTest.kt`
- [ ] Integration test: `OrderRepositoryImplTest.kt`
- [ ] Integration test: `OrderControllerV1Test.kt`
- [ ] Feature file: `features/04-order-fulfillment/01-place-order-fastest.feature`
- [ ] Feature file: `features/04-order-fulfillment/02-place-order-cheapest.feature`
- [ ] Feature file: `features/04-order-fulfillment/03-place-order-lowest-carbon.feature`
- [ ] Feature file: `features/04-order-fulfillment/04-confirm-order.feature`
- [ ] Feature file: `features/04-order-fulfillment/05-cancel-order.feature`
- [ ] Create `StepsOrder.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

### 4.12 Documentation
- [ ] Document: `docs/domains/order-fulfillment/overview.md` - Core domain status and strategic importance
- [ ] Document: `docs/domains/order-fulfillment/model.md` - Order aggregate and entities
- [ ] Document: `docs/domains/order-fulfillment/fulfillment-strategies.md` - Algorithm details for each strategy
- [ ] Document: `docs/domains/order-fulfillment/dependency-inversion.md` - How core domain uses providers
- [ ] Document: `docs/domains/order-fulfillment/worldview.md` - Customer profiles and order scenarios
- [ ] Document: `docs/domains/order-fulfillment/api.md` - REST API endpoints
- [ ] Document: `docs/domains/order-fulfillment/events.md` - Published events and integration
- [ ] Create sequence diagram showing complete order placement flow
- [ ] Create flowchart for FulfillmentOptimizer decision logic
- [ ] Create diagram showing dependency inversion pattern
- [ ] Add ADR: `docs/architecture/adr/0006-order-fulfillment-optimization.md`

---

## Epic 5: Logistics & Shipping (Supporting Domain)

### 5.1 Module Setup
- [ ] Create `ecoglobal/logistics/logistics-api` structure
- [ ] Create `ecoglobal/logistics/logistics-impl` structure
- [ ] Create `ecoglobal/logistics/logistics-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `LogisticsModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 5.2 Domain Model (in -impl)
- [ ] Create `domain/model/Shipment.kt` aggregate root
- [ ] Create `domain/model/ShipmentId.kt` value object
- [ ] Create `domain/model/Carrier.kt` enum (POSTNL, DHL, DPD, CHRONOPOST)
- [ ] Create `domain/model/TransportMode.kt` enum (VAN, TRUCK, PARCEL_SERVICE)
- [ ] Create `domain/model/TrackingNumber.kt` value object
- [ ] Create `domain/model/ShipmentStatus.kt` enum (CREATED, IN_TRANSIT, DELIVERED)
- [ ] Create `Shipment.create()` factory method
- [ ] Create `Shipment.markInTransit()` method
- [ ] Create `Shipment.markDelivered()` method with actual carbon
- [ ] Unit test: `ShipmentTest.kt` for business rules

### 5.3 Domain Services (in -impl)
- [ ] Create `domain/service/ShippingCostCalculator.kt` domain service
- [ ] Implement cost calculation per carrier and distance
- [ ] Implement country-specific pricing rules
- [ ] Create `domain/service/CarbonCalculator.kt` domain service
- [ ] Implement carbon calculation: distance * weight * carbon-per-km
- [ ] Define carbon-per-km constants for each carrier/transport mode combination
- [ ] Unit test: `ShippingCostCalculatorTest.kt`
- [ ] Unit test: `CarbonCalculatorTest.kt`

### 5.4 Repository (in -impl)
- [ ] Create `domain/repository/ShipmentRepository.kt` interface
- [ ] Create `infrastructure/persistence/ShipmentEntity.kt`
- [ ] Create `infrastructure/persistence/ShipmentEntityMappers.kt`
- [ ] Create `infrastructure/persistence/ShipmentRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/ShipmentRepositoryJdbc.kt` interface

### 5.5 Use Cases (in -impl)
- [ ] Create `application/CreateShipmentUseCase.kt`
- [ ] Create `application/CreateShipmentCommand.kt`
- [ ] Create `application/UpdateTrackingUseCase.kt`
- [ ] Create `application/MarkDeliveredUseCase.kt`
- [ ] Create `application/CalculateShippingCostUseCase.kt`

### 5.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/ShipmentInternalController.kt`
- [ ] Create `infrastructure/rest/internal/ShipmentDto.kt`
- [ ] Create `infrastructure/rest/internal/CreateShipmentRequest.kt`

### 5.7 Database (in application)
- [ ] Create migration `V004__create_logistics_schema.sql`
- [ ] Create `shipment` table
- [ ] Create indexes on `shipment(order_id)`, `shipment(status)`

### 5.8 Provider Interface Implementations (in -impl)
- [ ] Create `infrastructure/provider/ShippingCostProviderImpl.kt`
- [ ] Create `infrastructure/provider/CarbonDataProviderImpl.kt`
- [ ] Integration test: Providers return correct calculations

### 5.9 Event Consumers & Publishers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderConfirmed()` to create shipment
- [ ] Create `infrastructure/messaging/ShipmentEventPublisher.kt`
- [ ] Publish `ShipmentCreatedEvent` after creating shipment
- [ ] Publish `ShipmentDeliveredEvent` after delivery

### 5.10 Events (in -api)
- [ ] Create `ShipmentCreatedEvent.kt` data class
- [ ] Create `ShipmentDeliveredEvent.kt` data class

### 5.11 Worldview (in -worldview)
- [ ] Create `WorldviewCarrier.kt` with carrier data
- [ ] Define shipping costs per carrier and country
- [ ] Define carbon-per-km per carrier and transport mode
- [ ] Create realistic distance calculations between warehouses and cities
- [ ] Create `ShipmentBuilder.kt` for tests
- [ ] Create `insertWorldviewShipmentData()` function

### 5.12 Testing
- [ ] Unit test: `CreateShipmentUseCaseTest.kt`
- [ ] Unit test: `ShippingCostCalculatorTest.kt` with various scenarios
- [ ] Unit test: `CarbonCalculatorTest.kt` with various scenarios
- [ ] Unit test: `ShipmentEntityMappersTest.kt`
- [ ] Integration test: `ShipmentRepositoryImplTest.kt`
- [ ] Integration test: `ShippingCostProviderImplTest.kt`
- [ ] Integration test: `CarbonDataProviderImplTest.kt`
- [ ] Feature file: `features/05-logistics/01-create-shipment.feature`
- [ ] Feature file: `features/05-logistics/02-calculate-shipping-cost.feature`
- [ ] Feature file: `features/05-logistics/03-calculate-carbon-emissions.feature`
- [ ] Feature file: `features/05-logistics/04-mark-delivered.feature`
- [ ] Create `StepsShipment.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

### 5.13 Documentation
- [ ] Document: `docs/domains/logistics/overview.md` - Domain purpose and carrier integrations
- [ ] Document: `docs/domains/logistics/model.md` - Shipment aggregate and tracking
- [ ] Document: `docs/domains/logistics/carriers.md` - Carrier comparison and capabilities per country
- [ ] Document: `docs/domains/logistics/carbon-calculation.md` - Carbon emission formulas and constants
- [ ] Document: `docs/domains/logistics/worldview.md` - Carrier data and distance matrix
- [ ] Document: `docs/domains/logistics/integration.md` - Provider implementations for Order Fulfillment
- [ ] Create table showing carrier availability per country
- [ ] Create table showing carbon-per-km per carrier/transport mode
- [ ] Create map/diagram showing warehouse locations and typical delivery zones

---

## Epic 6: Carbon Accounting (Core Domain)

### 6.1 Module Setup
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-api` structure
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-impl` structure
- [ ] Create `ecoglobal/carbon-accounting/carbon-accounting-worldview` structure
- [ ] Create `build.gradle.kts` for each module (NO dependencies on supporting!)
- [ ] Create `CarbonAccountingModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 6.2 Provider Interfaces (in -api)
- [ ] Create `ShippingDetailsProvider.kt` interface
- [ ] Create DTOs for shipping details

### 6.3 Domain Model (in -impl)
- [ ] Create `domain/model/CarbonFootprint.kt` aggregate root
- [ ] Create `domain/model/CarbonFootprintId.kt` value object
- [ ] Create `domain/model/EmissionsBreakdown.kt` value object
- [ ] Create `domain/model/ShippingEmissions.kt` component
- [ ] Create `domain/model/PackagingEmissions.kt` component
- [ ] Create `CarbonFootprint.create()` factory method
- [ ] Create `CarbonFootprint.updateWithActualShipping()` method
- [ ] Create `CarbonFootprint.calculateTotal()` method
- [ ] Unit test: `CarbonFootprintTest.kt`

### 6.4 Domain Services (in -impl)
- [ ] Create `domain/service/CarbonFootprintCalculator.kt` domain service
- [ ] Implement `calculateShippingEmissions()` method
- [ ] Implement `calculatePackagingEmissions()` method (based on packaging type)
- [ ] Unit test: `CarbonFootprintCalculatorTest.kt`

### 6.5 Repository (in -impl)
- [ ] Create `domain/repository/CarbonFootprintRepository.kt` interface
- [ ] Create `infrastructure/persistence/CarbonFootprintEntity.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintEntityMappers.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/CarbonFootprintRepositoryJdbc.kt` interface

### 6.6 Use Cases (in -impl)
- [ ] Create `application/CalculateOrderCarbonUseCase.kt`
- [ ] Create `application/CalculateOrderCarbonCommand.kt`
- [ ] Create `application/UpdateWithActualEmissionsUseCase.kt`
- [ ] Create `application/GetCarbonFootprintUseCase.kt`
- [ ] Create `application/GeneratePeriodReportUseCase.kt`

### 6.7 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/CarbonFootprintControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/CarbonFootprintResponseV1.kt`
- [ ] Create endpoint to get carbon footprint by order ID
- [ ] Create `infrastructure/rest/internal/CarbonReportController.kt`
- [ ] Create endpoint to generate period reports

### 6.8 Database (in application)
- [ ] Create migration `V005__create_carbon_accounting_schema.sql`
- [ ] Create `carbon_footprint` table
- [ ] Create indexes on `carbon_footprint(order_id)`

### 6.9 Provider Implementation (in logistics-impl)
- [ ] Create `infrastructure/provider/ShippingDetailsProviderImpl.kt` in logistics-impl
- [ ] Implement interface defined in carbon-accounting-api

### 6.10 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to calculate estimated carbon
- [ ] Implement `onOrderShipped()` to update with actual shipping method
- [ ] Create `infrastructure/messaging/ShipmentEventConsumer.kt`
- [ ] Implement `onShipmentDelivered()` to update with actual emissions

### 6.11 Worldview (in -worldview)
- [ ] Create `WorldviewCarbonData.kt` with reference carbon values
- [ ] Define packaging emissions per packaging type
- [ ] Define typical emissions ranges per product category
- [ ] Create `CarbonFootprintBuilder.kt` for tests
- [ ] Create realistic carbon scenarios

### 6.12 Testing
- [ ] Unit test: `CalculateOrderCarbonUseCaseTest.kt` with mocked provider
- [ ] Unit test: `CarbonFootprintCalculatorTest.kt`
- [ ] Unit test: `CarbonFootprintEntityMappersTest.kt`
- [ ] Integration test: `CarbonFootprintRepositoryImplTest.kt`
- [ ] Integration test: `CarbonFootprintControllerV1Test.kt`
- [ ] Feature file: `features/06-carbon-accounting/01-calculate-order-carbon.feature`
- [ ] Feature file: `features/06-carbon-accounting/02-update-actual-emissions.feature`
- [ ] Feature file: `features/06-carbon-accounting/03-generate-period-report.feature`
- [ ] Create `StepsCarbonFootprint.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

### 6.13 Documentation
- [ ] Document: `docs/domains/carbon-accounting/overview.md` - Core domain status and competitive advantage
- [ ] Document: `docs/domains/carbon-accounting/model.md` - CarbonFootprint aggregate and breakdown
- [ ] Document: `docs/domains/carbon-accounting/calculation-methodology.md` - How emissions are calculated
- [ ] Document: `docs/domains/carbon-accounting/dependency-inversion.md` - How core domain queries logistics
- [ ] Document: `docs/domains/carbon-accounting/worldview.md` - Reference carbon values
- [ ] Document: `docs/domains/carbon-accounting/api.md` - Customer-facing API endpoints
- [ ] Document: `docs/domains/carbon-accounting/reporting.md` - Period reports and aggregations
- [ ] Create sequence diagram showing carbon calculation flow (estimate → update → final)
- [ ] Create breakdown diagram showing components (shipping, packaging, etc.)
- [ ] Add ADR: `docs/architecture/adr/0007-carbon-accounting-accuracy.md`

---

## Epic 7: Tax Compliance (Supporting Domain)

### 7.1 Module Setup
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-api` structure
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-impl` structure
- [ ] Create `ecoglobal/tax-compliance/tax-compliance-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `TaxComplianceModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 7.2 Domain Model (in -impl)
- [ ] Create `domain/model/TaxCalculation.kt` aggregate root
- [ ] Create `domain/model/TaxCalculationId.kt` value object
- [ ] Create `domain/model/TaxRate.kt` value object
- [ ] Create `domain/model/TaxBreakdown.kt` value object
- [ ] Create `domain/model/InvoiceNumber.kt` value object
- [ ] Create `TaxCalculation.create()` factory method
- [ ] Unit test: `TaxCalculationTest.kt`

### 7.3 Domain Services (in -impl)
- [ ] Create `domain/service/TaxCalculationService.kt` domain service
- [ ] Inject `CountryConfigProvider` for VAT rates
- [ ] Implement `calculateTax()` method with country-specific rules
- [ ] Implement NL special rule: reduced VAT for eco products over €150
- [ ] Implement DE rule: different VAT for different product categories
- [ ] Implement FR standard VAT calculation
- [ ] Unit test: `TaxCalculationServiceTest.kt` for all country scenarios

### 7.4 Repository (in -impl)
- [ ] Create `domain/repository/TaxCalculationRepository.kt` interface
- [ ] Create `infrastructure/persistence/TaxCalculationEntity.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationEntityMappers.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/TaxCalculationRepositoryJdbc.kt` interface

### 7.5 Use Cases (in -impl)
- [ ] Create `application/CalculateTaxUseCase.kt`
- [ ] Create `application/CalculateTaxCommand.kt`
- [ ] Create `application/GenerateInvoiceUseCase.kt`
- [ ] Create `application/FindTaxCalculationUseCase.kt`

### 7.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/internal/TaxInternalController.kt`
- [ ] Create `infrastructure/rest/internal/TaxCalculationDto.kt`
- [ ] Create endpoint to calculate tax for order
- [ ] Create endpoint to generate invoice

### 7.7 Database (in application)
- [ ] Create migration `V006__create_tax_compliance_schema.sql`
- [ ] Create `tax_calculation` table
- [ ] Create `invoice` table
- [ ] Create indexes on `tax_calculation(order_id)`, `invoice(order_id)`

### 7.8 Event Consumers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to calculate tax
- [ ] Implement `onOrderConfirmed()` to generate invoice

### 7.9 Worldview (in -worldview)
- [ ] Create `WorldviewTaxRules.kt` with realistic VAT rates
- [ ] NL: Standard 21%, Reduced 9% for eco products > €150
- [ ] DE: Standard 19%, Reduced 7% for food/textiles
- [ ] FR: Standard 20%
- [ ] Create `TaxCalculationBuilder.kt` for tests
- [ ] Create realistic tax calculation scenarios

### 7.10 Testing
- [ ] Unit test: `CalculateTaxUseCaseTest.kt`
- [ ] Unit test: `TaxCalculationServiceTest.kt` with all country rules
- [ ] Unit test: `TaxCalculationEntityMappersTest.kt`
- [ ] Integration test: `TaxCalculationRepositoryImplTest.kt`
- [ ] Feature file: `features/07-tax-compliance/01-calculate-tax-nl.feature`
- [ ] Feature file: `features/07-tax-compliance/02-calculate-tax-de.feature`
- [ ] Feature file: `features/07-tax-compliance/03-calculate-tax-fr.feature`
- [ ] Feature file: `features/07-tax-compliance/04-generate-invoice.feature`
- [ ] Create `StepsTax.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass with worldview data

### 7.11 Documentation
- [ ] Document: `docs/domains/tax-compliance/overview.md` - Domain purpose and regulatory requirements
- [ ] Document: `docs/domains/tax-compliance/model.md` - TaxCalculation aggregate
- [ ] Document: `docs/domains/tax-compliance/country-rules.md` - VAT rules per country with examples
- [ ] Document: `docs/domains/tax-compliance/worldview.md` - Tax rates and special rules
- [ ] Document: `docs/domains/tax-compliance/invoicing.md` - Invoice generation and legal requirements
- [ ] Create table comparing VAT rates across NL, DE, FR
- [ ] Create flowchart showing tax calculation decision tree
- [ ] Document special cases (eco product discount in NL, category-based in DE)

---

## Epic 8: Payment Processing (Supporting Domain - Mocked)

### 8.1 Module Setup
- [ ] Create `ecoglobal/payment/payment-api` structure
- [ ] Create `ecoglobal/payment/payment-impl` structure
- [ ] Create `ecoglobal/payment/payment-worldview` structure
- [ ] Create `build.gradle.kts` for each module
- [ ] Create `PaymentModule.kt` configuration class
- [ ] Add modules to root `settings.gradle.kts`
- [ ] Add dependency in application module

### 8.2 Domain Model (in -impl)
- [ ] Create `domain/model/Payment.kt` aggregate root
- [ ] Create `domain/model/PaymentId.kt` value object
- [ ] Create `domain/model/PaymentStatus.kt` enum (PENDING, COMPLETED, FAILED, REFUNDED)
- [ ] Create `domain/model/PaymentMethod.kt` enum (IDEAL, CREDITCARD, PAYPAL, SOFORT)
- [ ] Create `Payment.create()` factory method
- [ ] Create `Payment.complete()` method
- [ ] Create `Payment.fail()` method
- [ ] Unit test: `PaymentTest.kt`

### 8.3 Domain Services (in -impl)
- [ ] Create `domain/service/MockPaymentGateway.kt` domain service
- [ ] Implement `processPayment()` method (always succeeds after 100ms delay)
- [ ] Implement `refund()` method (always succeeds)
- [ ] Add configurable failure rate for testing (default 0%)
- [ ] Unit test: `MockPaymentGatewayTest.kt`

### 8.4 Repository (in -impl)
- [ ] Create `domain/repository/PaymentRepository.kt` interface
- [ ] Create `infrastructure/persistence/PaymentEntity.kt`
- [ ] Create `infrastructure/persistence/PaymentEntityMappers.kt`
- [ ] Create `infrastructure/persistence/PaymentRepositoryImpl.kt`
- [ ] Create `infrastructure/persistence/PaymentRepositoryJdbc.kt` interface

### 8.5 Use Cases (in -impl)
- [ ] Create `application/InitiatePaymentUseCase.kt`
- [ ] Create `application/InitiatePaymentCommand.kt`
- [ ] Create `application/ProcessPaymentUseCase.kt`
- [ ] Create `application/RefundPaymentUseCase.kt`
- [ ] Create `application/FindPaymentUseCase.kt`

### 8.6 REST API (in -impl)
- [ ] Create `infrastructure/rest/v1/PaymentControllerV1.kt`
- [ ] Create `infrastructure/rest/v1/InitiatePaymentRequestV1.kt`
- [ ] Create `infrastructure/rest/v1/PaymentResponseV1.kt`
- [ ] Create endpoint to initiate payment
- [ ] Create webhook endpoint (mocked, always succeeds)

### 8.7 Database (in application)
- [ ] Create migration `V007__create_payment_schema.sql`
- [ ] Create `payment` table
- [ ] Create indexes on `payment(order_id)`, `payment(status)`

### 8.8 Event Consumers & Publishers (in -impl)
- [ ] Create `infrastructure/messaging/OrderEventConsumer.kt`
- [ ] Implement `onOrderPlaced()` to initiate payment
- [ ] Create `infrastructure/messaging/PaymentEventPublisher.kt`
- [ ] Publish `PaymentCompletedEvent` after successful payment
- [ ] Publish `PaymentFailedEvent` after failed payment

### 8.9 Events (in -api)
- [ ] Create `PaymentCompletedEvent.kt` data class
- [ ] Create `PaymentFailedEvent.kt` data class

### 8.10 Worldview (in -worldview)
- [ ] Create `WorldviewPaymentMethods.kt` with available methods per country
- [ ] NL: iDEAL, Credit Card, PayPal
- [ ] DE: SOFORT, Credit Card, PayPal
- [ ] FR: Credit Card, PayPal
- [ ] Create `PaymentBuilder.kt` for tests

### 8.11 Testing
- [ ] Unit test: `InitiatePaymentUseCaseTest.kt`
- [ ] Unit test: `MockPaymentGatewayTest.kt`
- [ ] Unit test: `PaymentEntityMappersTest.kt`
- [ ] Integration test: `PaymentRepositoryImplTest.kt`
- [ ] Integration test: `PaymentControllerV1Test.kt`
- [ ] Feature file: `features/08-payment/01-initiate-payment.feature`
- [ ] Feature file: `features/08-payment/02-payment-success.feature`
- [ ] Feature file: `features/08-payment/03-payment-failure.feature`
- [ ] Create `StepsPayment.kt` in -impl/testFixtures
- [ ] Cucumber test: All scenarios pass

### 8.12 Documentation
- [ ] Document: `docs/domains/payment/overview.md` - Domain purpose and mock gateway explanation
- [ ] Document: `docs/domains/payment/model.md` - Payment aggregate and state machine
- [ ] Document: `docs/domains/payment/payment-methods.md` - Available methods per country
- [ ] Document: `docs/domains/payment/worldview.md` - Payment methods availability
- [ ] Document: `docs/domains/payment/mock-gateway.md` - How mock gateway works for testing
- [ ] Create state diagram showing payment status transitions
- [ ] Create table showing payment method availability per country
- [ ] Add ADR: `docs/architecture/adr/0008-mocked-payment-gateway.md`

---

## Epic 9: End-to-End Integration

### 9.1 Full Order Flow Test
- [ ] Feature file: `features/09-e2e/01-complete-order-flow.feature`
- [ ] Scenario: Customer places order → payment → shipment → delivery
- [ ] Verify: Order status transitions correctly
- [ ] Verify: Stock is reserved, then committed
- [ ] Verify: Tax is calculated
- [ ] Verify: Shipment is created
- [ ] Verify: Carbon footprint is calculated and updated
- [ ] Verify: All events are published and consumed

### 9.2 Multi-Country Orders
- [ ] Feature file: `features/09-e2e/02-nl-customer-order.feature`
- [ ] Feature file: `features/09-e2e/03-de-customer-order.feature`
- [ ] Feature file: `features/09-e2e/04-fr-customer-order.feature`
- [ ] Verify country-specific tax rates applied
- [ ] Verify country-specific carriers used
- [ ] Verify country-specific payment methods available

### 9.3 Fulfillment Strategy Tests
- [ ] Feature file: `features/09-e2e/05-fastest-fulfillment.feature`
- [ ] Feature file: `features/09-e2e/06-cheapest-fulfillment.feature`
- [ ] Feature file: `features/09-e2e/07-lowest-carbon-fulfillment.feature`
- [ ] Verify different warehouses chosen based on strategy
- [ ] Verify different carriers chosen based on strategy

### 9.4 Edge Cases
- [ ] Feature file: `features/09-e2e/08-out-of-stock-handling.feature`
- [ ] Feature file: `features/09-e2e/09-order-cancellation.feature`
- [ ] Feature file: `features/09-e2e/10-payment-failure-handling.feature`
- [ ] Verify reservation is released when order cancelled
- [ ] Verify payment failure prevents order confirmation

### 9.5 Performance & Load Tests
- [ ] Create `LoadTest.kt` simulating 100 concurrent orders
- [ ] Verify no deadlocks in stock reservation
- [ ] Verify event processing handles load
- [ ] Verify database connection pool sizing

### 9.6 Documentation
- [ ] Document: `docs/testing/e2e-scenarios.md` - Complete end-to-end test scenarios
- [ ] Document: `docs/testing/integration-patterns.md` - How domains integrate via events
- [ ] Create comprehensive sequence diagram showing full order flow across all domains
- [ ] Create event flow diagram showing all published/consumed events
- [ ] Document timing expectations and eventual consistency windows

---

## Epic 10: Documentation & Deployment

### 10.1 API Documentation
- [ ] Add Springdoc OpenAPI dependency
- [ ] Add OpenAPI annotations to all controllers
- [ ] Generate OpenAPI spec at `/api-docs`
- [ ] Add API examples to documentation
- [ ] Create Postman collection for manual testing
- [ ] Document: Import OpenAPI specs into MkDocs using swagger-ui-tag plugin
- [ ] Document: Create `docs/api/overview.md` with API versioning strategy
- [ ] Document: Create `docs/api/authentication.md` (even though not implemented)

### 10.2 Operational Documentation
- [ ] Document: `docs/operations/deployment.md` with deployment instructions
- [ ] Document: `docs/operations/monitoring.md` with monitoring strategy
- [ ] Document: `docs/operations/troubleshooting.md` with common issues
- [ ] Document: `docs/operations/environment-variables.md` - All configuration
- [ ] Document: `docs/operations/database.md` - Backup/restore procedures
- [ ] Document: `docs/operations/docker-compose.md` - Local development setup
- [ ] Create runbook for common operational tasks

### 10.3 Architecture Decision Records
- [ ] ADR: `docs/architecture/adr/0001-modular-monolith.md`
- [ ] ADR: `docs/architecture/adr/0002-dependency-inversion-for-core-domains.md`
- [ ] ADR: `docs/architecture/adr/0003-event-driven-integration.md`
- [ ] ADR: `docs/architecture/adr/0004-mocked-payment-gateway.md`
- [ ] ADR: `docs/architecture/adr/0005-worldview-for-testing.md`
- [ ] ADR: `docs/architecture/adr/0006-order-fulfillment-optimization.md`
- [ ] ADR: `docs/architecture/adr/0007-carbon-accounting-accuracy.md`
- [ ] ADR: `docs/architecture/adr/0008-country-specific-rules.md`
- [ ] ADR: `docs/architecture/adr/0009-aggregate-boundaries.md`
- [ ] ADR: `docs/architecture/adr/0010-testing-strategy.md`

### 10.4 Developer Onboarding
- [ ] Document: `docs/development/onboarding.md` for new developers
- [ ] Document: `docs/development/project-structure.md` - Directory layout
- [ ] Document: `docs/development/bounded-contexts.md` - Context boundaries and dependencies
- [ ] Document: `docs/development/dependency-rules.md` - What can depend on what
- [ ] Document: `docs/development/testing-guide.md` - How to write tests
- [ ] Document: `docs/development/worldview-guide.md` - How to use and extend worldview
- [ ] Create architecture diagram (C4 Context, Container, Component)
- [ ] Create bounded context map with relationships
- [ ] Create dependency diagram showing actual vs allowed dependencies

### 10.5 Domain Documentation Index
- [ ] Create `docs/domains/index.md` with overview of all domains
- [ ] Include domain classification (Core, Supporting, Generic)
- [ ] Include responsibility summary for each domain
- [ ] Include links to detailed domain documentation
- [ ] Create comparison table showing domain characteristics

### 10.6 Final Polish
- [ ] Run `./gradlew pitest` on all modules, ensure 0 surviving mutants
- [ ] Run `./gradlew test` ensure all tests pass
- [ ] Run `./gradlew build` ensure clean build
- [ ] Verify all Cucumber scenarios pass
- [ ] Code review checklist complete
- [ ] Update README with final project status
- [ ] Verify all documentation builds without errors
- [ ] Verify all diagrams render correctly
- [ ] Spell-check all documentation
- [ ] Review and finalize all ADRs

### 10.7 Documentation Publication
- [ ] Build production documentation site (`poetry run mkdocs build`)
- [ ] Verify all links work in production build
- [ ] Set up documentation hosting (GitHub Pages / internal server)
- [ ] Create documentation update process
- [ ] Document how to contribute to documentation
