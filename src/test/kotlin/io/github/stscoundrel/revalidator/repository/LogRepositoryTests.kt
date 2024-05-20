package io.github.stscoundrel.revalidator.repository

import io.github.stscoundrel.revalidator.entities.Log
import io.github.stscoundrel.revalidator.enums.DictionaryType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class LogRepositoryTests @Autowired constructor(
    private val logRepository: LogRepository
) {

    @BeforeEach
    fun setupLogs() {
        val icelandicLog1 = Log(message = "Icelandic test message 1", dictionaryType = DictionaryType.OLD_ICELANDIC)
        val icelandicLog2 = Log(message = "Icelandic test message 2", dictionaryType = DictionaryType.OLD_ICELANDIC)

        val norseLog1 = Log(message = "Norse test message 1", dictionaryType = DictionaryType.OLD_NORSE)
        val norseLog2 = Log(message = "Norse test message 2", dictionaryType = DictionaryType.OLD_NORSE)
        val norseLog3 = Log(message = "Norse test message 3", dictionaryType = DictionaryType.OLD_NORSE)

        val norwegianLog1 = Log(message = "Norwegian test message 1", dictionaryType = DictionaryType.OLD_NORWEGIAN)

        val swedishLog1 = Log(message = "Swedish test message 1", dictionaryType = DictionaryType.OLD_SWEDISH)
        val swedishLog2 = Log(message = "Swedish test message 2", dictionaryType = DictionaryType.OLD_SWEDISH)
        val swedishLog3 = Log(message = "Swedish test message 3", dictionaryType = DictionaryType.OLD_SWEDISH)
        val swedishLog4 = Log(message = "Swedish test message 4", dictionaryType = DictionaryType.OLD_SWEDISH)

        val danishLog1 = Log(message = "Danish test message 1", dictionaryType = DictionaryType.OLD_DANISH)

        logRepository.saveAll(
            listOf(
                icelandicLog1,
                icelandicLog2,
                norseLog1,
                norseLog2,
                norseLog3,
                norwegianLog1,
                swedishLog1,
                swedishLog2,
                swedishLog3,
                swedishLog4,
                danishLog1
            )
        )
    }

    @Test
    fun getsAll() {
        val allLogs = logRepository.findAllByOrderByTimestampDesc()
        assertEquals(11, allLogs.size)
    }

    @Test
    fun findsLogsByDictionaryType() {
        val icelandicLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_ICELANDIC)
        val norseLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_NORSE)
        val norwegianLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_NORWEGIAN)
        val swedishLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_SWEDISH)
        val danishLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_DANISH)

        assertEquals(2, icelandicLogs.size)
        assertEquals(3, norseLogs.size)
        assertEquals(1, norwegianLogs.size)
        assertEquals(4, swedishLogs.size)
        assertEquals(1, danishLogs.size)

        icelandicLogs.forEach {
            assertEquals(DictionaryType.OLD_ICELANDIC, it.dictionaryType)
        }

        norseLogs.forEach {
            assertEquals(DictionaryType.OLD_NORSE, it.dictionaryType)
        }

        norwegianLogs.forEach {
            assertEquals(DictionaryType.OLD_NORWEGIAN, it.dictionaryType)
        }

        swedishLogs.forEach {
            assertEquals(DictionaryType.OLD_SWEDISH, it.dictionaryType)
        }

        danishLogs.forEach {
            assertEquals(DictionaryType.OLD_DANISH, it.dictionaryType)
        }
    }

    @Test
    fun returnsFoundLogsLatestFirst() {
        val logs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_SWEDISH)
        val expectedLogs = listOf(
            "Swedish test message 4",
            "Swedish test message 3",
            "Swedish test message 2",
            "Swedish test message 1",
        )

        // Initial logs in expected order.
        assertEquals(
            expectedLogs,
            logs.map { it.message }
        )

        // Add additional log.
        logRepository.save(
            Log(
                message = "Swedish test message 5, which is totally new",
                dictionaryType = DictionaryType.OLD_SWEDISH,
            )
        )

        val updatedLogs = logRepository.findByDictionaryTypeOrderByTimestampDesc(DictionaryType.OLD_SWEDISH)

        assertEquals(
            listOf(
                "Swedish test message 5, which is totally new",
                "Swedish test message 4",
                "Swedish test message 3",
                "Swedish test message 2",
                "Swedish test message 1",
            ),
            updatedLogs.map { it.message }
        )
    }
}