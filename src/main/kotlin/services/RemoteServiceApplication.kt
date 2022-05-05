package services

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RemoteServiceApplication

fun main(args: Array<String>) {
	runApplication<RemoteServiceApplication>(*args)
}
