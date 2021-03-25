package de.kgrupp.poc.grpc.provider.configuration.log

import io.grpc.ForwardingServerCallListener.SimpleForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import java.util.UUID

const val LOG_REQUEST_ID = "request_id"

fun generateRandomRequestId() = UUID.randomUUID().toString()

@GrpcGlobalServerInterceptor
@Order(1)
class GrpcRequestIdLogEnhancerServerInterceptor : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(call: ServerCall<ReqT, RespT>, headers: Metadata, next: ServerCallHandler<ReqT, RespT>): ServerCall.Listener<ReqT> {
        val requestId = headers.get(Metadata.Key.of("request-id", Metadata.ASCII_STRING_MARSHALLER)) ?: generateRandomRequestId()

        MDC.put(LOG_REQUEST_ID, requestId)
        return LogEnhancerServerCallListener(next.startCall(call, headers))
    }

}

private class LogEnhancerServerCallListener<ReqT>(delegate: ServerCall.Listener<ReqT>) : SimpleForwardingServerCallListener<ReqT>(delegate) {

    /**
     * Is called when a call ends "normally" (success or exception)
     */
    override fun onComplete() {
        super.onComplete()
        MDC.remove(LOG_REQUEST_ID)
    }

    /**
     * Cancellations can be caused by timeouts, explicit cancellation by the client, network errors, etc.
     */
    override fun onCancel() {
        super.onCancel()
        MDC.remove(LOG_REQUEST_ID)
    }

}