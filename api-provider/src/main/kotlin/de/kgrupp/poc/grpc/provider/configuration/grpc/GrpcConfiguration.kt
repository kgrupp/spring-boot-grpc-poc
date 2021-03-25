package de.kgrupp.poc.grpc.provider.configuration.grpc

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfiguration {
    @Value("\${service.grpc.basic-auth.user}")
    val basicAuthUser: String = ""

    @Value("\${service.grpc.basic-auth.password}")
    val basicAuthPassword: String = ""
}