package de.kgrupp.poc.grpc.consumer.configuration.grpc

import de.kgrupp.poc.grpc.consumer.configuration.log.generateRandomRequestId
import de.kgrupp.poc.grpc.consumer.configuration.log.getRequestId
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@GrpcGlobalClientInterceptor
class RequestIdClientInterceptor : ClientInterceptor {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        var requestId = getRequestId()
        if (requestId == null) {
            requestId = generateRandomRequestId()
            logger.warn("request-id was not initialized in an entrypoint, generating a new one")
        }
        return AddRequestIdSimpleForwarding(next.newCall(method, callOptions), requestId)
    }
}

class AddRequestIdSimpleForwarding<ReqT : Any?, RespT : Any?>(delegate: ClientCall<ReqT, RespT>, private val requestId: String) : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(delegate) {
    override fun start(responseListener: Listener<RespT>, headers: Metadata) {
        val fixedHeaders = Metadata()
        val key = Metadata.Key.of("request-id", Metadata.ASCII_STRING_MARSHALLER)
        fixedHeaders.put(key, requestId)
        headers.merge(fixedHeaders)
        super.start(responseListener, headers)
    }
}