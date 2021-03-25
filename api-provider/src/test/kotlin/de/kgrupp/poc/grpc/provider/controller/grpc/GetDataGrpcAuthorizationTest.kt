package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.V1ApiServiceGrpc
import de.kgrupp.poc.grpc.provider.configuration.grpc.GrpcConfiguration
import io.grpc.Status
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
    private lateinit var apiService: V1ApiServiceGrpc.V1ApiServiceBlockingStub

    @Test
    fun getDataRejectsWhenAuthorizationIsMissing() {
        val request = buildRequest("my-id")

        val exception = assertThrows<Throwable> {
            apiService.getData(request)
            return
        }

        assertThat(exception.message).contains(Status.Code.UNAUTHENTICATED.name)
    }

    @Test
    fun getDataRejectsWhenAuthorizationIsInvalid() {
        val request = buildRequest("my-other-id")

        val exception = assertThrows<Throwable> {
            apiService.withCallCredentials(basicAuth("invalid", "invalid"))
                .getData(request)
            return
        }

        assertThat(exception.message).contains(Status.Code.UNAUTHENTICATED.name)
    }

    @Test
    fun getDataHappyPath() {
        val request = buildRequest("my-id")

        val reply = apiService.withCallCredentials(basicAuth(configuration.basicAuthUser, configuration.basicAuthPassword))
                .getData(request)

        assertThat(reply.data).contains("my-id")
    }
}