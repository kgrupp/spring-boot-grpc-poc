package de.kgrupp.poc.grpc.consumer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApiConsumerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ApiConsumerApplication::class.java, *args)
}
