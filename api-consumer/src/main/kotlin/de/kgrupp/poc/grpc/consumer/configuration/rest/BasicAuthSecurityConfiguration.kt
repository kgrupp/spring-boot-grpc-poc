package de.kgrupp.poc.grpc.consumer.configuration.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint

@Configuration
@Order(2)
class BasicAuthSecurityConfiguration(private val configuration: RestConfiguration) : WebSecurityConfigurerAdapter() {

    companion object {
        const val role = "user"
        const val realm = "user realm"
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser(configuration.basicAuthUser)
            .password(encoder().encode(configuration.basicAuthPassword))
            .roles(role)
    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint? {
        val entryPoint = BasicAuthenticationEntryPoint()
        entryPoint.realmName = realm
        return entryPoint
    }

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/api/**")
            .authorizeRequests().anyRequest().hasRole(role)
            .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .csrf { it.disable() }
            .cors()
    }
}