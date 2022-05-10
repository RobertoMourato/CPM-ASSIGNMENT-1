package services.dtos

import services.entities.Item
import java.util.*

class PaymentInfo (val items: List<Item>, val price: Double = 0.0, val date: String, val time: String)