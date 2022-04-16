package edu.backend.taskapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.annotation.Resource

@Profile("test")
@Configuration
@EnableWebSecurity
class OpenSecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests().antMatchers("/**").permitAll()
    }
}

@Profile("!test")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class JwtSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${url.unsecure}")
    val URL_UNSECURE: String? = null

    @Value("\${url.user.signup}")
    val URL_SIGNUP: String? = null

    @Resource
    private val userDetailsService: UserDetailsService? = null

    @Bean
    @Throws(NoSuchAlgorithmException::class)
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/".plus(URL_UNSECURE).plus("/**")).permitAll()
            .antMatchers(HttpMethod.POST, URL_SIGNUP).permitAll()
            .antMatchers("/**").authenticated()
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager()))
            .addFilter(JwtAuthorizationFilter(authenticationManager()))
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.applyPermitDefaultValues()
        config.addExposedHeader(HttpHeaders.AUTHORIZATION)
        config.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE")
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
