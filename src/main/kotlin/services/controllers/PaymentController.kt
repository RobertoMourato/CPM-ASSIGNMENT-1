package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.dtos.PaymentToken
import services.entities.Payment
import services.repositories.CustomerRepository
import services.repositories.PaymentRepository
import services.utilities.GenerateSignature
import services.utilities.VerifySignature
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/payments")
class PaymentController {
    @Autowired
    lateinit var paymentRepository: PaymentRepository
    @Autowired
    lateinit var customerRepository: CustomerRepository

    @PostMapping
    fun create(@RequestBody payment: Payment, @RequestHeader("Client-Id") uuid: UUID, @RequestHeader("Request-Time") requestTime: String, @RequestHeader("Signature") signature: String): PaymentToken {
        val dummySignature = GenerateSignature("/payments", uuid.toString(), requestTime, "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAkPfH5PP+BrXuFvcroQ5er/VASWlAfhAs8OvQfIiYZyUenHVpuThO2jNxyPL95YNpZC4hMCw/Hdqv8GFYTqO3dQIDAQABAkB+27XbTwCu0qnFAwr052RIym81TpuXNYR+V32cXNQw/WMoL4OmAF8JtxoqoX8xMUmdHbp68We0RkMMN9bms5HBAiEAyZBMlvxS8c4OXTVVLJCX8lokGFWEkq7xPBxkJgpZuRkCIQC4HpC0t2V8/8L3Vv2KZggsNLmknJKcQf9EBLiXZYiQvQIhAMQawol7gO+FfPAufWnYXB0MmmWafluAHXBQZxejpluxAiBN40A78slQE7IaiAZBoeAbM0LFTCcJSzH9SDH+eclH0QIhAKYkQq4f/MEGST8OWM5zD7EEaOYFOH1qe/dijcVB87cB", payment.toString())
        val signatureVerifier = VerifySignature("/payments", uuid.toString(), requestTime, "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJD3x+Tz/ga17hb3K6EOXq/1QElpQH4QLPDr0HyImGclHpx1abk4Ttozccjy/eWDaWQuITAsPx3ar/BhWE6jt3UCAwEAAQ==", payment.toString(), dummySignature.signature)

        println(payment)
        println(payment.toString())

        if(!signatureVerifier.isValidSignature){
            return PaymentToken("Unauthorized")
        }

        if(Math.random() <= 0.05) {
            return PaymentToken("Invalid Operation")
        }

        payment.uuid = uuid

        val paymentToken = UUID.randomUUID()
        payment.token = paymentToken

        payment.price = 123.0

        val sdfDate = SimpleDateFormat("dd/M/yyyy")
        val date = sdfDate.format(Date())
        payment.date = date.toString()

        val sdfTime = SimpleDateFormat("hh:mm:ss")
        val time = sdfTime.format(Date())
        payment.time = time.toString()

        this.paymentRepository.save(payment)

        return PaymentToken(paymentToken.toString())
    }
}