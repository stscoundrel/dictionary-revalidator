package io.github.stscoundrel.revalidator.controller

import io.github.stscoundrel.revalidator.enums.DictionaryType
import io.github.stscoundrel.revalidator.revalidators.validateRevalidationParameters
import io.github.stscoundrel.revalidator.service.RevalidatorService
import kotlinx.coroutines.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


data class RevalidationResponse(val statuses: Map<DictionaryType, Boolean>)

@RestController
@RequestMapping("/api/revalidate")
class RevalidateController(val revalidatorService: RevalidatorService) {

    @ExceptionHandler(IllegalArgumentException::class)
    fun invalidParameters(exception: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.badRequest().body(mapOf("error" to (exception.message ?: "Invalid revalidation parameters")))
    }

    @GetMapping("/old-norse")
    fun oldNorse(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(start, end, batchSize, retries)
        revalidatorService.revalidateOldNorse(start, end, batchSize, retries)
        RevalidationResponse(mapOf(DictionaryType.OLD_NORSE to true))
    }

    @GetMapping("/old-icelandic")
    fun oldIcelandic(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(start, end, batchSize, retries)
        revalidatorService.revalidateOldIcelandic(start, end, batchSize, retries)
        RevalidationResponse(mapOf(DictionaryType.OLD_ICELANDIC to true))
    }

    @GetMapping("/old-swedish")
    fun oldSwedish(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(start, end, batchSize, retries)
        revalidatorService.revalidateOldSwedish(start, end, batchSize, retries)
        RevalidationResponse(mapOf(DictionaryType.OLD_SWEDISH to true))
    }

    @GetMapping("/old-norwegian")
    fun oldNorwegian(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(start, end, batchSize, retries)
        revalidatorService.revalidateOldNorwegian(start, end, batchSize, retries)
        RevalidationResponse(mapOf(DictionaryType.OLD_NORWEGIAN to true))
    }

    @GetMapping("/old-danish")
    fun oldDanish(
        @RequestParam("start") start: Int? = null,
        @RequestParam("end") end: Int? = null,
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(start, end, batchSize, retries)
        revalidatorService.revalidateOldDanish(start, end, batchSize, retries)
        RevalidationResponse(mapOf(DictionaryType.OLD_DANISH to true))
    }

    @GetMapping("/all")
    fun all(
        @RequestParam("batch") batchSize: Int? = null,
        @RequestParam("retries") retries: Int? = null
    ): RevalidationResponse = runBlocking {
        validateRevalidationParameters(batchSize = batchSize, retriesCount = retries)
        revalidatorService.revalidateAll(batchSize = batchSize, retriesCount = retries)

        RevalidationResponse(
            mapOf(
                DictionaryType.OLD_NORSE to true,
                DictionaryType.OLD_ICELANDIC to true,
                DictionaryType.OLD_SWEDISH to true,
                DictionaryType.OLD_NORWEGIAN to true,
                DictionaryType.OLD_DANISH to true,
            )
        )
    }
}
