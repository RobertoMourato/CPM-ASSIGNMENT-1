package services.entities

class Payment(
        val id: Long = 0
                lateinit var uuid: UUID
                lateinit var token: UUID
        val items: List<Item>
        var price: Double = 0.0
                lateinit var date: String
                lateinit var time: String
)