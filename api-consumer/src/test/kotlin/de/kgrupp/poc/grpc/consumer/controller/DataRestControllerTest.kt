package de.kgrupp.poc.grpc.consumer.controller

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import de.kgrupp.poc.grpc.consumer.configuration.rest.RestConfiguration
import de.kgrupp.poc.grpc.consumer.service.DataService
import de.kgrupp.poc.grpc.consumer.testutils.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.env.Environment
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class DataRestControllerTest {

    @MockBean
    lateinit var dataService: DataService

    @Autowired
    lateinit var configuration: RestConfiguration

    @Autowired
    lateinit var env: Environment

    private fun boundedWebTestClient() = WebTestClient.bindToServer().baseUrl("http://localhost:${env.getProperty("local.server.port")}")

    @Test
    fun happyPath() {
        boundedWebTestClient()
            .defaultHeaders { it.setBasicAuth(configuration.basicAuthUser, configuration.basicAuthPassword) }
            .build()
            .get().uri("$DATA_PATH/my-id").exchange()
        verify(dataService, times(1)).get(eq("my-id"))
    }

    @Test
    fun `404 NOT_FOUND if id is missing`() {
        boundedWebTestClient()
            .defaultHeaders { it.setBasicAuth(configuration.basicAuthUser, configuration.basicAuthPassword) }
            .build()
            .get().uri(DATA_PATH).exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `401 NOT_FOUND header is missing`() {
        boundedWebTestClient()
            .build()
            .get().uri("$DATA_PATH/my-id").exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `401 NOT_FOUND header is invalid`() {
        boundedWebTestClient()
            .defaultHeaders { it.setBasicAuth("invalid", "invalid") }
            .build()
            .get().uri("$DATA_PATH/my-id").exchange()
            .expectStatus().isUnauthorized
    }
}