package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.V1ApiServiceGrpcKt
import de.kgrupp.poc.grpc.provider.configuration.grpc.GrpcConfiguration
import io.grpc.Status
import kotlinx.coroutines.runBlocking
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration
import net.devh.boot.grpc.client.inject.GrpcClient
import net.devh.boot.grpc.client.security.CallCredentialsHelper.basicAuth
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest

@GrpcTest
@SpringBootTest(
    properties = [
        "grpc.server.inProcessName=test", // Enable inProcess server
        "grpc.client.inProcess.address=in-process:test" // Configure the client to connect to the inProcess server
    ]
)
@ImportAutoConfiguration(GrpcClientAutoConfiguration::class)
class GetDataGrpcAuthorizationTest {

    @Autowired
    private lateinit var configuration: GrpcConfiguration

    @GrpcClient("inProcess")
    private lateinit var apiService: V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub

    @Test
    fun getDataRejectsWhenAuthorizationIsMissing() {
        val request = buildRequest("my-id")

        val exception = assertThrows<Throwable> {
            runBlocking { apiService.getData(request) }
        }

        assertThat(exception.message).contains(Status.Code.UNAUTHENTICATED.name)
    }

    @Test
    fun getDataRejectsWhenAuthorizationIsInvalid() {
        val request = buildRequest("my-other-id")

        val exception = assertThrows<Throwable> {
            runBlocking {
                apiService.withCallCredentials(basicAuth("invalid", "invalid"))
                    .getData(request)
            }
        }

        assertThat(exception.message).contains(Status.Code.UNAUTHENTICATED.name)
    }

    @Test
    fun getDataHappyPath() {
        val request = buildRequest("my-id")

        val reply = runBlocking {
            apiService.withCallCredentials(basicAuth(configuration.basicAuthUser, configuration.basicAuthPassword))
                .getData(request)
        }

        assertThat(reply.data).contains("my-id")
    }
}