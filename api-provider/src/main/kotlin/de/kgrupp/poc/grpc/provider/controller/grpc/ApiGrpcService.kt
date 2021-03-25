package de.kgrupp.poc.grpc.provider.controller.grpc

import de.kgrupp.poc.grpc.GetDataReply
import de.kgrupp.poc.grpc.GetDataRequest
import de.kgrupp.poc.grpc.V1ApiServiceGrpc
import de.kgrupp.poc.grpc.provider.model.Data
import de.kgrupp.poc.grpc.provider.service.DataService
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class ApiGrpcService(private val dataService: DataService) : V1ApiServiceGrpc.V1ApiServiceImplBase() {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun getData(request: GetDataRequest, responseObserver: StreamObserver<GetDataReply>) {
        try {
            logger.info("getData for id '${request.id}'")
            if (request.id.isEmpty()) {
                throw Status.INVALID_ARGUMENT.withDescription("id is not set").asRuntimeException()
            }
            val data = dataService.get(request.id)

            responseObserver.onNext(data.toProtobuf())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            responseObserver.onError(e)
            logger.error("Request failed with error", e)
        }
    }
}

private fun Data.toProtobuf(): GetDataReply = GetDataReply.newBuilder().setData(this.value).build()