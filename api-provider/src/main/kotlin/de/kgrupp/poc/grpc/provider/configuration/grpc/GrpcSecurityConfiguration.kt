package de.kgrupp.poc.grpc.provider.configuration.grpc

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader
import net.devh.boot.grpc.server.security.check.AccessPredicate
import net.devh.boot.grpc.server.security.check.AccessPredicateVoter
import net.devh.boot.grpc.server.security.check.GrpcSecurityMetadataSource
import net.devh.boot.grpc.server.security.check.ManualGrpcSecurityMetadataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.vote.UnanimousBased
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import java.util.ArrayList

@Configuration
class GrpcSecurityConfiguration {

    @Bean
    fun accessDecisionManager(): AccessDecisionManager {
        val voters: MutableList<AccessDecisionVoter<*>> = ArrayList()
        voters.add(AccessPredicateVoter())
        return UnanimousBased(voters)
    }

    @Bean
    fun grpcSecurityMetadataSource(): GrpcSecurityMetadataSource {
        val source = ManualGrpcSecurityMetadataSource()
        source.setDefault(AccessPredicate.authenticated())
        return source
    }

    @Bean
    fun authenticationManager(grpcBasicAuthenticationProvider: GrpcBasicAuthenticationProvider): AuthenticationManager {
        val providers: MutableList<AuthenticationProvider> = ArrayList()
        providers.add(grpcBasicAuthenticationProvider)
        return ProviderManager(providers)
    }

    @Bean
    fun authenticationReader(): GrpcAuthenticationReader {
        return BasicGrpcAuthenticationReader()
    }
}