package com.ecoglobal.productcatalog.domain.model

import java.util.UUID

@JvmInline
value class ProductId(val value: UUID) {
    companion object {
        fun generate() = ProductId(UUID.randomUUID())
    }
}
