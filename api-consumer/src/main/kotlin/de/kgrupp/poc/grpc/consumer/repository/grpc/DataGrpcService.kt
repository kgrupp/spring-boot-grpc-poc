package de.kgrupp.poc.grpc.consumer.repository.grpc

import com.google.common.annotations.VisibleForTesting
import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.GetDataRequest
import de.kgrupp.poc.grpc.V1ApiServiceGrpcKt
import de.kgrupp.poc.grpc.consumer.configuration.grpc.GrpcConfiguration
import de.kgrupp.poc.grpc.consumer.model.Data
import net.devh.boot.grpc.client.inject.GrpcClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DataGrpcService(private val configuration: GrpcConfiguration) {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @GrpcClient("api-provider")
    private lateinit var apiServiceStub: V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub

    @VisibleForTesting
    fun setApiServiceStub(apiServiceStub: V1ApiServiceGrpcKt.V1ApiServiceCoroutineStub) {
        this.apiServiceStub = apiServiceStub
    }

    suspend fun loadData(id: String): Data {
        logger.info("Loading data via gRPC for id '$id'")
        val request = GetDataRequest.newBuilder().setId(id).build()

        val reply = apiServiceStub.withCallCredentials(configuration.getCredentials()).getData(request)
        logger.info("Loaded data via gRPC for id '$id'")
        return reply.toData(id)
    }
}

private fun GetDataReply.toData(id: String): Data = Data(id, this.data)
