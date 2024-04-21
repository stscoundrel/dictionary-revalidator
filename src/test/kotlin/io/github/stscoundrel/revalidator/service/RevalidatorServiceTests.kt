package io.github.stscoundrel.revalidator.service

import io.github.stscoundrel.revalidator.repository.RevalidatorConfigRepository
import io.github.stscoundrel.revalidator.repository.SecretRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class RevalidatorServiceTests {
    private val httpClient: HTTPClient = mock(HTTPClient::class.java)
    private val logService: LogService = mock(LogService::class.java)
    private val secretRepository: SecretRepository = mock(SecretRepository::class.java)
    private val revalidatorConfigRepository: RevalidatorConfigRepository = RevalidatorConfigRepository(secretRepository)
    private val revalidatorService =
        RevalidatorService(configRepository = revalidatorConfigRepository, httpClient = httpClient, logService = logService)

    @Test
    fun revalidatesGivenDictionary() = runBlocking {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")
        `when`(secretRepository.getOldDanishSecret()).thenReturn("secret5")

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
        assertEquals(177, requestedUrls.size)

        val expectedUrls = listOf(
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=0&end=200",
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=200&end=400",
            "https://cleasby-vigfusson-dictionary.vercel.app/api/revalidate?secret=secret1&start=34800&end=35000",
        )

        for (expectedUrl in expectedUrls) {
            assertTrue(requestedUrls.contains(expectedUrl))
        }
    }

    @Test
    fun revalidatesDictionaryWithCustomRange() = runBlocking {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")

        // HTTP responses -> always 200.
        `when`(httpClient.get(anyString())).thenReturn(200)

        // Use Old Swedish as test case.
        revalidatorService.revalidateOldSwedish(100, 200)

        // We should've received HTTP calls to expected revalidation urls.
        val invocations = mockingDetails(httpClient).invocations

        val requestedUrls = invocations.map { invocation ->
            invocation.arguments[0] as String
        }

        // Short range should result in two batches.
        assertEquals(2, requestedUrls.size)

        val expectedUrls = listOf(
            "https://old-swedish-dictionary.vercel.app/api/revalidate?secret=secret4&start=100&end=150",
            "https://old-swedish-dictionary.vercel.app/api/revalidate?secret=secret4&start=150&end=200",
        )

        for (expectedUrl in expectedUrls) {
            assertTrue(requestedUrls.contains(expectedUrl))
        }
    }

    @Test
    fun revalidatesDictionaryWithCustomBatchSize() = runBlocking {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")

        // HTTP responses -> always 200.
        `when`(httpClient.get(anyString())).thenReturn(200)

        // Use Old Norwegian as test case.
        revalidatorService.revalidateOldNorwegian(batchSize = 20000)

        // We should've received HTTP calls to expected revalidation urls.
        val invocations = mockingDetails(httpClient).invocations

        val requestedUrls = invocations.map { invocation ->
            invocation.arguments[0] as String
        }

        // Huge bath size should result in only three calls.
        assertEquals(3, requestedUrls.size)

        val expectedUrls = listOf(
            "https://old-norwegian-dictionary.vercel.app/api/revalidate?secret=secret3&start=0&end=20000",
            "https://old-norwegian-dictionary.vercel.app/api/revalidate?secret=secret3&start=20000&end=40000",
            "https://old-norwegian-dictionary.vercel.app/api/revalidate?secret=secret3&start=40000&end=60000",
        )

        for (expectedUrl in expectedUrls) {
            assertTrue(requestedUrls.contains(expectedUrl))
        }
    }

    @Test
    fun retriesFailedRevalidationsWithDefaultAmount() = runBlocking {
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
        assertEquals(240, requestedUrls.size)
    }

    @Test
    fun retriesFailedRevalidationsWithCustomAmount() = runBlocking {
        // Repo secrets.
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")

        // HTTP responses -> always 408 to indicate timeout
        `when`(httpClient.get(anyString())).thenReturn(408)

        // Allow three retries. Use huge batch size for easier calculations.
        revalidatorService.revalidateOldIcelandic(batchSize = 50000, retriesCount = 3)

        val invocations = mockingDetails(httpClient).invocations

        val requestedUrls = invocations.map { invocation ->
            invocation.arguments[0] as String
        }

        // We should've received:
        // - HTTP calls to revalidation url. Batch is big enough to have all in one call.
        // - 3 retries.
        assertEquals(4, requestedUrls.size)

        // Alternative request with smaller batch & larger retries.
        revalidatorService.revalidateOldIcelandic(batchSize = 25000, retriesCount = 10)

        val invocations2 = mockingDetails(httpClient).invocations

        val requestedUrls2 = invocations2.map { invocation ->
            invocation.arguments[0] as String
        }

        // We should've received:
        // - 2 HTTP calls to revalidation url. Batch size requires two calls.
        // - 10 retries for both of failed requests.
        assertEquals(22, requestedUrls2.size - requestedUrls.size)
    }
}