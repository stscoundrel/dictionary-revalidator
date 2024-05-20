package io.github.stscoundrel.revalidator.repository

import io.github.stscoundrel.revalidator.entities.Log
import io.github.stscoundrel.revalidator.enums.DictionaryType
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long> {
    fun findAllByOrderByTimestampDesc(): List<Log>
    fun findByDictionaryTypeOrderByTimestampDesc(dictionaryType: DictionaryType): List<Log>
}