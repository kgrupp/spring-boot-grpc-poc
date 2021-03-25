package de.kgrupp.poc.grpc.provider.configuration.grpc

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class GrpcBasicAuthenticationProvider(private val configuration: GrpcConfiguration) : AuthenticationProvider {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        val name: String = authentication.name
        val password: String = authentication.credentials.toString()

        logger.info("try to authenticate '$name'...")
        return if (name == configuration.basicAuthUser
                && password == configuration.basicAuthPassword) {
            logger.info("successfully authenticated '$name'...")
            UsernamePasswordAuthenticationToken(name, password, ArrayList())
        } else {
            logger.info("'$name' is NOT authenticated")
            null
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}