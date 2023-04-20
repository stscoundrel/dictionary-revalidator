package io.github.stscoundrel.revalidator.repository

import org.springframework.stereotype.Repository
import io.github.stscoundrel.revalidator.enum.DictionaryType

val blueprints: Map<DictionaryType, Triple<String, String, Int>> = mapOf(
    DictionaryType.OLD_NORSE to Triple("https://cleasby-vigfusson-dictionary.vercel.app", System.getenv("OLD_NORSE_SECRET"), 35207),
    DictionaryType.OLD_ICELANDIC to Triple("https://old-icelandic.vercel.app", System.getenv("OLD_ICELANDIC_SECRET"), 29951),
    DictionaryType.OLD_SWEDISH to Triple("https://old-swedish-dictionary.vercel.app", System.getenv("OLD_SWEDISH_SECRET"), 41744),
    DictionaryType.OLD_NORWEGIAN to Triple("https://old-norwegian-dictionary.vercel.app", System.getenv("OLD_NORWEGIAN_SECRET"), 42021),
)

data class RevalidatorConfig(
    val dictionary: DictionaryType,
    val url: String,
    val secret: String,
    val words: Int,
)

@Repository
class RevalidatorConfigRepository {
    fun getConfig(dictionaryType: DictionaryType):  RevalidatorConfig {
        val (url, secret, words) = blueprints[dictionaryType]!!

        return RevalidatorConfig(
            dictionary=dictionaryType,
            url=url,
            secret=secret,
            words=words
        )
    }
}