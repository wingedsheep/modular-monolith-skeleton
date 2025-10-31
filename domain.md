# EcoGlobal: Sustainable Products Platform

A comprehensive example application that demonstrates DDD architecture with real-world complexity.

## Company Overview

**EcoGlobal** is a B2C e-commerce company selling sustainable, eco-friendly products (solar panels, reusable containers, organic textiles, etc.) across Europe. They operate warehouses in Netherlands, Germany, and France, each powered by renewable energy sources. The company is committed to:
- Carbon-neutral operations
- Transparent sustainability metrics
- Optimized clean energy usage
- Compliance with each country's regulations

## Business Context

### The Challenge
- **Multi-country operations**: Each country has different VAT rates, shipping regulations, and product certification requirements
- **Real-time inventory**: Stock distributed across 3 warehouses
- **Dynamic energy pricing**: Electricity costs vary by country and time-of-day (spot market)
- **Carbon accountability**: Track and minimize carbon footprint of every order
- **Complex fulfillment**: Choose optimal warehouse and shipping method based on cost, carbon impact, and delivery time

### The Solution
A modular system where each business capability is a bounded context, with careful attention to dependencies and eventual consistency.

---

## Strategic Domain Classification

### 🎯 Core Domains (Competitive Advantage)

**1. Order Fulfillment**
- Decides which warehouse fulfills each order
- Optimizes for: cost, carbon footprint, delivery time
- This is what makes EcoGlobal special: intelligent, sustainable fulfillment
- Dependencies: NONE (core domain shouldn't depend on supporting)

**2. Carbon Accounting**
- Calculates total carbon footprint per order, per warehouse, per period
- Provides sustainability reports to customers
- Competitive differentiator: transparent, accurate carbon tracking
- Dependencies: NONE (needs data from logistics, but via dependency inversion)

### 🔧 Supporting Domains (Necessary but not differentiating)

**3. Product Catalog**
- Products with sustainability ratings, certifications, weight, dimensions
- Manages which products available in which countries

**4. Inventory Management**
- Stock levels per warehouse
- Reorder thresholds and predictions
- Stock reservations

**5. Logistics & Shipping**
- Carrier integrations (DHL, PostNL, etc.)
- Shipping cost calculations
- Tracking information
- Actual carbon emissions per shipping method

**6. Tax Compliance**
- VAT/tax calculation per country
- Invoice generation
- Tax reporting to authorities

**7. Energy Management**
- Monitor warehouse energy consumption
- Track energy sources (solar, wind, grid)
- Optimize operations for low-cost/low-carbon time windows
- Battery storage management

**8. Payment Processing**
- Handle payments via Stripe/Mollie
- Refunds and chargebacks

### 🌐 Generic Subdomains (Could be bought/reused)

**9. Country Configuration**
- Country-specific rules: VAT rates, shipping regulations, holidays
- Certification requirements per country
- Currency and locale information

**10. Customer Identity**
- Authentication, profiles, addresses
- Could use Auth0 or similar

---

## Bounded Context Details

### 1. Order Fulfillment (Core Domain)

**Responsibility**: Orchestrate the entire order process with optimal decisions.

**Aggregates:**
- `Order` (root)
  - `OrderId`, `CustomerId`, `OrderLines`
  - `ShippingAddress`, `FulfillmentStrategy`
  - `FulfillmentDecision` (which warehouse, which carrier)
  
- `FulfillmentStrategy` (value object)
  - Optimize for: `FASTEST`, `CHEAPEST`, `LOWEST_CARBON`

**Key Use Cases:**
- `PlaceOrderUseCase`: Customer places order
- `OptimizeFulfillmentUseCase`: Decide warehouse + shipping method
- `ConfirmOrderUseCase`: Finalize order after payment

**Domain Services:**
- `FulfillmentOptimizer`: Complex algorithm to choose best warehouse and carrier
  - Needs: inventory levels, shipping costs, carbon data
  - But this is CORE domain! Can't depend on supporting domains directly!

**Integration Needs (Dependency Inversion Required):**
```kotlin
// Defined in order-fulfillment-api (core domain defines the interface)
interface InventoryAvailabilityProvider {
    fun checkAvailability(productId: ProductId, warehouseId: WarehouseId): Int
}

interface ShippingCostProvider {
    fun calculateCost(from: WarehouseId, to: Address, weight: Weight): Money
}

interface CarbonDataProvider {
    fun getCarbonPerKm(carrier: Carrier, transportMode: TransportMode): CarbonAmount
}

// Implementations live in supporting domains:
// - inventory-impl provides InventoryAvailabilityProvider
// - logistics-impl provides ShippingCostProvider and CarbonDataProvider
```

**Why Dependency Inversion?**
- Core domain (Order Fulfillment) needs data from supporting domains
- Can't have core depend on supporting → wrong direction!
- Core defines interfaces, supporting domains implement them

---

### 2. Carbon Accounting (Core Domain)

**Responsibility**: Calculate and track carbon footprint with high accuracy.

**Aggregates:**
- `CarbonFootprint` (root)
  - `OrderId`, `TotalEmissions`
  - `ShippingEmissions`, `WarehouseEmissions`, `PackagingEmissions`
  
- `CarbonReport` (root)
  - Period-based aggregations
  - Customer-facing sustainability reports

**Key Use Cases:**
- `CalculateOrderCarbonUseCase`: Calculate carbon for a specific order
- `GenerateMonthlyReportUseCase`: Sustainability report
- `CompareAlternativesUseCase`: "What if we used different warehouse?"

**Integration Needs (Dependency Inversion + Events):**

**Dependency Inversion** (for queries):
```kotlin
// Defined in carbon-accounting-api
interface ShippingDetailsProvider {
    fun getShippingDetails(orderId: OrderId): ShippingDetails
}

interface EnergyConsumptionProvider {
    fun getWarehouseConsumption(warehouseId: WarehouseId, period: Period): EnergyConsumption
}

// Implemented in logistics-impl and energy-management-impl
```

**Eventual Consistency** (for updates):
```kotlin
// Carbon Accounting listens to events:
@JmsListener(destination = "order.shipped")
fun onOrderShipped(event: OrderShippedEvent) {
    // Recalculate carbon footprint now that we know actual shipping method
    calculateOrderCarbonUseCase.execute(event.orderId)
}

@JmsListener(destination = "energy.daily-consumption")
fun onDailyEnergyConsumption(event: DailyEnergyConsumptionEvent) {
    // Update warehouse carbon footprint
}
```

**Why Both Patterns?**
- **Dependency Inversion**: When carbon accounting needs to query logistics data synchronously
- **Events**: When logistics/energy data changes, carbon accounting eventually recalculates

---

### 3. Product Catalog (Supporting)

**Responsibility**: Manage product information and availability rules.

**Aggregates:**
- `Product` (root)
  - `ProductId`, `Name`, `Description`
  - `SustainabilityRating` (A+ to F)
  - `Weight`, `Dimensions`, `PackagingType`
  - `Certifications` (EU Ecolabel, Fair Trade, etc.)
  - `CountryRestrictions` (some products only in certain countries)

**Direct Dependencies** (No inversion needed):
```kotlin
// product-catalog-impl/build.gradle.kts
dependencies {
    implementation("com.ecoglobal.common:country")  // Generic domain - direct dependency OK
    implementation("com.ecoglobal.common:sustainability")  // Generic domain - OK
}
```

**Publishes Events:**
```kotlin
data class ProductAvailabilityChangedEvent(
    val productId: ProductId,
    val availableInCountries: Set<CountryCode>
)
```

---

### 4. Inventory Management (Supporting)

**Responsibility**: Track stock levels across warehouses.

**Aggregates:**
- `InventoryLevel` (root)
  - `ProductId`, `WarehouseId`, `Quantity`
  - `ReservedQuantity`, `AvailableQuantity`
  - `ReorderThreshold`

**Key Use Cases:**
- `ReserveStockUseCase`: Reserve items for an order (2-phase commit style)
- `CommitReservationUseCase`: Order paid, commit the reservation
- `ReleaseReservationUseCase`: Order cancelled, release stock
- `UpdateStockLevelUseCase`: Goods received, stock adjusted

**Eventual Consistency Pattern:**
```kotlin
// Inventory listens for order events
@JmsListener(destination = "order.placed")
fun onOrderPlaced(event: OrderPlacedEvent) {
    // Reserve stock for this order
    event.orderLines.forEach { line ->
        reserveStockUseCase.execute(
            productId = line.productId,
            warehouseId = event.assignedWarehouse,
            quantity = line.quantity,
            reservationId = event.orderId
        )
    }
}

@JmsListener(destination = "order.shipped")
fun onOrderShipped(event: OrderShippedEvent) {
    // Commit the reservation, reduce actual stock
    commitReservationUseCase.execute(event.orderId)
}
```

**Implements Interface for Core Domain:**
```kotlin
@Component
class InventoryAvailabilityProviderImpl(
    private val inventoryRepository: InventoryRepository
) : InventoryAvailabilityProvider {
    
    override fun checkAvailability(productId: ProductId, warehouseId: WarehouseId): Int {
        val inventory = inventoryRepository.find(productId, warehouseId)
        return inventory?.availableQuantity ?: 0
    }
}
```

**Why This Pattern?**
- Order Fulfillment (core) needs to CHECK availability → synchronous query via interface
- When order is PLACED/SHIPPED → update inventory asynchronously via events

---

### 5. Logistics & Shipping (Supporting)

**Responsibility**: Manage shipping, carriers, and actual carbon emissions.

**Aggregates:**
- `Shipment` (root)
  - `ShipmentId`, `OrderId`, `Carrier`, `TrackingNumber`
  - `Origin`, `Destination`, `Weight`
  - `EstimatedDelivery`, `ActualDelivery`
  - `ActualCarbonEmissions` (measured after shipment)

- `Carrier` (entity)
  - `CarrierId`, `Name`, `CarbonPerKm` by transport mode
  - `PricingRules` per country

**Key Use Cases:**
- `CreateShipmentUseCase`: Create shipment when order ready
- `UpdateTrackingUseCase`: Update from carrier webhook
- `RecordActualEmissionsUseCase`: Record actual carbon after delivery

**Country-Specific Logic:**
```kotlin
@Service
class ShippingRulesService(
    private val countryConfig: CountryConfigProvider
) {
    fun getAvailableCarriers(country: CountryCode): List<Carrier> {
        // Different carriers per country
        return when (country) {
            CountryCode.NL -> listOf(Carrier.POSTNL, Carrier.DHL)
            CountryCode.DE -> listOf(Carrier.DHL, Carrier.DPD)
            CountryCode.FR -> listOf(Carrier.CHRONOPOST, Carrier.DHL)
            else -> throw IllegalArgumentException("Country not supported")
        }
    }
    
    fun requiresCustomsDeclaration(from: CountryCode, to: CountryCode): Boolean {
        return countryConfig.isInEU(from) != countryConfig.isInEU(to)
    }
}
```

**Implements Interfaces:**
```kotlin
@Component
class ShippingCostProviderImpl : ShippingCostProvider {
    override fun calculateCost(from: WarehouseId, to: Address, weight: Weight): Money {
        // Complex pricing logic
    }
}

@Component  
class CarbonDataProviderImpl : CarbonDataProvider {
    override fun getCarbonPerKm(carrier: Carrier, transportMode: TransportMode): CarbonAmount {
        // Return carbon emissions data
    }
}
```

**Publishes Events:**
```kotlin
data class OrderShippedEvent(
    val orderId: OrderId,
    val shipmentId: ShipmentId,
    val carrier: Carrier,
    val estimatedCarbonEmissions: CarbonAmount
)

data class DeliveryCompletedEvent(
    val shipmentId: ShipmentId,
    val actualCarbonEmissions: CarbonAmount  // Actual measured data
)
```

---

### 6. Tax Compliance (Supporting)

**Responsibility**: Calculate taxes according to country rules.

**Aggregates:**
- `TaxCalculation` (root)
  - `OrderId`, `Country`, `TaxRate`, `TaxAmount`
  - `TaxBreakdown` (per line item)
  
- `Invoice` (root)
  - `InvoiceId`, `OrderId`, `CustomerId`
  - Legal requirements per country

**Country-Specific Logic:**
```kotlin
@Service
class TaxCalculationService(
    private val countryConfig: CountryConfigProvider
) {
    fun calculateTax(order: Order): TaxCalculation {
        val country = order.shippingAddress.country
        val vatRate = countryConfig.getVatRate(country)
        
        // Special rules per country
        val taxableAmount = when {
            country == CountryCode.NL && order.total > Money(150.0) -> {
                // NL: reduced VAT for certain eco products
                calculateWithReducedRate(order)
            }
            country == CountryCode.DE -> {
                // DE: different VAT for food items
                calculateGermanVat(order)
            }
            else -> order.subtotal
        }
        
        return TaxCalculation(
            orderId = order.id,
            country = country,
            taxRate = vatRate,
            taxAmount = taxableAmount * vatRate
        )
    }
}
```

**Direct Dependency:**
```kotlin
// No dependency inversion needed - supporting domain can depend on another supporting
dependencies {
    implementation(project(":order-fulfillment:order-fulfillment-api"))
    implementation("com.ecoglobal.common:country")
}
```

**Why No Inversion?**
- Tax Compliance (supporting) can directly depend on Order Fulfillment API
- Supporting → Core dependency is fine (not the wrong direction)

---

### 7. Energy Management (Supporting)

**Responsibility**: Monitor and optimize warehouse energy usage.

**Aggregates:**
- `EnergyConsumption` (root)
  - `WarehouseId`, `Period`, `SourceBreakdown`
  - `SolarKwh`, `WindKwh`, `GridKwh`
  - `CarbonIntensity` (varies by grid source)

- `BatteryStorage` (root)
  - `WarehouseId`, `Capacity`, `CurrentCharge`
  - Charge when energy cheap/clean, discharge when expensive

**Key Use Cases:**
- `RecordConsumptionUseCase`: Record hourly energy data
- `OptimizeOperationsUseCase`: Schedule energy-intensive tasks
  - Example: "Run warehouse robots at 2 PM when solar production peaks"
- `GenerateEnergyReportUseCase`: For carbon accounting

**Country-Specific Logic:**
```kotlin
@Service
class EnergyPricingService(
    private val countryConfig: CountryConfigProvider
) {
    fun getCurrentPrice(warehouseId: WarehouseId): Money {
        val country = getCountry(warehouseId)
        
        // Different markets per country
        return when (country) {
            CountryCode.NL -> {
                // Netherlands uses day-ahead market prices
                dayAheadMarketClient.getPrice("NL", LocalTime.now())
            }
            CountryCode.DE -> {
                // Germany has different tariff structure
                germanEnergyClient.getCurrentTariff()
            }
            CountryCode.FR -> {
                // France has nuclear base load, different dynamics
                frenchGridClient.getSpotPrice()
            }
        }
    }
}
```

**Implements Interface:**
```kotlin
@Component
class EnergyConsumptionProviderImpl : EnergyConsumptionProvider {
    override fun getWarehouseConsumption(warehouseId: WarehouseId, period: Period): EnergyConsumption {
        return repository.findByWarehouseAndPeriod(warehouseId, period)
    }
}
```

**Publishes Events:**
```kotlin
data class DailyEnergyConsumptionEvent(
    val warehouseId: WarehouseId,
    val date: LocalDate,
    val totalKwh: Double,
    val carbonIntensity: CarbonAmount
)
```

---

### 8. Payment Processing (Supporting)

**Responsibility**: Handle payments and refunds.

**Aggregates:**
- `Payment` (root)
  - `PaymentId`, `OrderId`, `Amount`, `Status`
  - `PaymentMethod`, `PaymentProvider`

**Direct Dependencies:**
```kotlin
dependencies {
    implementation(project(":order-fulfillment:order-fulfillment-api"))
    implementation("com.ecoglobal.common:country")
}
```

**Country-Specific Logic:**
```kotlin
// Different payment methods per country
fun getAvailablePaymentMethods(country: CountryCode): List<PaymentMethod> {
    return when (country) {
        CountryCode.NL -> listOf(IDEAL, CREDITCARD, PAYPAL)
        CountryCode.DE -> listOf(SOFORT, CREDITCARD, PAYPAL)
        CountryCode.FR -> listOf(CREDITCARD, PAYPAL)
    }
}
```

---

### 9. Country Configuration (Generic)

**Responsibility**: Centralize country-specific rules.

```kotlin
// common-country module
data class CountryRules(
    val code: CountryCode,
    val vatRate: BigDecimal,
    val currency: Currency,
    val shippingRegulations: ShippingRegulations,
    val requiredCertifications: List<Certification>,
    val holidays: List<LocalDate>
)

interface CountryConfigProvider {
    fun getVatRate(country: CountryCode): BigDecimal
    fun isInEU(country: CountryCode): Boolean
    fun getHolidays(country: CountryCode, year: Int): List<LocalDate>
}
```

**Why Generic?**
- No business logic, just configuration
- Could be replaced with external service
- Everyone can depend on it

---

## Integration Patterns Summary

### Pattern 1: Dependency Inversion (Synchronous Queries)

**When**: Core domain needs data from supporting domain

**Example**:
```
Order Fulfillment (CORE) needs inventory data
→ Define InventoryAvailabilityProvider in order-fulfillment-api
→ Implement in inventory-impl
→ Spring auto-wires the implementation
```

### Pattern 2: Eventual Consistency (Asynchronous State Changes)

**When**: State changes that cross bounded contexts

**Example**:
```
Order placed (Order Fulfillment)
→ Publishes OrderPlacedEvent
→ Inventory listens and reserves stock
→ Tax Compliance listens and calculates tax
→ Payment listens and initiates payment
```

### Pattern 3: Direct Dependencies (Supporting → Supporting)

**When**: Strategic design allows direct dependency

**Example**:
```
Tax Compliance (SUPPORTING) needs Order data
→ Direct dependency on order-fulfillment-api is fine
→ No dependency inversion needed
```

---

## Event Flow: Complete Order Journey

1. **Customer places order**
   ```
   POST /api/v1/orders
   → CreateOrderUseCase in Order Fulfillment
   ```

2. **Order Fulfillment optimizes fulfillment**
   ```
   Queries (via dependency inversion):
   - InventoryAvailabilityProvider.checkAvailability()
   - ShippingCostProvider.calculateCost()
   - CarbonDataProvider.getCarbonPerKm()
   
   Decides: Ship from NL warehouse via PostNL
   ```

3. **Order placed event published**
   ```
   OrderPlacedEvent published to JMS
   ```

4. **Multiple domains react (eventual consistency)**
   ```
   Inventory → reserves stock
   Tax Compliance → calculates VAT
   Payment → initiates payment  
   Carbon Accounting → estimates carbon footprint
   ```

5. **Payment confirmed**
   ```
   PaymentConfirmedEvent published
   → Order Fulfillment commits the order
   → Inventory commits the reservation
   ```

6. **Shipment created**
   ```
   Logistics creates shipment
   → OrderShippedEvent published
   ```

7. **Multiple domains react again**
   ```
   Inventory → reduces actual stock
   Carbon Accounting → updates with actual shipping method
   Customer → receives tracking email
   ```

8. **Delivery completed**
   ```
   Logistics records actual emissions
   → DeliveryCompletedEvent published
   → Carbon Accounting updates with actual data
   ```
