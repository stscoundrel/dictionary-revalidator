package io.github.stscoundrel.revalidator.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import io.github.stscoundrel.revalidator.service.RevalidatorService
import io.github.stscoundrel.revalidator.enum.DictionaryType


data class RevalidationResponse(val statuses: Map<DictionaryType, Boolean>)

@RestController
@RequestMapping("/api")
class RevalidateController(val revalidatorService: RevalidatorService) {
    @GetMapping("/old-norse")
    fun oldNorse(): RevalidationResponse {
        revalidatorService.revalidateOldNorse()
        return RevalidationResponse(mapOf(DictionaryType.OLD_NORSE to true))
    }

    @GetMapping("/old-icelandic")
    fun oldIcelandic(): RevalidationResponse {
        revalidatorService.revalidateOldIcelandic()
        return RevalidationResponse(mapOf(DictionaryType.OLD_ICELANDIC to true))
    }

    @GetMapping("/old-swedish")
    fun oldSwedish(): RevalidationResponse {
        revalidatorService.revalidateOldSwedish()
        return RevalidationResponse(mapOf(DictionaryType.OLD_SWEDISH to true))
    }

    @GetMapping("/old-norwegian")
    fun oldNorwegian(): RevalidationResponse {
        revalidatorService.revalidateOldNorwegian()
        return RevalidationResponse(mapOf(DictionaryType.OLD_NORWEGIAN to true))
    }

    @GetMapping("/")
    fun all(): RevalidationResponse {
        revalidatorService.revalidateOldIcelandic()
        revalidatorService.revalidateOldNorse()
        revalidatorService.revalidateOldNorwegian()
        revalidatorService.revalidateOldSwedish()


        return RevalidationResponse(mapOf(
            DictionaryType.OLD_NORSE to true,
            DictionaryType.OLD_ICELANDIC to true,
            DictionaryType.OLD_SWEDISH to true,
            DictionaryType.OLD_NORWEGIAN to true,
        ))
    }
}