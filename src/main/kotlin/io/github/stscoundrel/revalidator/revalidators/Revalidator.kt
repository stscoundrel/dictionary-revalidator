package io.github.stscoundrel.revalidator.revalidators

import io.github.stscoundrel.revalidator.service.HTTPClient
import io.github.stscoundrel.revalidator.repository.RevalidatorConfig
import io.github.stscoundrel.revalidator.enum.DictionaryType


class Revalidator {
    val dictionary: DictionaryType
    val baseUrl: String
    val secret: String
    val words: Int
    val httpClient: HTTPClient

    constructor(config: RevalidatorConfig, httpClient: HTTPClient) {
        this.dictionary = config.dictionary
        this.baseUrl = config.url
        this.secret = config.secret
        this.words = config.words
        this.httpClient = httpClient
    }

    private fun getUrl(start: Int, end: Int): String {
        return "$baseUrl/api/revalidate?secret=$secret&start=$start&end=$end"
    }

    fun revalidate() {
        var start = 0

        try {
            val retrys = mutableListOf<Pair<Int, Int>>()
            while (start < words) {
                val end = start + 250
                val statusCode = httpClient.get(getUrl(start, end))
                if (statusCode == 200) {
                    println("OK ${statusCode}: Finished words $start - $end")
                } else {
                    println("FAIL ${statusCode}: Failed words $start - $end! Adding to retrys...")
                    retrys.add(start to end)
                }
                start = end
            }

            println("Starting retrys:")

            for ((retryStart, retryEnd) in retrys) {
                println("$retryStart - $retryEnd")
                val statusCode = httpClient.get(getUrl(retryStart, retryEnd))
                if (statusCode == 200) {
                    println("Successful retry :)")
                } else {
                    println("Failed retry :(")
                }
            }

            println("All finished!")
        } catch (e: Exception) {
            println(e)
        }
    }
}