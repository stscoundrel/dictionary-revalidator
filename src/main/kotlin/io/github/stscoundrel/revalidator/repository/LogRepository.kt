package io.github.stscoundrel.revalidator.repository

import io.github.stscoundrel.revalidator.entities.Log
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long>