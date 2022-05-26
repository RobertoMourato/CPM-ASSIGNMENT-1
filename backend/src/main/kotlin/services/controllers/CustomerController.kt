package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.dtos.RegisterCustomer
import services.dtos.CustomerUUID
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
    fun create(@RequestBody customerInfo: RegisterCustomer): CustomerUUID {
        val customerUUID = CustomerUUID(UUID.randomUUID())

        val customer = Customer(customerUUID.uuid, customerInfo.publicKey, customerInfo.name, customerInfo.address, customerInfo.nif, customerInfo.cardType, customerInfo.cardNumber, customerInfo.cardValidity)

        repository.save(customer)

        return customerUUID
    }
}