package io.github.stscoundrel.revalidator.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HealthController::class)
class HealthControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun returns200OkHealth() {
        mockMvc.perform(
            get("/health/")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
    }
}