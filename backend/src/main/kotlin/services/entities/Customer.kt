package services.entities

import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.*

@Entity
@Table(name = "customers")
class Customer(
    @field:NotNull
    var uuid: UUID,

    @field:NotNull
    @field:Lob
    val publicKey: String,

    @field:NotNull
    val name: String,

    @field:NotNull
    val address: String,

    @field:NotNull
    @field: Digits(integer = 9, fraction = 0)
    val nif: Int,

    @field:NotNull
    val cardType: String,

    @field:NotNull
    @field: Digits(integer = 16, fraction = 0)
    val cardNumber: Long,

    @field:NotNull
    val cardValidity: String
){
    @field:Id
    @field:NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}