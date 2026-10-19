package io.github.stscoundrel.revalidator.revalidators

import io.github.stscoundrel.revalidator.enums.DictionaryType
import io.github.stscoundrel.revalidator.repository.RevalidatorConfig
import io.github.stscoundrel.revalidator.service.HTTPClient
import io.github.stscoundrel.revalidator.service.LogService
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class RevalidatorParameterTests {
    private val httpClient = mock(HTTPClient::class.java)
    private val logService = mock(LogService::class.java)
    private val revalidator = Revalidator(
        RevalidatorConfig(DictionaryType.OLD_NORSE, "https://example.invalid", "test", 100, 10),
        httpClient,
        logService
    )

    @Test
    fun rejectsInvalidResolvedParametersWithoutMakingRequests() {
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(null, null, 0, null) }
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(null, null, -1, null) }
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(null, null, null, -1) }
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(-1, null, null, null) }
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(100, null, null, null) }
        assertThrows(IllegalArgumentException::class.java) { revalidator.revalidate(101, null, null, null) }
        verifyNoInteractions(httpClient, logService)
    }

    @Test
    fun largeBatchDoesNotOverflowWordRange() {
        `when`(httpClient.get(anyString())).thenReturn(200)
        revalidator.revalidate(1, 100, Int.MAX_VALUE, 0)
        verify(httpClient).get("https://example.invalid/api/revalidate?secret=test&start=1&end=2147483647")
        verifyNoMoreInteractions(httpClient)
    }
}
