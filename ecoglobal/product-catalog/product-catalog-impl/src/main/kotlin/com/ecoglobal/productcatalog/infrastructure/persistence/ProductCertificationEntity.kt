package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.Certification
import org.springframework.data.relational.core.mapping.Table

@Table("product_certifications")
data class ProductCertificationEntity(
    val certification: Certification
)
