package services.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import services.entities.Payment
import java.util.*

@Repository
interface PaymentRepository: JpaRepository<Payment, Long> {
}