package io.github.stscoundrel.revalidator.revalidators

import io.github.stscoundrel.revalidator.service.HTTPClient
import io.github.stscoundrel.revalidator.repository.RevalidatorConfig
import io.github.stscoundrel.revalidator.enum.DictionaryType


class Revalidator {
    private val dictionary: DictionaryType
    private val baseUrl: String
    private val secret: String
    private val words: Int
    private val httpClient: HTTPClient

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

    private fun log(message: String) {
        println("${dictionary.name}: $message")
    }

    fun revalidate() {
        var start = 0

        try {
            val retries = mutableListOf<Pair<Int, Int>>()
            while (start < words) {
                val end = start + 250
                val statusCode = httpClient.get(getUrl(start, end))
                if (statusCode == 200) {
                    log("OK ${statusCode}: Finished words $start - $end")
                } else {
                    log("FAIL ${statusCode}: Failed words $start - $end! Adding to retries...")
                    retries.add(start to end)
                }
                start = end
            }

            if (retries.isNotEmpty()) {
                log("Starting retries. Total of ${retries.size} batches:")

                for ((retryStart, retryEnd) in retries) {
                    log("$retryStart - $retryEnd")
                    val statusCode = httpClient.get(getUrl(retryStart, retryEnd))
                    if (statusCode == 200) {
                        log("Successful retry $retryStart - $retryEnd :)")
                    } else {
                        log("Failed retry  $retryStart - $retryEnd :(")
                    }
                }
            }

            log("All finished!")
        } catch (e: Exception) {
            println(e)
        }
    }
}