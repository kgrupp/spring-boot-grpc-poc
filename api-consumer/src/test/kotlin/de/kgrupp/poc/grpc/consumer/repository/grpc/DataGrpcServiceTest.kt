package de.kgrupp.poc.grpc.consumer.repository.grpc

import com.nhaarman.mockitokotlin2.any
import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.V1ApiServiceGrpc
import de.kgrupp.poc.grpc.consumer.testutils.IntegrationTest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.argThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class DataGrpcServiceTest {

    private var apiServiceBlockingStub: V1ApiServiceGrpc.V1ApiServiceBlockingStub = mockApiServiceBlockingStub()

    @Autowired
    private lateinit var dataGrpcService: DataGrpcService

    @BeforeEach
    fun setUp() {
        apiServiceBlockingStub = mockApiServiceBlockingStub()
        dataGrpcService.setApiServiceBlockingStub(apiServiceBlockingStub)
    }

    @Test
    fun happyPath() {
        Mockito.`when`(apiServiceBlockingStub.getData(argThat { "my-id" == it.id })).thenReturn(GetDataReply.newBuilder().setData("some-data").build())

        val data = dataGrpcService.loadData("my-id")

        assertEquals("my-id", data.id)
        assertEquals("some-data", data.value)
        verify(apiServiceBlockingStub, times(1))
            .withCallCredentials(any())
        verify(apiServiceBlockingStub, times(1))
            .getData(argThat { it.id == "my-id" })
    }

    @Test
    fun errorPathUnavailableService() {
        Mockito.`when`(apiServiceBlockingStub.getData(argThat { "my-id" == it.id })).thenThrow(
            Status.UNAVAILABLE.asRuntimeException()
        )

        val exception = assertThrows<StatusRuntimeException> {
            dataGrpcService.loadData("my-id")
        }

        assertEquals(Status.UNAVAILABLE, exception.status)
        verify(apiServiceBlockingStub, times(1))
            .withCallCredentials(any())
        verify(apiServiceBlockingStub, times(1))
            .getData(argThat { it.id == "my-id" })
    }
}

fun mockApiServiceBlockingStub(): V1ApiServiceGrpc.V1ApiServiceBlockingStub {
    val mock = mock(V1ApiServiceGrpc.V1ApiServiceBlockingStub::class.java)
    Mockito.`when`(mock.withCallCredentials(argThat { true })).thenReturn(mock)
    return mock
}