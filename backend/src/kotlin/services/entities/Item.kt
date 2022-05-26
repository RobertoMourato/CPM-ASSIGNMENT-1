package services.entities

class Item(
        val id: Long,
        val quantity: Int,
        val model: String,
        val make: String,
        val price: Double,
        val characteristic: String
)