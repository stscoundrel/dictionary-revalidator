package io.github.stscoundrel.revalidator.repository

import org.springframework.stereotype.Repository
import io.github.stscoundrel.revalidator.enum.DictionaryType
import io.github.stscoundrel.revalidator.revalidators.Revalidator

val blueprints: Map<DictionaryType, Pair<String, Int>> = mapOf(
    DictionaryType.OLD_NORSE to Pair("https://cleasby-vigfusson-dictionary.vercel.app", 35207),
    DictionaryType.OLD_ICELANDIC to Pair("https://old-icelandic.vercel.app", 29951),
    DictionaryType.OLD_SWEDISH to Pair("https://old-swedish-dictionary.vercel.app", 41744),
    DictionaryType.OLD_NORWEGIAN to Pair("https://old-norwegian-dictionary.vercel.app", 42021),
)

data class RevalidatorConfig(
    val dictionary: DictionaryType,
    val url: String,
    val secret: String,
    val words: Int,
)

@Repository
class RevalidatorConfigRepository(val secretRepository: SecretRepository) {
    private fun getSecret(dictionaryType: DictionaryType): String {
        return when (dictionaryType) {
            DictionaryType.OLD_NORSE -> secretRepository.getOldNorseSecret()
            DictionaryType.OLD_ICELANDIC -> secretRepository.getOldIcelandicSecret()
            DictionaryType.OLD_NORWEGIAN -> secretRepository.getOldNorwegianSecret()
            DictionaryType.OLD_SWEDISH -> secretRepository.getOldSwedishSecret()
        }
    }

    fun getConfig(dictionaryType: DictionaryType): RevalidatorConfig {
        val (url, words) = blueprints[dictionaryType]!!
        val secret = getSecret(dictionaryType)

        return RevalidatorConfig(
            dictionary = dictionaryType,
            url = url,
            secret = secret,
            words = words
        )
    }
}