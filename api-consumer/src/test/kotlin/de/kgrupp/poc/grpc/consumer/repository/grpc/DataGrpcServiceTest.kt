package de.kgrupp.poc.grpc.consumer.repository.grpc

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.V1ApiServiceGrpcKt
import de.kgrupp.poc.grpc.consumer.testutils.IntegrationTest
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
class DataGrpcServiceTest {

    private var apiServiceStub: V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub = mockApiServiceStub()

    @Autowired
    private lateinit var dataGrpcService: DataGrpcService

    @BeforeEach
    fun setUp() {
        apiServiceStub = mockApiServiceStub()
        dataGrpcService.setApiServiceStub(apiServiceStub)
    }

    @Test
    fun happyPath() = runBlocking<Unit> {
        Mockito.`when`(apiServiceStub.getData(argThat { "my-id" == this.id })).thenReturn(GetDataReply.newBuilder().setData("some-data").build())

        val data = dataGrpcService.loadData("my-id")

        assertEquals("my-id", data.id)
        assertEquals("some-data", data.value)
        verify(apiServiceStub, times(1))
            .withCallCredentials(any())
        verify(apiServiceStub, times(1))
            .getData(argThat { this.id == "my-id" })
    }

    @Test
    fun errorPathUnavailableService() = runBlocking<Unit> {
        Mockito.`when`(apiServiceStub.getData(argThat { "my-id" == this.id })).thenThrow(
            Status.UNAVAILABLE.asRuntimeException()
        )

        val exception = assertThrows<StatusRuntimeException> {
            dataGrpcService.loadData("my-id")
        }

        assertEquals(Status.UNAVAILABLE, exception.status)
        verify(apiServiceStub, times(1))
            .withCallCredentials(any())
        verify(apiServiceStub, times(1))
            .getData(argThat { this.id == "my-id" })
    }
}

fun mockApiServiceStub(): V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub {
    val mock = mock(V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub::class.java)
    Mockito.`when`(mock.withCallCredentials(argThat { true })).thenReturn(mock)
    return mock
}