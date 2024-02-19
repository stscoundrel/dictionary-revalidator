package io.github.stscoundrel.revalidator.controller

import io.github.stscoundrel.revalidator.enum.DictionaryType
import io.github.stscoundrel.revalidator.service.RevalidatorService
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


data class RevalidationResponse(val statuses: Map<DictionaryType, Boolean>)

@RestController
@RequestMapping("/api")
class RevalidateController(val revalidatorService: RevalidatorService) {

    @GetMapping("/old-norse")
    fun oldNorse(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse {
        revalidatorService.revalidateOldNorse(start, end, batchSize, retries)
        return RevalidationResponse(mapOf(DictionaryType.OLD_NORSE to true))
    }

    @GetMapping("/old-icelandic")
    fun oldIcelandic(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse {
        revalidatorService.revalidateOldIcelandic(start, end, batchSize, retries)
        return RevalidationResponse(mapOf(DictionaryType.OLD_ICELANDIC to true))
    }

    @GetMapping("/old-swedish")
    fun oldSwedish(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse {
        revalidatorService.revalidateOldSwedish(start, end, batchSize, retries)
        return RevalidationResponse(mapOf(DictionaryType.OLD_SWEDISH to true))
    }

    @GetMapping("/old-norwegian")
    fun oldNorwegian(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse {
        revalidatorService.revalidateOldNorwegian(start, end, batchSize, retries)
        return RevalidationResponse(mapOf(DictionaryType.OLD_NORWEGIAN to true))
    }

    @GetMapping("/")
    fun all(): RevalidationResponse {
        val coroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val tasks = listOf(
                async { revalidatorService.revalidateOldNorse() },
                async { revalidatorService.revalidateOldIcelandic() },
                async { revalidatorService.revalidateOldSwedish() },
                async { revalidatorService.revalidateOldNorwegian() },
            )

            awaitAll(*tasks.toTypedArray())
        }

        return RevalidationResponse(
            mapOf(
                DictionaryType.OLD_NORSE to true,
                DictionaryType.OLD_ICELANDIC to true,
                DictionaryType.OLD_SWEDISH to true,
                DictionaryType.OLD_NORWEGIAN to true,
            )
        )
    }
}