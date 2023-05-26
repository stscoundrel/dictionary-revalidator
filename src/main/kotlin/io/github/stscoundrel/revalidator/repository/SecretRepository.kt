package io.github.stscoundrel.revalidator.repository

import org.springframework.stereotype.Repository

@Repository
class SecretRepository {
    fun getOldNorseSecret(): String {
        return System.getenv("OLD_NORSE_SECRET")
    }

    fun getOldIcelandicSecret(): String {
        return System.getenv("OLD_ICELANDIC_SECRET")
    }

    fun getOldSwedishSecret(): String {
        return System.getenv("OLD_SWEDISH_SECRET")
    }

    fun getOldNorwegianSecret(): String {
        return System.getenv("OLD_NORWEGIAN_SECRET")
    }
}