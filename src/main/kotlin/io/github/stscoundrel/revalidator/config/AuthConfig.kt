package io.github.stscoundrel.revalidator.config

import io.github.stscoundrel.revalidator.security.SecretAuthInterceptor
import io.github.stscoundrel.revalidator.security.RateLimitInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfig(
    @Value("\${revalidator.rate-limit.max-requests:6}") maxRequests: Int,
    @Value("\${revalidator.rate-limit.window-seconds:60}") windowSeconds: Long
) : WebMvcConfigurer {
    private val rateLimitInterceptor = RateLimitInterceptor(maxRequests, windowSeconds)

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/**").order(0)
        registry.addInterceptor(SecretAuthInterceptor()).addPathPatterns("/api/**").order(1)
    }
}
