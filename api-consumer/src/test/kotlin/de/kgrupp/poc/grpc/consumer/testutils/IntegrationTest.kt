package de.kgrupp.poc.grpc.consumer.testutils

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application.test.yml"])
annotation class IntegrationTest