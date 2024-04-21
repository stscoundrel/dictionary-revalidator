package io.github.stscoundrel.revalidator.service

import io.github.stscoundrel.revalidator.enums.DictionaryType
import io.github.stscoundrel.revalidator.repository.RevalidatorConfigRepository
import io.github.stscoundrel.revalidator.revalidators.Revalidator
import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class RevalidatorService(
    val configRepository: RevalidatorConfigRepository,
    val httpClient: HTTPClient,
    val logService: LogService
) {
    private val revalidators: MutableMap<DictionaryType, Revalidator?> = mutableMapOf(
        DictionaryType.OLD_NORSE to null,
        DictionaryType.OLD_ICELANDIC to null,
        DictionaryType.OLD_SWEDISH to null,
        DictionaryType.OLD_NORWEGIAN to null,
    )

    private fun getDictionaryRevalidator(dictionaryType: DictionaryType): Revalidator {
        if (revalidators[dictionaryType] == null) {
            val config = configRepository.getConfig(dictionaryType)
            revalidators[dictionaryType] =
                Revalidator(config = config, httpClient = httpClient, logService = logService)
        }

        return revalidators[dictionaryType]!!
    }

    suspend fun revalidateOldNorse(
        start: Int? = null,
        end: Int? = null,
        batchSize: Int? = null,
        retriesCount: Int? = null
    ) {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_NORSE)
        withContext(Dispatchers.IO) {
            dictionary.revalidate(start = start, end = end, customBatchSize = batchSize, retriesCount = retriesCount)
        }
    }

    suspend fun revalidateOldIcelandic(
        start: Int? = null,
        end: Int? = null,
        batchSize: Int? = null,
        retriesCount: Int? = null
    ) {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_ICELANDIC)
        withContext(Dispatchers.IO) {
            dictionary.revalidate(start = start, end = end, customBatchSize = batchSize, retriesCount = retriesCount)
        }
    }

    suspend fun revalidateOldSwedish(
        start: Int? = null,
        end: Int? = null,
        batchSize: Int? = null,
        retriesCount: Int? = null
    ) {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_SWEDISH)
        withContext(Dispatchers.IO) {
            dictionary.revalidate(start = start, end = end, customBatchSize = batchSize, retriesCount = retriesCount)
        }
    }

    suspend fun revalidateOldNorwegian(
        start: Int? = null,
        end: Int? = null,
        batchSize: Int? = null,
        retriesCount: Int? = null
    ) {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_NORWEGIAN)
        withContext(Dispatchers.IO) {
            dictionary.revalidate(start = start, end = end, customBatchSize = batchSize, retriesCount = retriesCount)
        }
    }

    suspend fun revalidateOldDanish(
        start: Int? = null,
        end: Int? = null,
        batchSize: Int? = null,
        retriesCount: Int? = null
    ) {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_DANISH)
        withContext(Dispatchers.IO) {
            dictionary.revalidate(start = start, end = end, customBatchSize = batchSize, retriesCount = retriesCount)
        }
    }

    suspend fun revalidateAll(batchSize: Int? = null, retriesCount: Int? = null) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val tasks = listOf(
                async { revalidateOldNorse(batchSize = batchSize, retriesCount = retriesCount) },
                async { revalidateOldIcelandic(batchSize = batchSize, retriesCount = retriesCount) },
                async { revalidateOldNorwegian(batchSize = batchSize, retriesCount = retriesCount) },
                async { revalidateOldSwedish(batchSize = batchSize, retriesCount = retriesCount) },
                async { revalidateOldDanish(batchSize = batchSize, retriesCount = retriesCount) },
            )
            tasks.awaitAll()
        }
    }
}