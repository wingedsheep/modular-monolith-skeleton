# Worldview: Domain Knowledge as Code

## What is Worldview?

Worldview is a testing and onboarding strategy where realistic domain data is defined as code alongside your bounded contexts. Each domain module has a `-worldview` submodule containing realistic instantiations of domain concepts, reference data, and typical scenarios that developers can use for testing, learning, and demonstrating the system.

## Structure

```
domain-name/
├── domain-name-api/          # Public interfaces
├── domain-name-impl/         # Implementation
└── domain-name-worldview/    # Realistic domain data
    ├── WorldviewProduct.kt
    ├── WorldviewCustomer.kt
    ├── ProductBuilder.kt
    └── insertWorldviewData()
```

The worldview module contains:
- **Reference data**: Realistic products, customers, warehouses with actual names, weights, prices
- **Builders**: Test data builders with sensible defaults based on worldview data
- **Scenarios**: Pre-defined realistic scenarios (e.g., "bulk order from German customer")
- **Insert functions**: Methods to populate databases with worldview data for testing

## Why Worldview?

### 1. **Domain Knowledge Transfer**
New developers joining the project can browse worldview modules to understand what the domain actually looks like. Instead of abstract `Product(id=1, name="test")`, they see `Product(id=1, name="SolarMax 450W Panel", weight=22.5kg, sustainabilityRating=A_PLUS)`. This teaches the domain vocabulary and constraints.

### 2. **Realistic Testing**
Tests using worldview data catch bugs that abstract test data misses. When you test with "EcoFlow 500Wh Battery" weighing 6kg shipping from Netherlands to France, you're testing real-world scenarios, not just code paths.

### 3. **Consistent Test Data**
All tests reference the same worldview data. When a Cucumber scenario mentions "the SolarMax 450W Panel", both the test author and reader know exactly which product with which properties. This eliminates test data duplication and confusion.

### 4. **Living Documentation**
Worldview serves as executable documentation. Product owners and domain experts can review `WorldviewProduct.kt` to verify the system models their domain correctly. It's documentation that can't drift from the code because it *is* code.

### 5. **Demo and Development**
When running the application locally, developers can insert worldview data to have a realistic working system immediately. No need to manually create test accounts, products, and orders—just run `insertWorldviewData()`.

## Best Practices

### Make it Realistic
Don't use "Product A" and "Product B". Use "SolarMax 450W Monocrystalline Panel" and "EcoContainer 500ml Stainless Steel". Include realistic weights, dimensions, prices, and certifications.

### Cover Edge Cases
Include worldview items that test boundary conditions:
- Very heavy products (solar panels, batteries)
- Very light products (reusable bags)
- High-value items (triggers certain tax rules)
- Products restricted to certain countries

### Keep it Stable
Worldview data should change rarely. Tests depend on it. When you add "SolarMax 450W Panel" with productId=1, it should always be productId=1.

### Document Scenarios
Name worldview scenarios clearly:
```kotlin
object WorldviewScenarios {
    val smallOrderNLCustomer = // Single item, local delivery
    val bulkOrderDECustomer = // 50+ items, cross-border
    val expressLowCarbonOrder = // Conflicting requirements
}
```

### Use in Cucumber
Cucumber feature files reference worldview data by name:
```gherkin
Given the product "SolarMax 450W Panel"
And the customer "Hans Müller from Berlin"
When the customer places an order for 5 panels
```

## Implementation

Worldview modules have no implementation dependencies—they only depend on the domain API:
```kotlin
// domain-name-worldview/build.gradle.kts
dependencies {
    api(project(":domain-name:domain-name-api"))
    api("com.company.common:country")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
}
```

This keeps worldview focused purely on domain knowledge, not technical concerns.

## Conclusion

Worldview transforms your test suite from abstract code exercises into realistic domain simulations. It serves as onboarding material, living documentation, and a shared vocabulary for the entire team. By investing in realistic worldview data upfront, you create a foundation that pays dividends throughout the project's lifetime.