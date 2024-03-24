package io.github.stscoundrel.revalidator.repository


import io.github.stscoundrel.revalidator.enums.DictionaryType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class RevalidatorConfigRepositoryTests {
    private val secretRepository: SecretRepository = mock(SecretRepository::class.java)

    @Test
    fun usesSecretsFromEnv() {
        `when`(secretRepository.getOldNorseSecret()).thenReturn("secret1")
        `when`(secretRepository.getOldIcelandicSecret()).thenReturn("secret2")
        `when`(secretRepository.getOldNorwegianSecret()).thenReturn("secret3")
        `when`(secretRepository.getOldSwedishSecret()).thenReturn("secret4")
        `when`(secretRepository.getOldDanishSecret()).thenReturn("secret5")

        val repository = RevalidatorConfigRepository(secretRepository)
        val oldNorse = repository.getConfig(DictionaryType.OLD_NORSE)
        val oldIcelandic = repository.getConfig(DictionaryType.OLD_ICELANDIC)
        val oldNorwegian = repository.getConfig(DictionaryType.OLD_NORWEGIAN)
        val oldSwedish = repository.getConfig(DictionaryType.OLD_SWEDISH)
        val oldDanish = repository.getConfig(DictionaryType.OLD_DANISH)

        assertEquals("secret1", oldNorse.secret)
        assertEquals("secret2", oldIcelandic.secret)
        assertEquals("secret3", oldNorwegian.secret)
        assertEquals("secret4", oldSwedish.secret)
        assertEquals("secret5", oldDanish.secret)
    }
}