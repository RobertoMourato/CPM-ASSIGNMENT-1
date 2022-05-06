package services.entities

import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "customers")
class Customer(
    @field:Id
    @field:NotNull
    val uuid: UUID,

    @field:NotNull
    var publicKey: String,

    @field:NotNull
    var name: String,

    @field:NotNull
    var address: String,

    @field:NotNull
    @field: Digits(integer = 9, fraction = 0)
    var nif: Int,

    @field:NotNull
    var cardType: String,

    @field:NotNull
    @field: Digits(integer = 16, fraction = 0)
    var cardNumber: Long,

    @field:NotNull
    var cardValidity: String
)