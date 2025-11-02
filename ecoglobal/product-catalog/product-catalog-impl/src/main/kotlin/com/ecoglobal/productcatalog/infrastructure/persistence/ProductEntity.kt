package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.PackagingType
import com.ecoglobal.productcatalog.domain.model.ProductCategory
import com.ecoglobal.productcatalog.domain.model.SustainabilityRating
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.util.UUID

@Table("products")
data class ProductEntity(
    @Id
    val id: UUID,
    val name: String,
    val category: ProductCategory,
    val sustainabilityRating: SustainabilityRating,
    val price: BigDecimal,
    @MappedCollection(idColumn = "product_id")
    val certifications: Set<ProductCertificationEntity>,
    val packaging: PackagingType
)
