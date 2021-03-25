package de.kgrupp.poc.grpc.consumer.configuration.log

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(1)
class RequestIdLogEnhancerFilter : Filter {

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        when(request) {
            is HttpServletRequest -> {
                val requestId = request.getHeader("request-id") ?: generateRandomRequestId()
                logForRequestId(requestId) {
                    chain.doFilter(request, response)
                }
            }
            else -> chain.doFilter(request, response)
        }
    }
}
