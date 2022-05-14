package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import services.entities.Customer
import java.util.*

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {

    @Query("select c from Customer c where c.uuid = ?1")
    fun findByUUID(uuid: UUID): Optional<Customer>

}