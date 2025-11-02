package com.ecoglobal.productcatalog.domain.model

@JvmInline
value class ProductName(val value: String) {
    init {
        require(value.isNotBlank()) { "ProductName cannot be blank." }
    }
}
