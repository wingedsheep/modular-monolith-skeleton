package com.ecoglobal.productcatalog.infrastructure.persistence

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface ProductRepositoryJdbc : CrudRepository<ProductEntity, UUID>
