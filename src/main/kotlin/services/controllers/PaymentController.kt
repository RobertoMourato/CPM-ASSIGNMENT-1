package services.controllers

import org.springframework.web.bind.annotation.*
import services.repositories.PaymentRepository

@RestController
@RequestMapping("/payments")
class PaymentController {
    lateinit var repository: PaymentRepository
}