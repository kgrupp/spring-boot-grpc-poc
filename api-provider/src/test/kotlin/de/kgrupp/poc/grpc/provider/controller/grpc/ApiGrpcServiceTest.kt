package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.GetDataRequest
import io.grpc.internal.testing.StreamRecorder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@GrpcTest
class ApiGrpcServiceTest {

    @Autowired
    private lateinit var apiGrpcService: ApiGrpcService

    @Test
    fun getData() {
        val request = buildRequest("my-id")
        val responseObserver: StreamRecorder<GetDataReply> = StreamRecorder.create()

        apiGrpcService.getData(request, responseObserver)

        assertNull(responseObserver.error)
        val reply = responseObserver.values.assertSizeOneAndGet()

        assertThat(reply.data).contains("my-id")
    }

    @Test
    fun getDataFailsOnInvalidRequestIdMissing() {
        val request = buildRequest("")
        val responseObserver: StreamRecorder<GetDataReply> = StreamRecorder.create()

        apiGrpcService.getData(request, responseObserver)

        assertNotNull(responseObserver.error)
        assertThat(responseObserver.error?.message).contains("id is not set")
    }
}

fun buildRequest(id: String): GetDataRequest = GetDataRequest.newBuilder().setId(id).build()

fun <T> List<T>.assertSizeOneAndGet(): T {
    assertEquals(1, this.size)
    return this[0]
}