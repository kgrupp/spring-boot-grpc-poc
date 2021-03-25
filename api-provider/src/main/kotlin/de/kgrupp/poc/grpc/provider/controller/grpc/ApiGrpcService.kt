package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.GetDataRequest
import de.kgrupp.poc.grpc.V1ApiServiceGrpcKt
import de.kgrupp.poc.grpc.provider.model.Data
import de.kgrupp.poc.grpc.provider.service.DataService
import io.grpc.Status
import kotlinx.coroutines.slf4j.MDCContext
import net.devh.boot.grpc.server.service.GrpcService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@GrpcService
class ApiGrpcService(private val dataService: DataService) : V1ApiServiceGrpcKt.V1ApiServiceCoroutineImplBase(MDCContext()) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override suspend fun getData(request: GetDataRequest): GetDataReply {
        logger.info("getData for id '${request.id}'")
        if (request.id.isEmpty()) {
            throw Status.INVALID_ARGUMENT.withDescription("id is not set").asRuntimeException()
        }
        val data = dataService.get(request.id)

        return data.toProtobuf()
    }
}

private fun Data.toProtobuf(): GetDataReply = GetDataReply.newBuilder().setData(this.value).build()