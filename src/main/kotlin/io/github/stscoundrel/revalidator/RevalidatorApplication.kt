package io.github.stscoundrel.revalidator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RevalidatorApplication

fun main(args: Array<String>) {
    runApplication<RevalidatorApplication>(*args)
}


