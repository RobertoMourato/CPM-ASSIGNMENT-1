package services.entities

import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "items")
class BasketItem(
    @field:Id
    @field:NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @ManyToOne
    @field:NotNull
    val product: Product,

    @field:NotNull
    val quantity: Int
)