package io.github.stscoundrel.revalidator.repository

import io.github.stscoundrel.revalidator.enums.DictionaryType
import org.springframework.stereotype.Repository

val blueprints: Map<DictionaryType, Triple<String, Int, Int>> = mapOf(
    DictionaryType.OLD_NORSE to Triple("https://cleasby-vigfusson-dictionary.vercel.app", 35207, 200),
    DictionaryType.OLD_ICELANDIC to Triple("https://old-icelandic.vercel.app", 29951, 250),
    DictionaryType.OLD_SWEDISH to Triple("https://old-swedish-dictionary.vercel.app", 41744, 50),
    DictionaryType.OLD_NORWEGIAN to Triple("https://old-norwegian-dictionary.vercel.app", 42021, 50),
    DictionaryType.OLD_DANISH to Triple("https://old-danish-dictionary.vercel.app", 45500, 200),
)

data class RevalidatorConfig(
    val dictionary: DictionaryType,
    val url: String,
    val secret: String,
    val words: Int,
    val batchSize: Int,
)

@Repository
class RevalidatorConfigRepository(val secretRepository: SecretRepository) {
    private fun getSecret(dictionaryType: DictionaryType): String {
        return when (dictionaryType) {
            DictionaryType.OLD_NORSE -> secretRepository.getOldNorseSecret()
            DictionaryType.OLD_ICELANDIC -> secretRepository.getOldIcelandicSecret()
            DictionaryType.OLD_NORWEGIAN -> secretRepository.getOldNorwegianSecret()
            DictionaryType.OLD_SWEDISH -> secretRepository.getOldSwedishSecret()
            DictionaryType.OLD_DANISH -> secretRepository.getOldDanishSecret()
        }
    }

    fun getConfig(dictionaryType: DictionaryType): RevalidatorConfig {
        val (url, words, batchSize) = blueprints[dictionaryType]!!
        val secret = getSecret(dictionaryType)

        return RevalidatorConfig(
            dictionary = dictionaryType,
            url = url,
            secret = secret,
            words = words,
            batchSize = batchSize
        )
    }
}