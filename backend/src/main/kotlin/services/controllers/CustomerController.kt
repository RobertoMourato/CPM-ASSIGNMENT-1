package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.dtos.CustomerInfo
import services.dtos.ResponseUUID
import services.entities.Customer
import services.repositories.CustomerRepository
import java.util.*

@RestController
@RequestMapping("/customers")
class CustomerController {
    @Autowired
    lateinit var repository: CustomerRepository

    @GetMapping
    fun getAll(): List<Customer>{
        return this.repository.findAll()
    }

    @PostMapping
    fun create(@RequestBody customerInfo: CustomerInfo): ResponseUUID {
        val customerUUID = ResponseUUID(UUID.randomUUID())
        val customer = Customer(customerUUID.uuid, customerInfo.publicKey, customerInfo.name, customerInfo.address, customerInfo.nif, customerInfo.cardType, customerInfo.cardNumber, customerInfo.cardValidity)

        //println(GenerateKeys())

        repository.save(customer)
        return customerUUID
    }
}