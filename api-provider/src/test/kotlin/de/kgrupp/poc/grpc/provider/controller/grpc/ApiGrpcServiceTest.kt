package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.GetDataRequest
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

@GrpcTest
class ApiGrpcServiceTest {

    @Autowired
    private lateinit var apiGrpcService: ApiGrpcService

    @Test
    fun getData() {
        val request = buildRequest("my-id")

        val reply = runBlocking { apiGrpcService.getData(request) }

        assertThat(reply.data).contains("my-id")
    }

    @Test
    fun getDataFailsOnInvalidRequestIdMissing() {
        val request = buildRequest("")

        val exception = assertThrows<StatusRuntimeException> { runBlocking { apiGrpcService.getData(request) } }

        assertThat(exception.message).contains("id is not set")
    }
}

fun buildRequest(id: String): GetDataRequest = GetDataRequest.newBuilder().setId(id).build()