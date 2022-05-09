package services.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "items")
@JsonIgnoreProperties(ignoreUnknown = true)
class Item(
    @field:Id
    @field:NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @field:NotNull
    val quantity: Int,

    @field:NotNull
    val model: String,

    @field:NotNull
    val make: String,

    @field:NotNull
    val price: Double,

    @field:NotNull
    val characteristic: String
)