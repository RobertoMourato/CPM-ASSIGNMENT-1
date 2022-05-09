package services.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import services.entities.Product
import services.repositories.ProductRepository

@RestController
@RequestMapping("/products")
class ProductController {
    @Autowired
    lateinit var repository: ProductRepository

    @GetMapping
    fun getAll(): List<Product>{
        return this.repository.findAll()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable("id") id: Long): Product {
        return this.repository.getById(id)
    }
}