package io.github.stscoundrel.revalidator.entities

import io.github.stscoundrel.revalidator.enums.DictionaryType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Log(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING)
    val dictionaryType: DictionaryType
) {
    // Default constructor, mainly to sidestep serialization issues.
    constructor() : this(0, "", LocalDateTime.now(), DictionaryType.OLD_NORSE)
}