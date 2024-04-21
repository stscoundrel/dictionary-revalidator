package io.github.stscoundrel.revalidator.repository

import io.github.stscoundrel.revalidator.entities.Log
import io.github.stscoundrel.revalidator.enums.DictionaryType
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long> {
    fun findByDictionaryType(dictionaryType: DictionaryType): List<Log>
}