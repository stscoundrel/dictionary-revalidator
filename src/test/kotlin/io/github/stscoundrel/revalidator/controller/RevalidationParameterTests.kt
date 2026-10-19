package io.github.stscoundrel.revalidator.controller

import io.github.stscoundrel.revalidator.service.RevalidatorService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class RevalidationParameterTests {
    private val service = mock(RevalidatorService::class.java)
    // Exercise controller validation without authentication, rate limiting, or production services.
    private val mockMvc = MockMvcBuilders.standaloneSetup(RevalidateController(service)).build()
    private val dictionaries = listOf("old-norse", "old-icelandic", "old-swedish", "old-norwegian", "old-danish")

    @Test
    fun rejectsInvalidParametersBeforeStartingAnyWork() {
        val invalidParameters = listOf(
            mapOf("batch" to "0"),
            mapOf("batch" to "-1"),
            mapOf("retries" to "-1"),
            mapOf("start" to "-1"),
            mapOf("end" to "-1"),
            mapOf("end" to "0"),
            mapOf("start" to "100", "end" to "100"),
            mapOf("start" to "200", "end" to "100")
        )
        for (dictionary in dictionaries) {
            for (parameters in invalidParameters) {
                val request = get("/api/revalidate/$dictionary")
                parameters.forEach { (name, value) -> request.param(name, value) }
                mockMvc.perform(request)
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").isNotEmpty())
            }
        }
        verifyNoInteractions(service)
    }

    @Test
    fun rejectsInvalidParametersForAllDictionariesBeforeLaunchingBackgroundWork() {
        for ((name, value) in listOf("batch" to "0", "batch" to "-1", "retries" to "-1")) {
            mockMvc.perform(get("/api/revalidate/all").param(name, value))
                .andExpect(status().isBadRequest())
        }
        verifyNoInteractions(service)
    }

    @Test
    fun acceptsDefaultsAndValidBoundaryValues() {
        for (dictionary in dictionaries) {
            mockMvc.perform(get("/api/revalidate/$dictionary"))
                .andExpect(status().isOk())
            mockMvc.perform(
                get("/api/revalidate/$dictionary")
                    .param("start", "0").param("end", "1")
                    .param("batch", "1").param("retries", "0")
            ).andExpect(status().isOk())
        }
        mockMvc.perform(get("/api/revalidate/all").param("batch", "1").param("retries", "0"))
            .andExpect(status().isOk())
    }
}
