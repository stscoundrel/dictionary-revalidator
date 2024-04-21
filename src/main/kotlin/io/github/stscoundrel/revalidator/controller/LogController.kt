package io.github.stscoundrel.revalidator.controller

import io.github.stscoundrel.revalidator.entities.Log
import io.github.stscoundrel.revalidator.service.LogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/logs")
class LogController(private val logService: LogService) {
    @GetMapping
    fun getAllLogs(): List<Log> {
        return logService.getAll()
    }

    @GetMapping("/old-norse")
    fun getOldNorseLogs(): List<Log> {
        return logService.getOldNorse()
    }

    @GetMapping("/old-icelandic")
    fun getOldIcelandicLogs(): List<Log> {
        return logService.getOldIcelandic()
    }

    @GetMapping("/old-swedish")
    fun getOldSwedishLogs(): List<Log> {
        return logService.getOldSwedish()
    }

    @GetMapping("/old-norwegian")
    fun getOldNorwegianLogs(): List<Log> {
        return logService.getOldNorwegian()
    }

    @GetMapping("/old-danish")
    fun getOldDanishLogs(): List<Log> {
        return logService.getOldDanish()
    }
}