package com.ecoglobal.productcatalog.domain.repository

import com.ecoglobal.productcatalog.domain.model.Product
import com.ecoglobal.productcatalog.domain.model.ProductId
import org.springframework.data.repository.CrudRepository

interface ProductRepository : CrudRepository<Product, ProductId>
