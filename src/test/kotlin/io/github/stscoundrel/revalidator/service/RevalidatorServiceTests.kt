package io.github.stscoundrel.revalidator.service

import io.github.stscoundrel.revalidator.repository.RevalidatorConfigRepository
import io.github.stscoundrel.revalidator.repository.SecretRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class RevalidatorServiceTests {
    private val httpClient: HTTPClient = mock(HTTPClient::class.java)
    private val secretRepository: SecretRepository = mock(SecretRepository::class.java)
    private val revalidatorConfigRepository: RevalidatorConfigRepository = RevalidatorConfigRepository(secretRepository)
    private val revalidatorService =
        RevalidatorService(configRepository = revalidatorConfigRepository, httpClient = httpClient)

    @Test
    fun revalidatesGivenDictionary() {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")

        // HTTP responses -> always 200.
        `when`(httpClient.get(anyString())).thenReturn(200)

        // Use Old Norse as test case.
        revalidatorService.revalidateOldNorse()

        // We should've received HTTP calls to all relevant revalidation urls.
        val invocations = mockingDetails(httpClient).invocations

        val requestedUrls = invocations.map { invocation ->
            invocation.arguments[0] as String
        }

        // Expected amount of calls = total words diviced into 250 sized batches.
        assertEquals(141, requestedUrls.size)

        val expectedUrls = listOf(
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=0&end=250",
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=250&end=500",
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=34750&end=3500",
        )

        for (expectedUrl in expectedUrls) {
            assertTrue(expectedUrls.contains(expectedUrl))
        }
    }

    @Test
    fun retriesFailedRevalidations() {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")

        // HTTP responses -> always 408 to indicate timeout
        `when`(httpClient.get(anyString())).thenReturn(408)

        // Use Old Icelandic as test case.
        revalidatorService.revalidateOldIcelandic()

        val invocations = mockingDetails(httpClient).invocations

        val requestedUrls = invocations.map { invocation ->
            invocation.arguments[0] as String
        }

        // We should've received HTTP calls to all revalidation urls + retry for each of them.
        assertEquals(200, requestedUrls.size)
    }
}