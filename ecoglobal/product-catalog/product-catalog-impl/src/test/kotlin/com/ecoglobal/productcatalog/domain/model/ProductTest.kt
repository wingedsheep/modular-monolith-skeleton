package com.ecoglobal.productcatalog.domain.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class ProductTest {

    @Test
    fun `should create product with valid data`() {
        val product = Product.create(
            name = ProductName("Solar Panel"),
            category = ProductCategory.SOLAR_PANELS,
            sustainabilityRating = SustainabilityRating.A_PLUS,
            price = BigDecimal("100.00"),
            certifications = setOf(Certification.EU_ECOLABEL),
            packaging = PackagingType.RECYCLABLE_CARDBOARD
        )

        assert(product.name.value == "Solar Panel")
        assert(product.price == BigDecimal("100.00"))
    }

    @Test
    fun `should throw exception when price is negative`() {
        assertThrows<IllegalArgumentException> {
            Product.create(
                name = ProductName("Solar Panel"),
                category = ProductCategory.SOLAR_PANELS,
                sustainabilityRating = SustainabilityRating.A_PLUS,
                price = BigDecimal("-100.00"),
                certifications = setOf(Certification.EU_ECOLABEL),
                packaging = PackagingType.RECYCLABLE_CARDBOARD
            )
        }
    }

    @Test
    fun `should throw exception when price is zero`() {
        assertThrows<IllegalArgumentException> {
            Product.create(
                name = ProductName("Solar Panel"),
                category = ProductCategory.SOLAR_PANELS,
                sustainabilityRating = SustainabilityRating.A_PLUS,
                price = BigDecimal("0.00"),
                certifications = setOf(Certification.EU_ECOLABEL),
                packaging = PackagingType.RECYCLABLE_CARDBOARD
            )
        }
    }

    @Test
    fun `should throw exception when product name is blank`() {
        assertThrows<IllegalArgumentException> {
            ProductName(" ")
        }
    }
}
