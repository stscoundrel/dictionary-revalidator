package io.github.stscoundrel.revalidator.service

import io.github.stscoundrel.revalidator.enum.DictionaryType
import io.github.stscoundrel.revalidator.repository.RevalidatorConfigRepository
import io.github.stscoundrel.revalidator.revalidators.Revalidator
import org.springframework.stereotype.Service

@Service
class RevalidatorService(val configRepository: RevalidatorConfigRepository, val httpClient: HTTPClient) {
    val revalidators: MutableMap<DictionaryType, Revalidator?> = mutableMapOf(
        DictionaryType.OLD_NORSE to null,
        DictionaryType.OLD_ICELANDIC to null,
        DictionaryType.OLD_SWEDISH to null,
        DictionaryType.OLD_NORWEGIAN to null,
    )

    private fun getDictionaryRevalidator(dictionaryType: DictionaryType): Revalidator {
        if (revalidators[dictionaryType] == null) {
            val config = configRepository.getConfig(dictionaryType)
            revalidators[dictionaryType] = Revalidator(config, httpClient)
        }

        return revalidators[dictionaryType]!!
    }

    fun revalidateOldNorse() {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_NORSE)
        dictionary.revalidate()
    }

    fun revalidateOldIcelandic() {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_ICELANDIC)
        dictionary.revalidate()
    }

    fun revalidateOldSwedish() {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_SWEDISH)
        dictionary.revalidate()
    }

    fun revalidateOldNorwegian() {
        val dictionary = getDictionaryRevalidator(DictionaryType.OLD_NORWEGIAN)
        dictionary.revalidate()
    }
}