package io.github.stscoundrel.revalidator.controller

import io.github.stscoundrel.revalidator.service.RevalidatorService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RevalidateController::class)
class RevalidateControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var revalidatorService: RevalidatorService

    @Test
    fun requiresSecretAuth() {
        mockMvc.perform(
            get("/api")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized())

        mockMvc.perform(
            get("/api/old-norse")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized())

        mockMvc.perform(
            get("/api/old-icelandic")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized())

        mockMvc.perform(
            get("/api/old-norwegian")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized())

        mockMvc.perform(
            get("/api/old-swedish")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isUnauthorized())
    }
}