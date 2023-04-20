package io.github.stscoundrel.revalidator.config

import io.github.stscoundrel.revalidator.security.SecretAuthInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecretAuthInterceptor()).addPathPatterns("/api/**")
    }
}