package de.kgrupp.poc.grpc.consumer.configuration.grpc

import io.grpc.CallCredentials
import net.devh.boot.grpc.client.security.CallCredentialsHelper.basicAuth
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfiguration {

    @Value("\${service.api-provider.grpc.basic-auth.user}")
    val basicAuthUser: String = ""

    @Value("\${service.api-provider.grpc.basic-auth.password}")
    val basicAuthPassword: String = ""

    fun getCredentials(): CallCredentials = basicAuth(basicAuthUser, basicAuthPassword)
}