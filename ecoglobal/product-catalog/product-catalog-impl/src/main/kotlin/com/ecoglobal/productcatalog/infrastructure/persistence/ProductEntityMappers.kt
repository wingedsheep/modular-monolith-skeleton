package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.Certification
import com.ecoglobal.productcatalog.domain.model.Product
import com.ecoglobal.productcatalog.domain.model.ProductId
import com.ecoglobal.productcatalog.domain.model.ProductName

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = this.id.value,
    name = this.name.value,
    category = this.category,
    sustainabilityRating = this.sustainabilityRating,
    price = this.price,
    certifications = this.certifications.map { ProductCertificationEntity(it) }.toSet(),
    packaging = this.packaging
)

fun ProductEntity.toDomain(): Product = Product(
    id = ProductId(this.id),
    name = ProductName(this.name),
    category = this.category,
    sustainabilityRating = this.sustainabilityRating,
    price = this.price,
    certifications = this.certifications.map { it.certification }.toSet(),
    packaging = this.packaging
)
