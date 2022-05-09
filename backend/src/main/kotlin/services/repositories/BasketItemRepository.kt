package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import services.entities.Item

@Repository
interface BasketItemRepository: JpaRepository<Item, Long> {
}