package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.dtos.PaymentToken
import services.entities.Payment
import services.repositories.PaymentRepository
import java.text.SimpleDateFormat
import java.util.*


@RestController
@RequestMapping("/payments")
class PaymentController {
    @Autowired
    lateinit var repository: PaymentRepository

    @PostMapping
    fun create(@RequestBody payment: Payment): PaymentToken {
        if(Math.random() <= 0.05) {
            return PaymentToken("Invalid Operation!")
        }

        val paymentToken = UUID.randomUUID()
        payment.token = paymentToken

        payment.price = 123.0

        val sdfDate = SimpleDateFormat("dd/M/yyyy")
        val date = sdfDate.format(Date())
        payment.date = date.toString()

        val sdfTime = SimpleDateFormat("hh:mm:ss")
        val time = sdfTime.format(Date())
        payment.time = time.toString()

        this.repository.save(payment)

        return PaymentToken(paymentToken.toString())
    }
}