package services.entities

import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "payments")
class Payment(
    @field:Id
    @field:NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @field:NotNull
    val token: UUID,

    @field:NotNull
    val customerUUID: UUID,

    @field:NotNull
    @OneToMany
    val items: List<BasketItem>,

    @field:NotNull
    val price: Double,

    @field:NotNull
    val date: String,

    @field:NotNull
    val time: String
)