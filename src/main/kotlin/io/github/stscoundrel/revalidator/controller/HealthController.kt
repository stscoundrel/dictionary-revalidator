package io.github.stscoundrel.revalidator.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


data class HealthCheckResponse(val status: String)

@RestController
@RequestMapping("/health")
class HealthController {

    @GetMapping("/")
    fun healthCheck(): HealthCheckResponse {
        return HealthCheckResponse(status = "Healthy as a goat")
    }
}