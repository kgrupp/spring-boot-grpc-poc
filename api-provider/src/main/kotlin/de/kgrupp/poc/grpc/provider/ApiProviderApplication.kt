package de.kgrupp.poc.grpc.provider

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApiProviderApplication

fun main(args: Array<String>) {
    SpringApplication.run(ApiProviderApplication::class.java, *args)
}
