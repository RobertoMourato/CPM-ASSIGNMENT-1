package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import services.entities.Payment
import java.util.*

@Repository
interface PaymentRepository: JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.uuid = ?1")
    fun findByUUID(uuid: UUID): List<Payment>

    @Query("select p from Payment p where p.token = ?1")
    fun findByToken(token: UUID): Optional<Payment>
}