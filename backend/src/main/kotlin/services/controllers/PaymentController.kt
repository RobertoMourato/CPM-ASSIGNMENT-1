package services.controllers

import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.dtos.*
import services.entities.Customer
import services.entities.Payment
import services.repositories.CustomerRepository
import services.repositories.PaymentRepository
import services.utilities.VerifySignature
import services.utilities.encryptMessage
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.EntityNotFoundException

@RestController
@RequestMapping("/payments")
class PaymentController {
    @Autowired
    lateinit var paymentRepository: PaymentRepository

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @GetMapping("/receipt")
    fun get(@RequestHeader("Token") token: UUID): ReceiptInfo {
        val payment = this.paymentRepository.findByToken(token).orElseThrow { EntityNotFoundException() }
        val paymentInfoJson = Gson().toJson(payment, Payment::class.java)
        val paymentInfo = Gson().fromJson(paymentInfoJson, PaymentInfo::class.java)

        val customer = this.customerRepository.findByUUID(payment.uuid).orElseThrow { EntityNotFoundException() }
        val customerInfoJson = Gson().toJson(customer, Customer::class.java)
        val customerInfo = Gson().fromJson(customerInfoJson, CustomerInfo::class.java)

        payment.token = UUID.fromString("00000000-0000-0000-0000-000000000000")
        this.paymentRepository.save(payment)

        return ReceiptInfo(customerInfo, paymentInfo)
    }

    @GetMapping("/past")
    fun getAll(
        @RequestHeader("Client-Id") uuid: UUID,
        @RequestHeader("Request-Time") requestTime: String,
        @RequestHeader("Signature") signature: String
    ): PastPayments {
        val customer = this.customerRepository.findByUUID(uuid).orElseThrow { EntityNotFoundException() }
        val signatureVerifier =
            VerifySignature("/payments", uuid.toString(), requestTime, customer.publicKey, "", signature)

        /*if(!signatureVerifier.isValidSignature){
            return PaymentToken("Unauthorized")
        }*/

        val pastPayments = this.paymentRepository.findByUUID(uuid)
        val customerNewUUID = UUID.randomUUID()

        var listOfPastPayments: MutableList<PaymentInfo> = mutableListOf()
        for (pastPayment in pastPayments) {
            val paymentInfoJson = Gson().toJson(pastPayment, Payment::class.java)
            val paymentInfo = Gson().fromJson(paymentInfoJson, PaymentInfo::class.java)
            listOfPastPayments.add(listOfPastPayments.size, paymentInfo)
//            pastPayment.apply {
//                this.uuid = customerNewUUID
//            }
        }
//        this.paymentRepository.saveAll(pastPayments)

//        customer.apply {
//            this.uuid = customerNewUUID
//        }
//        this.customerRepository.save(customer)

        println(customerNewUUID.toString())
        return PastPayments(encryptMessage(customerNewUUID.toString(), customer.publicKey), listOfPastPayments)
    }

    @PostMapping
    fun create(
        @RequestBody items: String,
        @RequestHeader("Client-Id") uuid: UUID,
        @RequestHeader("Request-Time") requestTime: String,
        @RequestHeader("Signature") signature: String
    ): PaymentToken {
        //val dummySignature = GenerateSignature("/payments", uuid.toString(), requestTime, "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAkPfH5PP+BrXuFvcroQ5er/VASWlAfhAs8OvQfIiYZyUenHVpuThO2jNxyPL95YNpZC4hMCw/Hdqv8GFYTqO3dQIDAQABAkB+27XbTwCu0qnFAwr052RIym81TpuXNYR+V32cXNQw/WMoL4OmAF8JtxoqoX8xMUmdHbp68We0RkMMN9bms5HBAiEAyZBMlvxS8c4OXTVVLJCX8lokGFWEkq7xPBxkJgpZuRkCIQC4HpC0t2V8/8L3Vv2KZggsNLmknJKcQf9EBLiXZYiQvQIhAMQawol7gO+FfPAufWnYXB0MmmWafluAHXBQZxejpluxAiBN40A78slQE7IaiAZBoeAbM0LFTCcJSzH9SDH+eclH0QIhAKYkQq4f/MEGST8OWM5zD7EEaOYFOH1qe/dijcVB87cB", items)

        val customer = this.customerRepository.findByUUID(uuid).orElseThrow { EntityNotFoundException() }
        val signatureVerifier =
            VerifySignature("/payments", uuid.toString(), requestTime, customer.publicKey, items, signature)

        if(!signatureVerifier.isValidSignature){
            return PaymentToken("Unauthorized")
        }

        if (Math.random() <= 0.05) {
            return PaymentToken("Invalid Operation")
        }

        val payment = Gson().fromJson(items, Payment::class.java)

        payment.uuid = uuid

        val paymentToken = UUID.randomUUID()
        payment.token = paymentToken

        var price = 0.0
        for (basketItem in payment.items) {
            price += basketItem.price * basketItem.quantity
        }
        payment.price = price

        val sdfDate = SimpleDateFormat("dd/M/yyyy")
        val date = sdfDate.format(Date())
        payment.date = date.toString()

        val sdfTime = SimpleDateFormat("HH:mm:ss")
        val time = sdfTime.format(Date())
        payment.time = time.toString()

        this.paymentRepository.save(payment)

        println(paymentToken.toString())
        return PaymentToken(encryptMessage(paymentToken.toString(), customer.publicKey))
    }
}