package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.entities.Customer
import services.repositories.CustomerRepository
import services.utilities.CustomerInfo
import services.utilities.CustomerUUID
import services.utilities.GenerateKeys
import services.utilities.StringToPrivateKey
import services.utilities.StringToPublicKey
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController {
    @Autowired
    lateinit var repository: CustomerRepository

    @GetMapping
    fun index(): List<Customer>{
        return repository.findAll()
    }

    @PostMapping
    fun create(@RequestBody customerInfo: CustomerInfo): CustomerUUID {
        val customerUUID = CustomerUUID(UUID.randomUUID())
        val customer = Customer(customerUUID.uuid, customerInfo.publicKey, customerInfo.name, customerInfo.address, customerInfo.nif, customerInfo.cardType, customerInfo.cardNumber, customerInfo.cardValidity)

        //println(GenerateKeys())

        repository.save(customer)
        return customerUUID
    }
}