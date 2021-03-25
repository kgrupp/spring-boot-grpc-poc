package de.kgrupp.poc.grpc.consumer.configuration.log

import org.slf4j.MDC
import java.util.UUID

const val LOG_REQUEST_ID = "request_id"

fun generateRandomRequestId() = UUID.randomUUID().toString()

fun <T> logForRequestId(requestId: String, block: () -> T): T {
    MDC.putCloseable(LOG_REQUEST_ID, requestId).use {
        return block()
    }
}

fun getRequestId(): String? = try {
    MDC.get(LOG_REQUEST_ID)
} catch (e: IllegalArgumentException) {
    null
}