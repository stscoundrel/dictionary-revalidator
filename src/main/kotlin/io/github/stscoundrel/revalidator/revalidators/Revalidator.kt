package io.github.stscoundrel.revalidator.revalidators

import io.github.stscoundrel.revalidator.enums.DictionaryType
import io.github.stscoundrel.revalidator.repository.RevalidatorConfig
import io.github.stscoundrel.revalidator.service.HTTPClient
import org.springframework.http.HttpStatus

val defaultRetries = 1

class Revalidator {
    private val dictionary: DictionaryType
    private val baseUrl: String
    private val secret: String
    private val words: Int
    private val batchSize: Int
    private val httpClient: HTTPClient

    constructor(config: RevalidatorConfig, httpClient: HTTPClient) {
        this.dictionary = config.dictionary
        this.baseUrl = config.url
        this.secret = config.secret
        this.words = config.words
        this.batchSize = config.batchSize
        this.httpClient = httpClient
    }

    private fun getUrl(start: Int, end: Int): String {
        return "$baseUrl/api/revalidate?secret=$secret&start=$start&end=$end"
    }

    private fun log(message: String) {
        println("${dictionary.name}: $message")
    }

    private fun retry(retries: List<Pair<Int, Int>>, currentRetry: Int): MutableList<Pair<Int, Int>> {
        log("Starting round ${currentRetry} of retries.")
        val failedRetries = mutableListOf<Pair<Int, Int>>()
        for ((retryStart, retryEnd) in retries) {
            log("Retries round ${currentRetry}: $retryStart - $retryEnd")
            val statusCode = httpClient.get(getUrl(retryStart, retryEnd))
            if (statusCode == HttpStatus.OK.value()) {
                log("Retries round ${currentRetry}: Successful retry $retryStart - $retryEnd :)")
            } else {
                log("Retries round ${currentRetry}: Failed retry  $retryStart - $retryEnd :(")
                failedRetries.add(retryStart to retryEnd)
            }
        }

        return failedRetries
    }

    fun revalidate(start: Int?, end: Int?, customBatchSize: Int?, retriesCount: Int?) {
        var current = start ?: 0
        val max = end ?: words
        val batch = customBatchSize ?: batchSize
        val maxRetries = retriesCount ?: defaultRetries

        try {
            val retries = mutableListOf<Pair<Int, Int>>()
            while (current < max) {
                val rangeEnd = current + batch
                val statusCode = httpClient.get(getUrl(current, rangeEnd))
                if (statusCode == HttpStatus.OK.value()) {
                    log("OK ${statusCode}: Finished words $current - $rangeEnd")
                } else {
                    log("FAIL ${statusCode}: Failed words $current - $rangeEnd! Adding to retries...")
                    retries.add(current to rangeEnd)
                }
                current = rangeEnd
            }

            if (retries.isNotEmpty()) {
                log("Starting retries. Total of ${retries.size} batches:")
                var failedRetries = retries

                var currentRetry = 1

                while (currentRetry <= maxRetries && failedRetries.isNotEmpty()) {
                    failedRetries = retry(failedRetries, currentRetry)
                    currentRetry += 1
                }
            }

            log("All finished!")
        } catch (e: Exception) {
            println(e)
        }
    }
}