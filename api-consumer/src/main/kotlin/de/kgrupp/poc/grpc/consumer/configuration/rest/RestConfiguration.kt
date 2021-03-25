package de.kgrupp.poc.grpc.consumer.configuration.rest

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class RestConfiguration {

    @Value("\${service.rest.basic-auth.user}")
    val basicAuthUser: String = ""

    @Value("\${service.rest.basic-auth.password}")
    val basicAuthPassword: String = ""
}