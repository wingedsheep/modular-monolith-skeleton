package com.ecoglobal.productcatalog.domain.model

import java.math.BigDecimal

data class Product(
    val id: ProductId,
    val name: ProductName,
    val category: ProductCategory,
    val sustainabilityRating: SustainabilityRating,
    val price: BigDecimal,
    val certifications: Set<Certification> = emptySet(),
    val packaging: PackagingType
) {
    companion object {
        fun create(
            name: ProductName,
            category: ProductCategory,
            sustainabilityRating: SustainabilityRating,
            price: BigDecimal,
            certifications: Set<Certification>,
            packaging: PackagingType
        ): Product {
            require(price > BigDecimal.ZERO) { "Price must be positive." }
            return Product(
                id = ProductId.generate(),
                name = name,
                category = category,
                sustainabilityRating = sustainabilityRating,
                price = price,
                certifications = certifications,
                packaging = packaging
            )
        }
    }
}
