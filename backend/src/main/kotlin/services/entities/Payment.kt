package services.entities

import java.util.UUID
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "payments")
class Payment(
    @OneToMany(cascade = [CascadeType.ALL])
    @JoinColumn(name = "pi_fk", referencedColumnName = "id")
    val items: List<Item>
){
    @field:Id
    @field:NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
    lateinit var uuid: UUID
    lateinit var token: UUID
    var price: Double = 0.0
    lateinit var date: String
    lateinit var time: String
}