package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import services.entities.Product

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
}