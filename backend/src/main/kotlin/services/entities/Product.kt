package services.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
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
    val price: Double,

    @field:NotNull
    val characteristic: String
)