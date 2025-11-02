package com.ecoglobal.productcatalog.infrastructure.persistence

import com.ecoglobal.productcatalog.domain.model.Product
import com.ecoglobal.productcatalog.domain.model.ProductId
import com.ecoglobal.productcatalog.domain.repository.ProductRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
class ProductRepositoryImpl(private val jdbcRepository: ProductRepositoryJdbc) : ProductRepository {

    override fun <S : Product> save(entity: S): S {
        val savedEntity = jdbcRepository.save(entity.toEntity())
        return savedEntity.toDomain() as S
    }

    override fun <S : Product> saveAll(entities: Iterable<S>): Iterable<S> {
        val savedEntities = jdbcRepository.saveAll(entities.map { it.toEntity() })
        return savedEntities.map { it.toDomain() as S }
    }

    override fun findById(id: ProductId): Optional<Product> {
        return jdbcRepository.findById(id.value).map { it.toDomain() }
    }

    override fun existsById(id: ProductId): Boolean {
        return jdbcRepository.existsById(id.value)
    }

    override fun findAll(): Iterable<Product> {
        return jdbcRepository.findAll().map { it.toDomain() }
    }

    override fun findAllById(ids: Iterable<ProductId>): Iterable<Product> {
        return jdbcRepository.findAllById(ids.map { it.value }).map { it.toDomain() }
    }

    override fun count(): Long {
        return jdbcRepository.count()
    }

    override fun deleteById(id: ProductId) {
        jdbcRepository.deleteById(id.value)
    }

    override fun delete(entity: Product) {
        jdbcRepository.delete(entity.toEntity())
    }

    override fun deleteAllById(ids: Iterable<ProductId>) {
        jdbcRepository.deleteAllById(ids.map { it.value })
    }

    override fun deleteAll(entities: Iterable<Product>) {
        jdbcRepository.deleteAll(entities.map { it.toEntity() })
    }

    override fun deleteAll() {
        jdbcRepository.deleteAll()
    }
}
