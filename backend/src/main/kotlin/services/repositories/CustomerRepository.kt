package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import services.entities.Customer
import java.util.*

@Repository
interface CustomerRepository: JpaRepository<Customer, UUID> {
}