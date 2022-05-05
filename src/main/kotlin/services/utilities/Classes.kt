package services.utilities

import java.util.*

class CustomerInfo (val publicKey: String, val name: String, val address: String, val nif: Int, val cardType: String, val cardNumber: Long, val cardValidity: String)

class CustomerUUID (val uuid: UUID)