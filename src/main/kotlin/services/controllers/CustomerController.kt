package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.entities.Customer
import services.repositories.CustomerRepository
import services.utilities.CustomerInfo
import services.utilities.CustomerUUID
import services.utilities.KeysToString
import services.utilities.PublicKeyGenerator
import java.security.KeyPair
import java.security.KeyPairGenerator
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

        //test keys
        val keyPair = generateKeyPair()
        println(keyPair.public)
        val keys = KeysToString(keyPair)
        val publicKey = PublicKeyGenerator(keys.publicKeyString)
        println(publicKey.publicKey)

        repository.save(customer)
        return customerUUID
    }
}

fun generateKeyPair(): KeyPair {
    val generator = KeyPairGenerator.getInstance("RSA")
    generator.initialize(512)
    return generator.genKeyPair()
}