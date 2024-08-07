package io.github.stscoundrel.revalidator.service

import io.github.stscoundrel.revalidator.entities.Log
import io.github.stscoundrel.revalidator.enums.DictionaryType
import io.github.stscoundrel.revalidator.repository.LogRepository
import org.springframework.stereotype.Service

@Service
class LogService(private val logRepository: LogRepository) {
    private fun saveLog(message: String, dictionaryType: DictionaryType) {
        val log = Log(message = message, dictionaryType = dictionaryType)
        logRepository.save(log)
    }

    fun log(message: String, dictionaryType: DictionaryType) {
        println("${dictionaryType.name}: $message")
        saveLog(message, dictionaryType)
    }

    fun getAll(): List<Log> {
        return logRepository.findAllByOrderByTimestampDesc()
    }

    fun getOldNorse(): List<Log> {
        return logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_NORSE)
    }

    fun getOldIcelandic(): List<Log> {
        return logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_ICELANDIC)
    }

    fun getOldNorwegian(): List<Log> {
        return logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_NORWEGIAN)
    }

    fun getOldSwedish(): List<Log> {
        return logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_SWEDISH)
    }

    fun getOldDanish(): List<Log> {
        return logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_DANISH)
    }
}