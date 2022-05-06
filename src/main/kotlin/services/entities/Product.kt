package services.entities

import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "products")
class Product(
    @field:Id
    @field:NotNull
    val id: Long,

    @field:NotNull
    val model: String,

    @field:NotNull
    val make: String,

    @field:NotNull
    val price: Float,

    @field:NotNull
    val characteristic: String
)