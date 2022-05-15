import services.entities.Item
import services.entities.Customer
import services.entities.Payment


class PaymentInfo(
        val customer:Customer
        val items: List<Item>,
        val payment: Payment)