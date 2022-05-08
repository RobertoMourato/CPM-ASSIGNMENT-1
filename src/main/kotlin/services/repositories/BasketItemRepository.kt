package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import services.entities.BasketItem

@Repository
interface BasketItemRepository: JpaRepository<BasketItem, Long> {
}