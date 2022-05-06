package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import services.entities.Product
import services.repositories.ProductRepository

@RestController
@RequestMapping("/products")
class ProductController {
    @Autowired
    lateinit var repository: ProductRepository

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Product {
        return this.repository.getById(id)
    }
}